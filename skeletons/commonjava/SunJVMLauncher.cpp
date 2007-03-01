/*
  JSmooth: a VM wrapper toolkit for Windows
  Copyright (C) 2003-2007 Rodrigo Reyes <reyes@charabia.net>

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Library General Public
  License as published by the Free Software Foundation; either
  version 2 of the License, or (at your option) any later version.
  
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Library General Public License for more details.
  
  You should have received a copy of the GNU Library General Public
  License along with this library; if not, write to the Free
  Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
  
*/

#include "SunJVMLauncher.h"

extern "C" {
  static jint JNICALL  myvprintf(FILE *fp, const char *format, va_list args)
  {
        DEBUG("MYPRINTF");
  }
  void JNICALL myexit(jint code)
  {
       DEBUG("EXIT CALLED FROM JVM DLL");        
       exit(code); 
  }
}

std::string SunJVMLauncher::toString() const
{
    return "<" + JavaHome + "><" + RuntimeLibPath + "><" + VmVersion.toString() + ">";
}

bool SunJVMLauncher::run(ResourceManager& resource, const string& origin)
{
  // patch proposed by zregvart
  // if you're using bundeled JVM, you apriori
  // specified version, and the version check
  // would require instantiateing JVM which is
  // an overhead in both code and runtime
    if (origin != "bundled") {

      if ( ! VmVersion.isValid())
	{
	  // If there is a JavaHome, let's try to guess the version of the JVM
	  if (this->JavaHome.size() > 0)
	    {
	      this->VmVersion = guessVersionByProcess(FileUtils::concFile(this->JavaHome, "\\bin\\java.exe"));

	      if (!VmVersion.isValid())
		{
		  this->VmVersion = guessVersionByProcess(FileUtils::concFile(this->JavaHome, "\\bin\\jre.exe"));
		}
	    }
	  
	  if (! VmVersion.isValid())
	    return false;
	}
      
      Version max(resource.getProperty(ResourceManager:: KEY_MAXVERSION));
      Version min(resource.getProperty(ResourceManager:: KEY_MINVERSION));
  
      if (min.isValid() && (VmVersion < min))
        return false;
      
      if (max.isValid() && (max < VmVersion))
	return false;
    }

    DEBUG("Launching " + toString());
    
    //
    // search for the dll if it's not set in the registry...
    //
    if ((this->RuntimeLibPath.size() == 0) && (this->JavaHome.size()>0))
      {
	std::string assump = FileUtils::concFile(this->JavaHome, "jre\\bin\\jvm.dll");
	std::string assump2 = FileUtils::concFile(this->JavaHome, "jre\\bin\\server\\jvm.dll"); // for JRE 1.5+
	std::string assump3 = FileUtils::concFile(this->JavaHome, "jre\\bin\\client\\jvm.dll"); // for JRE 1.5+
	std::string assump4 = FileUtils::concFile(this->JavaHome, "bin\\javai.dll"); // For JRE 1.1

	if (FileUtils::fileExists(assump))
	  this->RuntimeLibPath = assump;
	else if (FileUtils::fileExists(assump2))
	  this->RuntimeLibPath = assump2;
	else if (FileUtils::fileExists(assump3))
	  this->RuntimeLibPath = assump3;
	else if (FileUtils::fileExists(assump4))
	  this->RuntimeLibPath = assump4;
	else
	  {
	    vector<string> dlls = FileUtils::recursiveSearch(this->JavaHome, string("jvm.dll"));
	    if (dlls.size() > 0)
	      this->RuntimeLibPath = dlls[0];
	  }
      }


    // First, set up the VM
    if (Version("1.2") <= VmVersion)
      {
	if (!setupVM12DLL(resource, origin))
	  {
	    DEBUG("Can't set up the VM12DLL");
	    return false;
	  }
      } 
    else if (Version("1.1") <= VmVersion)
      {
	if (!setupVM11DLL(resource, origin))
	  {
	    DEBUG("Can't set up the VM11DLL");
	    return false;
	  }
    }

    if ((m_javavm != 0) && (m_javaenv != 0))
      {
	return runVMDLL(resource, origin);
      }
    return false;
}

