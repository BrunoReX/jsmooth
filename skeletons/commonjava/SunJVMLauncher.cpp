/*
  JSmooth: a VM wrapper toolkit for Windows
  Copyright (C) 2003 Rodrigo Reyes <reyes@charabia.net>

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

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

bool SunJVMLauncher::run(ResourceManager& resource)
{
    if ( ! VmVersion.isValid())
        return false;

    Version max(resource.getProperty(ResourceManager:: KEY_MAXVERSION));
    Version min(resource.getProperty(ResourceManager:: KEY_MINVERSION));
    
    if (min.isValid() && (VmVersion < min))
        return false;

    if (max.isValid() && (max < VmVersion))
        return false;

    DEBUG("Launching " + toString());

    if (Version("1.2") <= VmVersion)
    {
        DEBUG("RUNNING L VM " + VmVersion.toString());
        return runVM12DLL(resource);
    } else if (Version("1.1") <= VmVersion)
    {
        DEBUG("RUNNING L VM11 = " + VmVersion.toString());
        return runVM11DLL(resource);
    }
    
    return false;
}

bool SunJVMLauncher::runProc(ResourceManager& resource, bool noConsole)
{
    Version max(resource.getProperty(ResourceManager:: KEY_MAXVERSION));
    Version min(resource.getProperty(ResourceManager:: KEY_MINVERSION));

    string javapath = "\\bin\\java.exe";
    string jrepath = "\\bin\\jre.exe";

    if (noConsole)
    {
        javapath = "\\bin\\javaw.exe";
        jrepath = "\\bin\\jrew.exe";
    }

    DEBUG("RUN PROC... " + min.toString() + " <= " + VmVersion.toString() + "<= " + max.toString());
    Version curver = VmVersion;

    if (curver.isValid() == false)
    {
        Version vjava = guessVersionByProcess(JavaHome + javapath);
        DEBUG("JAVA VERSION = " + vjava.toString());
        if (vjava.isValid())
                curver = vjava;
        else
        {
             Version vjre = guessVersionByProcess(JavaHome + jrepath);
             DEBUG("JRE VERSION = " + vjre.toString());    
             curver = vjre;
        }
    }
    
    if (curver.isValid() == false)
        return false;
    
    if (min.isValid() && (curver < min))
        return false;

    if (max.isValid() && (max < curver))
        return false;

    DEBUG("RUN PROC... version OK");
    
    if (Version("1.2") <= VmVersion)
    {
        DEBUG("RUNVM12PROC");
        return runVM12proc(resource, noConsole);
    } else if (Version("1.1") <= VmVersion)
    {
        DEBUG("RUNVM11PROC");
        return runVM11proc(resource, noConsole);
    }
    
    return false;
}

bool SunJVMLauncher::runVM12DLL(ResourceManager& resource)
{
    std::string jarpath = resource.saveJarInTempFile();
    std::string classname = resource.getProperty(string(ResourceManager::KEY_MAINCLASSNAME));

    std::string args = resource.getProperty(ResourceManager::KEY_ARGUMENTS);

    vector<string> pargs = StringUtils::split(args, " \t\n\r", "\"\'");
    for (int i=0; i<pargs.size(); i++)
        DEBUG("ARG:: <" + pargs[i] + ">");
    
    HINSTANCE vmlib = LoadLibrary(this->RuntimeLibPath.c_str());
 
    if (vmlib != 0)
    {
        DEBUG("VM12 LOADED!");
        CreateJavaVM_t CreateJavaVM = (CreateJavaVM_t)GetProcAddress(vmlib, "JNI_CreateJavaVM");

        GetDefaultJavaVMInitArgs_t GetDefaultJavaVMInitArgs = (GetDefaultJavaVMInitArgs_t)GetProcAddress(vmlib, "JNI_GetDefaultJavaVMInitArgs");
        
        if ((CreateJavaVM != NULL) && (GetDefaultJavaVMInitArgs != NULL))
        {
                DEBUG("CREATEJAVAVM FN LOADED");
                JavaVM *vm = new JavaVM();
                JNIEnv *env = new JNIEnv();

                jint res;
                jclass cls;
                jmethodID mid;
                jstring jstr;
                jobjectArray args;
                char classpath[2048];
                
                JavaVMInitArgs vm_args;
                JavaVMOption options[4];
                std::string cpoption = "-Djava.class.path=";
                cpoption += jarpath;

                DEBUG("Classpath: " + cpoption);
                options[0].optionString =  (char*)cpoption.c_str();
                vm_args.version = 0x00010002;
                vm_args.options = options;
                vm_args.nOptions = 1;
                
                DEBUG("OPTIONS SET!");
                
                vm_args.ignoreUnrecognized = JNI_FALSE;
                
                GetDefaultJavaVMInitArgs(&vm_args);

                res = CreateJavaVM( &vm, &env, &vm_args);
                if (res != 0)
                {
                                DEBUG("Can't create VM");
                                return false;
                }
                else
                            DEBUG("VM Created !!");
                
                jclass clstest = env->FindClass("java/lang/System");
                if (clstest != 0)
                {
                                DEBUG("FOUND java.lang.system !");
                }
                else
                {
                                DEBUG("java.lang.system NOT FOUND");
                                return false;
                }                
                
                cls = (env)->FindClass(classname.c_str());
                if (cls == 0)
                {
                                char tmpbuf[255];
                                sprintf(tmpbuf, "Cant find <%s> at all!", classname.c_str());
                                DEBUG(tmpbuf);
                                DEBUG(std::string("Can't Find CLASS <") + classname + std::string(">"));
                                return false;
                }
                else
                            DEBUG("CLASS FOUND");

                char strbuf[255];
                sprintf(strbuf, "");
                jstr = (env)->NewStringUTF(strbuf);
                mid = (env)->GetStaticMethodID(cls, "main", "([Ljava/lang/String;)V");
                if (pargs.size() > 0)
                {
                   args = (env)->NewObjectArray(pargs.size(), (env)->FindClass("java/lang/String"), jstr);
                   for (int i=0; i<pargs.size(); i++)
                   {
                       jstr = (env)->NewStringUTF(pargs[i].c_str());
                       (env)->SetObjectArrayElement(args, i, jstr);
                   }
                }
                else
                {
                    args = (env)->NewObjectArray(0, (env)->FindClass("java/lang/String"), jstr);
                }
                
                if ((mid != 0) && (args != 0))
                {
                                env->CallStaticVoidMethod(cls, mid, args);
                                DEBUG("VM CALLED !!");
                                vm->DestroyJavaVM();
                                return true;
                }
                else
                {
                                DEBUG("Can't find method !");
                                return false;
                }
        }
    }
    else
    {
        DEBUG("CAN'T LOAD DLL");
    }
    return false;
}



bool SunJVMLauncher::runVM11DLL(ResourceManager& resource)
{

    std::string jarpath = resource.saveJarInTempFile();
    std::string classname = resource.getProperty(string(ResourceManager::KEY_MAINCLASSNAME));
    std::string extracp = resource.getProperty(string(ResourceManager::KEY_CLASSPATH));

    std::string args = resource.getProperty(ResourceManager::KEY_ARGUMENTS);
    vector<string> pargs = StringUtils::split(args, " \t\n\r", "\"\'");

    string jvmdll = RuntimeLibPath;
    
    if (FileUtils::fileExists(jvmdll) == false)
    {
        jvmdll = JavaHome +  "\\bin\\javai.dll";
        if (FileUtils::fileExists(jvmdll) == false)
        {
             DEBUG("JVM1.1: CAN'T FIND DLL !!!");
             return false;
        }
    }

    HINSTANCE vmlib = LoadLibrary(jvmdll.c_str());
      
    if (vmlib != 0)
    {
        DEBUG("VM LOADED!");
        CreateJavaVM_t CreateJavaVM = (CreateJavaVM_t)GetProcAddress(vmlib, "JNI_CreateJavaVM");
        GetDefaultJavaVMInitArgs_t GetDefaultJavaVMInitArgs = (GetDefaultJavaVMInitArgs_t)GetProcAddress(vmlib, "JNI_GetDefaultJavaVMInitArgs");
        
        if ((CreateJavaVM != NULL) && (GetDefaultJavaVMInitArgs != NULL))
        {
                DEBUG("CREATEJAVAVM FN LOADED");
                JavaVM *javavm = new JavaVM();
                JNIEnv *env = new JNIEnv();

                jint res;
                jclass cls;
                jmethodID mid;
                jstring jstr;
                jobjectArray args;
              
                  JDK1_1InitArgs vm_args;
                  vm_args.exit = myexit;
                  vm_args.version = 0x00010001;
                  GetDefaultJavaVMInitArgs(&vm_args);

     /* Append USER_CLASSPATH to the default system class path */

        std::string classpath = vm_args.classpath;
        classpath += ";" + jarpath;
        classpath += ";" + extracp;
        DEBUG("CLASSPATH = " + classpath);
        vm_args.classpath = (char*)classpath.c_str();

      /* Create the Java VM */

        res = CreateJavaVM( &javavm, &env, &vm_args);

       if (res < 0)
        {
             DEBUG("Can't create VM " + jvmdll);
             return false;
        }
        else
                            DEBUG("VM Created !!");
       
                jclass clstest = env->FindClass("java/lang/System");
                if (clstest != 0)
                {
                                DEBUG("FOUND java.lang.system !");
                }
                else
                {
                                DEBUG("java.lang.system NOT FOUND");
                                return false;
                }                
                
                cls = (env)->FindClass(classname.c_str());
                if (cls == 0)
                {
                                char tmpbuf[255];
                                sprintf(tmpbuf, "Cant find <%s> at all!", classname.c_str());
                                DEBUG(tmpbuf);
                                DEBUG(std::string("Can't Find CLASS <") + classname + std::string(">"));
                                return false;
                }
                else
                            DEBUG("CLASS FOUND");

                char strbuf[255];
                sprintf(strbuf, "");
                jstr = (env)->NewStringUTF(strbuf);
                mid = (env)->GetStaticMethodID(cls, "main", "([Ljava/lang/String;)V");

                if (pargs.size() > 0)
                {
                   args = (env)->NewObjectArray(pargs.size(), (env)->FindClass("java/lang/String"), jstr);
                   for (int i=0; i<pargs.size(); i++)
                   {
                       jstr = (env)->NewStringUTF(pargs[i].c_str());
                       (env)->SetObjectArrayElement(args, i, jstr);
                   }
                }
                else
                {
                    args = (env)->NewObjectArray(0, (env)->FindClass("java/lang/String"), jstr);
                }

                if ((mid != 0) && (args != 0))
                {
                                env->CallStaticVoidMethod(cls, mid, args);
                                DEBUG("VM CALLED !!");
                                
                                javavm->DestroyJavaVM();
                                return true;
                }
                else
                {
                                DEBUG("Can't find method !");
                                return false;
                }
        }
    }
    else
    {
        DEBUG("CAN'T LOAD DLL " + jvmdll);
    }
    return false;
}