bool SunJVMLauncher::runProc(ResourceManager& resource, bool noConsole, const string& origin)
{
    Version max(resource.getProperty(ResourceManager:: KEY_MAXVERSION));
    Version min(resource.getProperty(ResourceManager:: KEY_MINVERSION));

    string javapath = "bin\\java.exe";
    string jrepath = "bin\\jre.exe";
    std::string exepath = StringUtils::requote( FileUtils::concFile(this->JavaHome, javapath) );

    DEBUG("trying " + exepath);
    if (FileUtils::fileExists(exepath) == false)
      {
	exepath = StringUtils::requote( FileUtils::concFile(this->JavaHome, jrepath) );
	DEBUG("trying " + exepath);
      }

    if (FileUtils::fileExists(exepath) == false)
      {
	DEBUG("Neither java. or jre.exe were at " + this->JavaHome +" ... aborting");
	return false;
      }

    DEBUG("Running process " + origin +" ... " + min.toString() + " <= " + VmVersion.toString() + "<= " + max.toString());
    Version curver = VmVersion;
    
    if (curver.isValid() == false)
    {
        DEBUG(origin + ": check version for " + exepath);
        Version vjava = guessVersionByProcess(exepath);
        DEBUG("Java Version detected: " + vjava.toString());
        if (vjava.isValid())
	  {
                curver = vjava;
                VmVersion = vjava;
	  }
    }
    
    if (curver.isValid() == false)
        return false;
    
    if (min.isValid() && (curver < min))
        return false;

    if (max.isValid() && (max < curver))
        return false;

    DEBUG("Version of VM checked... OK");
    
    if (Version("1.2") <= VmVersion)
    {
        return runVM12proc(resource, noConsole, origin);
    } else if (Version("1.1") <= VmVersion)
    {
        return runVM11proc(resource, noConsole, origin);
    }
    
    return false;
}