bool SunJVMLauncher::runVM11proc(ResourceManager& resource, bool noConsole)
{
    string javapath = "\\bin\\java.exe";
    string jrepath = "\\bin\\jre.exe";
    if (noConsole)
    {
        javapath = "\\bin\\javaw.exe";
        jrepath = "\\bin\\jrew.exe";
    }

    if (runExe(JavaHome + jrepath, true, resource, noConsole))
        return true;

    if (runExe(JavaHome + javapath, true, resource, noConsole))
        return true;
        
    return false;    
}

bool SunJVMLauncher::runVM12proc(ResourceManager& resource, bool noConsole)
{
    string javapath = "\\bin\\java.exe";
    string jrepath = "\\bin\\jre.exe";
    if (noConsole)
    {
        javapath = "\\bin\\javaw.exe";
        jrepath = "\\bin\\jrew.exe";
    }

    if (runExe(JavaHome + javapath, false, resource, noConsole))
        return true;

    if (runExe(JavaHome + jrepath, false, resource, noConsole))
        return true;
        
    return false;
}

bool SunJVMLauncher::runExe(const string& exepath, bool forceFullClasspath, ResourceManager& resource, bool noConsole)
{    
   if (FileUtils::fileExists(exepath))
   {
      DEBUG("Running new proc for " + exepath);

      string classpath = resource.saveJarInTempFile();

      if (forceFullClasspath && (JavaHome != ""))
      {
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
      
      string addcp = resource.getProperty("classpath");
      classpath += ";" + addcp;
      
      string addargs = resource.getProperty(ResourceManager::KEY_ARGUMENTS);

      string javaproperties = "";
      const vector<JavaProperty>& jprops = resource.getJavaProperties();
      for (vector<JavaProperty>::const_iterator i=jprops.begin(); i != jprops.end(); i++)
      {
            JavaProperty jp = *i;
            javaproperties += " \"-D" + jp.getName() + "=" + jp.getValue() + "\"";
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
//            info.wShowWindow = SW_HIDE;
            creationFlags = NORMAL_PRIORITY_CLASS | DETACHED_PROCESS;
            inheritsHandle = FALSE;
      }

      PROCESS_INFORMATION procinfo;
      string exeline = exepath + " " + arguments;

      int res = CreateProcess(NULL, (char*)exeline.c_str(), NULL, NULL, inheritsHandle, creationFlags, NULL, NULL, &info, &procinfo);

      DEBUG("COMMAND LINE: " +exeline);
      DEBUG("RESULT: " + StringUtils::toString(res));
      if (res != 0)
      {
            WaitForSingleObject(procinfo.hProcess, INFINITE);
            return true;
      }
      else
      {
            DEBUG("Can't run " + exeline);
      }
   }

   return false;
}

Version SunJVMLauncher::guessVersionByProcess(const string& exepath)
{
    Version result;

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
      }
    DeleteFile(tmpfilename.c_str());
    return result;
}