bool SunJVMLauncher::setupVM12DLL(ResourceManager& resource, const string& origin)
{
  std::string jarpath = resource.saveJarInTempFile();
  HINSTANCE vmlib = LoadLibrary(this->RuntimeLibPath.c_str());
 
  if (vmlib != 0)
    {
      DEBUG("v1.2+ type VM loaded from " +this->RuntimeLibPath);

      CreateJavaVM_t CreateJavaVM = (CreateJavaVM_t)GetProcAddress(vmlib, "JNI_CreateJavaVM");
      GetDefaultJavaVMInitArgs_t GetDefaultJavaVMInitArgs = (GetDefaultJavaVMInitArgs_t)GetProcAddress(vmlib, "JNI_GetDefaultJavaVMInitArgs");
        
      if ((CreateJavaVM != NULL) && (GetDefaultJavaVMInitArgs != NULL))
        {
	  DEBUG("VM Created successfully");
	  JavaVM *vm = new JavaVM();
	  JNIEnv *env = new JNIEnv();

	  //
	  // create the properties array
	  //
	  const vector<JavaProperty>& jprops = resource.getJavaProperties();
	  vector<string> jpropstrv;
	  for (int i=0; i<jprops.size(); i++)
	    {
	      const JavaProperty& jp = jprops[i];
	      string value = jp.getValue();

	      value = StringUtils::replace(value, "${VMSELECTION}", origin);
	      value = StringUtils::replace(value, "${VMSPAWNTYPE}", "JVMDLL");

	      jpropstrv.push_back("-D" + jp.getName() + "=" + StringUtils::fixQuotes(value));
	    }

	  if (resource.getProperty("maxheap") != "")
	    {
	      jpropstrv.push_back("-Xmx" + sizeToString(resource.getProperty("maxheap"))); // the extra space at the end would cause JNI_EINVAL return code in CreateJavaVM

	    }
	  if (resource.getProperty("initialheap") != "")
	    {
	      jpropstrv.push_back("-Xms" + sizeToString(resource.getProperty("initialheap"))); // the extra space at the end would cause JNI_EINVAL return code in CreateJavaVM
	    }
                
	  JavaVMInitArgs vm_args;
	  GetDefaultJavaVMInitArgs(&vm_args);

	  JavaVMOption options[1 + jpropstrv.size()];
	  std::string cpoption = "-Djava.class.path=";
	  string cpath = jarpath;
	  string additionalcpath = resource.getNormalizedClassPath();
	  if ((cpath.size()>0) && (additionalcpath.size()>0))
	    cpath += ';';
	  if (additionalcpath.size()>0)
	    cpath += additionalcpath;
	  //	  cpoption += StringUtils::fixQuotes(cpath);
	  cpoption += cpath;
	  cpoption += "";

	  DEBUG("Classpath: " + cpoption);
	  options[0].optionString =  (char*)cpoption.c_str();
	  vm_args.version = 0x00010002;
	  vm_args.version = JNI_VERSION_1_2;
	  vm_args.options = options;
	  vm_args.nOptions = 1 + jpropstrv.size();
                
	  for (int i=0; i<jpropstrv.size(); i++)
	    {
	      options[1 + i].optionString = (char*)jpropstrv[i].c_str();
	    }
	  for (int i=0; i< 1+jpropstrv.size(); i++)
	    {
	      DEBUG(string("Option added:") + options[i].optionString);
	    }
                
	  vm_args.ignoreUnrecognized = JNI_TRUE;

	  //
	  // Create the VM
	  if (CreateJavaVM( &vm, &env, &vm_args) != 0)
	    {
	      DEBUG("Can't create VM");
	      return false;
	    }
	  DEBUG("VM Created !!");
                
	  jclass clstest = env->FindClass("java/lang/System");
	  if (clstest != 0)
	    {
	      DEBUG("Found java.lang.system !");
	    }
	  else
	    {
	      DEBUG("java.lang.system not found");
	      return false;
	    }                
	
	  m_javavm = vm;
	  m_javaenv = env;

	  return true;
	}
    } 
  else 
    { 
      DEBUG("Warning: can't even load the DLL: " + this->RuntimeLibPath);
    }

  return false;
}

bool SunJVMLauncher::runVMDLL(ResourceManager& resource, const string& origin)
{
  std::string classname = resource.getProperty(string(ResourceManager::KEY_MAINCLASSNAME));  
  classname = StringUtils::replace(classname,".", "/");
  DEBUG("Check if " + classname + " is available");
  jclass cls = (m_javaenv)->FindClass(classname.c_str());
  if (cls == 0)
    {
      DEBUG(std::string("Can't Find CLASS <") + classname + std::string("> (probably a classpath issue)"));
      return false;
    }
  else
    DEBUG("OK, class " + classname + " is available");

  char strbuf[255];
  sprintf(strbuf, "");
  jstring jstr = (m_javaenv)->NewStringUTF(strbuf);
  jmethodID mid = (m_javaenv)->GetStaticMethodID(cls, "main", "([Ljava/lang/String;)V");

  vector<string> pargs = StringUtils::split(resource.getProperty(ResourceManager::KEY_ARGUMENTS), " \t\n\r", "\"\'");
  for (int i=0; i<pargs.size(); i++)
    DEBUG("Argument " + StringUtils::toString(i)+" : <" + pargs[i] + ">");

  jobjectArray args;
    
  if (pargs.size() > 0)
    {
      args = (m_javaenv)->NewObjectArray(pargs.size(), (m_javaenv)->FindClass("java/lang/String"), jstr);
      for (int i=0; i<pargs.size(); i++)
	{
	  jstr = (m_javaenv)->NewStringUTF(pargs[i].c_str());
	  (m_javaenv)->SetObjectArrayElement(args, i, jstr);
	}
    }
  else
    {
      args = (m_javaenv)->NewObjectArray(0, (m_javaenv)->FindClass("java/lang/String"), jstr);
    }
                
  if ((mid != 0) && (args != 0))
    {
      DEBUG("Calling static void main(String[]) method from " + classname);
      m_javaenv->CallStaticVoidMethod(cls, mid, args);
      DEBUG(classname + " call complete");
      m_javavm->DestroyJavaVM();
      DEBUG("VM destroyed");
      return true;
    }
  else
    {
      DEBUG("Can't find method !");
      return false;
    }
}


// bool SunJVMLauncher::runVM12DLL(ResourceManager& resource, const string& origin)
// {
//   if (setupVM12DLL(resource, origin) == false)
//     {
//       DEBUG("CAN'T LOAD DLL");
//       return false;
//     }

//   std::string classname = resource.getProperty(string(ResourceManager::KEY_MAINCLASSNAME));  
//   classname = StringUtils::replace(classname,".", "/");
//   DEBUG("Look for " + classname);
//   jclass cls = (m_javaenv)->FindClass(classname.c_str());
//   if (cls == 0)
//     {
//       char tmpbuf[255];
//       sprintf(tmpbuf, "Cant find <%s> at all!", classname.c_str());
//       DEBUG(tmpbuf);
//       DEBUG(std::string("Can't Find CLASS <") + classname + std::string(">"));
//       return false;
//     }
//   else
//     DEBUG("CLASS FOUND");

//   char strbuf[255];
//   sprintf(strbuf, "");
//   jstring jstr = (m_javaenv)->NewStringUTF(strbuf);
//   jmethodID mid = (m_javaenv)->GetStaticMethodID(cls, "main", "([Ljava/lang/String;)V");

//   vector<string> pargs = StringUtils::split(resource.getProperty(ResourceManager::KEY_ARGUMENTS), " \t\n\r", "\"\'");
//   for (int i=0; i<pargs.size(); i++)
//     DEBUG("ARG:: <" + pargs[i] + ">");

//   jobjectArray args;
    
//   if (pargs.size() > 0)
//     {
//       args = (m_javaenv)->NewObjectArray(pargs.size(), (m_javaenv)->FindClass("java/lang/String"), jstr);
//       for (int i=0; i<pargs.size(); i++)
// 	{
// 	  jstr = (m_javaenv)->NewStringUTF(pargs[i].c_str());
// 	  (m_javaenv)->SetObjectArrayElement(args, i, jstr);
// 	}
//     }
//   else
//     {
//       args = (m_javaenv)->NewObjectArray(0, (m_javaenv)->FindClass("java/lang/String"), jstr);
//     }
                
//   if ((mid != 0) && (args != 0))
//     {
//       m_javaenv->CallStaticVoidMethod(cls, mid, args);
//       DEBUG("VM CALLED !!");
//       m_javavm->DestroyJavaVM();
//       DEBUG("VM DESTROYED !!");
//       return true;
//     }
//   else
//     {
//       DEBUG("Can't find method !");
//       return false;
//     }
// }


bool SunJVMLauncher::setupVM11DLL(ResourceManager& resource, const string& origin)
{
  DEBUG("Setting up a 1.1-type JVM from the DLL " + this->RuntimeLibPath);

  std::string jarpath = resource.saveJarInTempFile();
  std::string extracp = resource.getNormalizedClassPath();

  HINSTANCE vmlib = LoadLibrary(this->RuntimeLibPath.c_str());
      
  if (vmlib != 0)
    {
      DEBUG("DLL successfully loaded from " + this->RuntimeLibPath );
      CreateJavaVM_t CreateJavaVM = (CreateJavaVM_t)GetProcAddress(vmlib, "JNI_CreateJavaVM");
      GetDefaultJavaVMInitArgs_t GetDefaultJavaVMInitArgs = (GetDefaultJavaVMInitArgs_t)GetProcAddress(vmlib, "JNI_GetDefaultJavaVMInitArgs");
        
      if ((CreateJavaVM != NULL) && (GetDefaultJavaVMInitArgs != NULL))
	{
	  DEBUG("Found the CreateJavaVM and GetDefaultJavaVMInitArgs methods in the DLL... that's good");

	  JavaVM *javavm = new JavaVM();
	  JNIEnv *env = new JNIEnv();

	  jint res;
	  jclass cls;
	  jmethodID mid;
	  jstring jstr;
	  jobjectArray args;
              
	  JDK1_1InitArgs vm_args;
	  //vm_args.exit = myexit;
	  vm_args.version = 0x00010001;
	  GetDefaultJavaVMInitArgs(&vm_args);
                  
	  if (resource.getProperty("maxheap") != "")
	    {
	      vm_args.maxHeapSize = StringUtils::parseInt(resource.getProperty("maxheap"));
	    }
	  if (resource.getProperty("initialheap") != "")
	    {
	      vm_args.minHeapSize = StringUtils::parseInt(resource.getProperty("initialheap"));
	    }
                  
	  //
	  // create the properties array
	  //
	  const vector<JavaProperty>& jprops = resource.getJavaProperties();
	  vector<string> jpropstrv;
	  for (int i=0; i<jprops.size(); i++)
	    {
	      const JavaProperty& jp = jprops[i];
	      string value = jp.getValue();

	      value = StringUtils::replace(value, "${VMSELECTION}", origin);
	      value = StringUtils::replace(value, "${VMSPAWNTYPE}", "JVMDLL");

	      jpropstrv.push_back(jp.getName() + "=" + StringUtils::fixQuotes(value));
	    }
      
	  char  const  * props[jprops.size()+1];
	  for (int i=0; i<jpropstrv.size(); i++)
	    {
	      props[i] = jpropstrv[i].c_str();
	    }
	  props[jprops.size()] = NULL;
      
	  vm_args.properties = (char**)props;

	  /* Append USER_CLASSPATH to the default system class path */

	  std::string classpath = vm_args.classpath;
	  classpath += ";" + jarpath;
	  classpath += ";" + extracp;
	  DEBUG("Classpath = " + classpath);
	  vm_args.classpath = (char*)classpath.c_str();

	  /* Create the Java VM */
	  if ((res = CreateJavaVM( &javavm, &env, &vm_args)) < 0)
	    {
	      DEBUG("Can't create VM " + this->RuntimeLibPath);
	      return false;
	    }

	  DEBUG("VM 1.1 successfully created");

	  //
	  // Test java.lang.system
	  if (env->FindClass("java/lang/System") == 0)
	    {
	      DEBUG("java.lang.system not found... aborting the VM setup");
	      return false;
	    }                
	  
	  m_javavm = javavm;
	  m_javaenv = env;

	  return true;
	}
    }

  return false;
}

// bool SunJVMLauncher::runVM11DLL(ResourceManager& resource, const string& origin)
// {
//   if (setupVM11DLL(resource, origin) == false)
//     {
//       DEBUG("CAN'T LOAD DLL ");
//       return false;
//     }
      
//   std::string classname = resource.getProperty(string(ResourceManager::KEY_MAINCLASSNAME));
//   classname = StringUtils::replace(classname,".", "/");                
//   jclass cls = (m_javaenv)->FindClass(classname.c_str());
//   if (cls == 0)
//     {
//       char tmpbuf[255];
//       sprintf(tmpbuf, "Cant find <%s> at all!", classname.c_str());
//       DEBUG(tmpbuf);
//       DEBUG(std::string("Can't Find CLASS <") + classname + std::string(">"));
//       return false;
//     }
//   else
//     DEBUG("CLASS "+ classname +" FOUND");

//   char strbuf[255];
//   sprintf(strbuf, "");
//   jstring jstr = (m_javaenv)->NewStringUTF(strbuf);
//   jmethodID mid = (m_javaenv)->GetStaticMethodID(cls, "main", "([Ljava/lang/String;)V");

//   vector<string> pargs = StringUtils::split(resource.getProperty(ResourceManager::KEY_ARGUMENTS), " \t\n\r", "\"\'");
//   jobjectArray args;

//   if (pargs.size() > 0)
//     {
//       args = (m_javaenv)->NewObjectArray(pargs.size(), (m_javaenv)->FindClass("java/lang/String"), jstr);
//       for (int i=0; i<pargs.size(); i++)
// 	{
// 	  jstr = (m_javaenv)->NewStringUTF(pargs[i].c_str());
// 	  (m_javaenv)->SetObjectArrayElement(args, i, jstr);
// 	}
//     }
//   else
//     {
//       args = (m_javaenv)->NewObjectArray(0, (m_javaenv)->FindClass("java/lang/String"), jstr);
//     }

//   if ((mid != 0) && (args != 0))
//     {
//       m_javaenv->CallStaticVoidMethod(cls, mid, args);
//       DEBUG("VM CALLED !!");
                                
//       m_javavm->DestroyJavaVM();
//       return true;
//     }
//   else
//     {
//       DEBUG("Can't find method !");
//       return false;
//     }
// }

bool SunJVMLauncher::runVM11proc(ResourceManager& resource, bool noConsole, const string& origin)
{
  DEBUG("Running process with 1.1 compatibility mode");

  string javapath = "bin\\java.exe";
  string jrepath = "bin\\jre.exe";
  if (noConsole)
    {
      javapath = "bin\\javaw.exe";
      jrepath = "bin\\jrew.exe";
    }

  std::string exepath = StringUtils::requote( FileUtils::concFile(this->JavaHome, javapath) );
  if (FileUtils::fileExists(exepath) == false)
    exepath = StringUtils::requote( FileUtils::concFile(this->JavaHome, jrepath) );

  if (FileUtils::fileExists(exepath))
    {
      if (runExe(exepath, true, resource, noConsole, "1.1", origin))
	return true;
    }
        
  return false;    
}

bool SunJVMLauncher::runVM12proc(ResourceManager& resource, bool noConsole, const string& origin)
{
    string javapath = "bin\\java.exe";
    string jrepath = "bin\\jre.exe";
    if (noConsole)
    {
        javapath = "bin\\javaw.exe";
        jrepath = "bin\\jrew.exe";
    }

    std::string exepath = StringUtils::requote( FileUtils::concFile(this->JavaHome, javapath) );
    if (FileUtils::fileExists(exepath) == false)
      exepath = StringUtils::requote( FileUtils::concFile(this->JavaHome, jrepath) );

    if (FileUtils::fileExists(exepath))
      {
	if (runExe(exepath, false, resource, noConsole, "1.2+", origin))
	  return true;
      }
        
    return false;
}

bool SunJVMLauncher::runExe(const string& exepath, bool forceFullClasspath, ResourceManager& resource, bool noConsole, const string& version, const string& origin)
{    
  DEBUG("Running new proc for " + exepath);
  
  string embeddedjar = resource.saveJarInTempFile();
  DEBUG("Embedded jar saved at " + embeddedjar);
  string classpath = embeddedjar;

  if (forceFullClasspath && (JavaHome != ""))
    {
      DEBUG("Forcing full classpath");
      vector<string> cpzips = FileUtils::recursiveSearch(JavaHome, "*.zip");
      for (vector<string>::iterator i=cpzips.begin(); i!=cpzips.end(); i++)
	DEBUG("ZIP FILE: " + *i);
      vector<string> cpjars = FileUtils::recursiveSearch(JavaHome, "*.jar");
      for (vector<string>::iterator i=cpjars.begin(); i!=cpjars.end(); i++)
	DEBUG("JAR FILE: " + *i);
      vector<string> fullcp;
      fullcp.insert(fullcp.end(), cpzips.begin(), cpzips.end());
      fullcp.insert(fullcp.end(), cpjars.begin(), cpjars.end());
      for (vector<string>::iterator i=fullcp.begin(); i!=fullcp.end(); i++)
	DEBUG("FULL CP FILE: " + *i);
      string lcp = StringUtils::join(fullcp, ";");
      
      classpath += string(";") + lcp;
    }
      
  string addcp = resource.getNormalizedClassPath();
  classpath += ";" + addcp;
      
  string addargs = resource.getProperty(ResourceManager::KEY_ARGUMENTS);

  string javaproperties = "";
  const vector<JavaProperty>& jprops = resource.getJavaProperties();
  for (vector<JavaProperty>::const_iterator i=jprops.begin(); i != jprops.end(); i++)
    {
      JavaProperty jp = *i;
      string v = jp.getValue();

      v = StringUtils::replace(v, "${VMSELECTION}", origin);
      v = StringUtils::replace(v, "${VMSPAWNTYPE}", "PROC");

      string::iterator t = v.end();
      if (*(--t) == '\\')
	v += "\\";

      javaproperties += " \"-D" + jp.getName() + "\"=" + StringUtils::fixQuotes(v);
    }
      
  if (resource.getProperty("maxheap") != "")
    {
      if (version == "1.1")
	javaproperties += " -mx" + sizeToString(resource.getProperty("maxheap")) + " ";
      else
	javaproperties += " -Xmx" + sizeToString(resource.getProperty("maxheap")) + " ";
    }

  if (resource.getProperty("initialheap") != "")
    {
      if (version == "1.1")
	javaproperties += " -ms" + sizeToString(resource.getProperty("initialheap")) + " ";
      else
	javaproperties += " -Xms" + sizeToString(resource.getProperty("initialheap")) + " ";
    }
 
  string classname = resource.getProperty(string(ResourceManager::KEY_MAINCLASSNAME));
  string arguments = javaproperties + " -classpath \"" + classpath + "\" " + classname + " " + addargs;

  DEBUG("CLASSNAME = <" + classname + ">");
  STARTUPINFO info;
  GetStartupInfo(&info);
  int creationFlags = 0;
  int inheritsHandle;
  if (noConsole == false)
    {
      info.dwFlags = STARTF_USESHOWWINDOW|STARTF_USESTDHANDLES;
      info.hStdOutput = GetStdHandle(STD_OUTPUT_HANDLE);
      info.hStdError = GetStdHandle(STD_ERROR_HANDLE);
      info.hStdInput = GetStdHandle(STD_INPUT_HANDLE);
      creationFlags = NORMAL_PRIORITY_CLASS;
      inheritsHandle = TRUE;
    }
  else
    {
      info.dwFlags = STARTF_USESHOWWINDOW;
      creationFlags = NORMAL_PRIORITY_CLASS | DETACHED_PROCESS;
      inheritsHandle = FALSE;
    }

  PROCESS_INFORMATION procinfo;
  string exeline = StringUtils::fixQuotes(exepath) + " " + arguments;
  int res = CreateProcess(NULL, (char*)exeline.c_str(), NULL, NULL, inheritsHandle, creationFlags, NULL, NULL, &info, &procinfo);

  DEBUG("---------------------------------------------------");
  DEBUG("COMMAND LINE: " +exeline);
  DEBUG("RESULT: " + StringUtils::toString(res));
  if (res != 0)
    {
      DEBUG("WAITING: " + StringUtils::toString(res));
      WaitForSingleObject(procinfo.hProcess, INFINITE);
      DEBUG("WAIT ENDED");

      if (embeddedjar.size()>0)
	{
	  DEBUG("DELETING " + embeddedjar);
	  DeleteFile(embeddedjar.c_str());
	}
      return true;
    }
  else
    {
      DEBUG("Can't run " + exeline);
    }

  if (embeddedjar.size()>0)
    {
      DEBUG("DELETING " + embeddedjar);
      DeleteFile(embeddedjar.c_str());
    }

  return false;
}

Version SunJVMLauncher::guessVersionByProcess(const string& exepath)
{
  Version result;

  // Return immediatly if the exe does not exist
  if (!FileUtils::fileExists(exepath))
    return result;

  string tmpfilename = FileUtils::createTempFileName(".tmp");
  SECURITY_ATTRIBUTES secattrs;
  secattrs.nLength = sizeof(SECURITY_ATTRIBUTES);
  secattrs.lpSecurityDescriptor = NULL;
  secattrs.bInheritHandle = TRUE;
    
  HANDLE tmph = CreateFile(tmpfilename.c_str(), GENERIC_WRITE,
			   FILE_SHARE_WRITE, &secattrs,
			   CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, NULL);
                            
  if (tmph == NULL)
    {
      DEBUG("TEMPH == NULL");
    }
    
  DEBUG("REDIRECTED TMP TO " + tmpfilename);
  STARTUPINFO info;
  GetStartupInfo(&info);
  info.hStdOutput = tmph;
  info.hStdError = tmph;
  info.wShowWindow = TRUE;
  info.dwFlags = STARTF_USESTDHANDLES;
  PROCESS_INFORMATION procinfo;
    
  string exeline = exepath + " -version";
  DEBUG("Running: " + exeline);
  int res = CreateProcess(NULL, (char*)exeline.c_str(), NULL, NULL, 
			  TRUE, NORMAL_PRIORITY_CLASS, NULL, NULL, &info, &procinfo);
      
  if (res != 0)
    {
      WaitForSingleObject(procinfo.hProcess, INFINITE);
      CloseHandle(tmph);

      tmph = CreateFile(tmpfilename.c_str(), GENERIC_READ,
			FILE_SHARE_READ, NULL,
			OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
            
      if (tmph != NULL)
	{
	  DEBUG("Reading temp...");
	  char buffer[128];
	  DWORD hasread;
	  buffer[127] = 0;
	  if (ReadFile(tmph, buffer, 127, &hasread, NULL))
	    {
	      DEBUG(string("DATA READ: ") + buffer);
	      vector<string> split = StringUtils::split(buffer, " \t\n\r", "\"");
	      for (vector<string>::iterator i=split.begin(); i != split.end(); i++)
		{
		  Version v(*i);
		  if (v.isValid())
		    {
		      result = v;
		      break;
		    }
		}
	    }
	  CloseHandle(tmph);
	}
      else
	{
	  DEBUG("Can't open temporary file for result");
	}
    }
  else
    {
      DEBUG("Can't run process");
    }

  DeleteFile(tmpfilename.c_str());
  return result;
}

std::string SunJVMLauncher::sizeToString(std::string size)
{
  if ( (size.find('m') != string::npos)
      || (size.find('M') != string::npos)
      || (size.find('k') != string::npos)
      || (size.find('K') != string::npos) )
    {
      return size;
    }
  else
    return sizeToString(StringUtils::parseInt(size));
}

std::string SunJVMLauncher::sizeToString(int size)
{
    if (size > (1024*1024))
    {
        return StringUtils::toString(size / (1024*1024)) + "m";
    } else if (size > 1024)
    {
        return StringUtils::toString(size / 1024) + "k";
    } else
    {
        return StringUtils::toString(size);
    }
}

bool operator < (const SunJVMLauncher& v1, const SunJVMLauncher& v2)
{
  return v1.VmVersion < v2.VmVersion;
}
