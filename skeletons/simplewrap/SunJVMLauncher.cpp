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
      char buffer[2048];
//      sprintf(buffer, format, args);
      
//      DEBUG(buffer);
        DEBUG("MYPRINTF");
  }
  void JNICALL myexit(jint code)
  {
       DEBUG("EXIT CALLED");
        DEBUGWAITKEY();
    	exit(code);
  }
  
}

std::string SunJVMLauncher::toString() const
{
    return "<" + JavaHome + "><" + RuntimeLibPath + "><" + VmVersion.toString() + ">";
}

bool SunJVMLauncher::run(const ResourceManager& resource)
{
    Version max(resource.getProperty(ResourceManager:: KEY_MAXVERSION));
    Version min(resource.getProperty(ResourceManager:: KEY_MINVERSION));
    
    if ((VmVersion < min) || (max < VmVersion))
    {
        return false;
    }
    
    DEBUG("Launching " + toString());
    
    if (Version("1.2") <= VmVersion)
    {
        DEBUG("RUNNING L VM " + VmVersion.toString());
//        runVM12DLL(resource);
        return runVM12proc(resource);
    } else if (Version("1.1") <= VmVersion)
    {
            DEBUG("RUNNING L VM11 = " + VmVersion.toString());
                    return runVM11proc(resource);
//            runVM11DLL(resource);
    }
    
    return false;
}

bool SunJVMLauncher::runProc(const ResourceManager& resource)
{
    Version max(resource.getProperty(ResourceManager:: KEY_MAXVERSION));
    Version min(resource.getProperty(ResourceManager:: KEY_MINVERSION));
    
    DEBUG("RUN PROC... " + min.toString() + " / " + VmVersion.toString() + " / " + max.toString());
    
    
    if ( VmVersion.isValid() && ((VmVersion < min) || (max < VmVersion)))
    {
        return false;
    }

    DEBUG("RUN PROC... version OK");
    
    if (Version("1.2") <= VmVersion)
    {
        return runVM12proc(resource);
    } else if (Version("1.1") <= VmVersion)
    {
        return runVM11proc(resource);
    }
    
    return false;
}

bool SunJVMLauncher::runVM12DLL(const ResourceManager& resource)
{

    std::string jarpath = resource.saveJarInTempFile();
    std::string classname = resource.getProperty(string(ResourceManager::KEY_MAINCLASSNAME));

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
//                cpoption += "grostest.jar";
                DEBUG("Classpath: " + cpoption);
                options[0].optionString =  (char*)cpoption.c_str();
                options[1].optionString = "exit";
                options[1].extraInfo = (void*)myexit;
                
                vm_args.version = 0x00010002;
                vm_args.options = options;
                vm_args.nOptions = 2;
                
                DEBUG("OPTIONS SET!");
                
//                vm_args.ignoreUnrecognized = JNI_TRUE;
                vm_args.ignoreUnrecognized = JNI_FALSE;
                
                GetDefaultJavaVMInitArgs(&vm_args);

//                std::string classpathstr = vm_args.classpath;
//                classpathstr += ";gen-application.jar";
 //               copyString(classpathstr, classpath, 2047);
//                vm_args.classpath = classpath;                
//                this->Message = classpathstr;
//                 res = JNI_CreateJavaVM(&vm,(void**)&env,&vm_args);

              res = CreateJavaVM( &vm, &env, &vm_args);
                if (res < 0)
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
                args = (env)->NewObjectArray(0, (env)->FindClass("java/lang/String"), jstr);
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



bool SunJVMLauncher::runVM11DLL(const ResourceManager& resource)
{

    std::string jarpath = resource.saveJarInTempFile();
    std::string classname = resource.getProperty(string(ResourceManager::KEY_MAINCLASSNAME));

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
                args = (env)->NewObjectArray(0, (env)->FindClass("java/lang/String"), jstr);
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

bool SunJVMLauncher::runVM11proc(const ResourceManager& resource)
{
    if (runExe(JavaHome + "\\bin\\jre.exe", false, resource))
        return true;

    if (runExe(JavaHome + "\\bin\\java.exe", true, resource))
        return true;
        
    return false;    
}

bool SunJVMLauncher::runVM12proc(const ResourceManager& resource)
{
    if (runExe(JavaHome + "\\bin\\java.exe", false, resource))
        return true;

    if (runExe(JavaHome + "\\bin\\jre.exe", false, resource))
        return true;
        
    return false;
}

bool SunJVMLauncher::runExe(const string& exepath, bool forceFullClasspath, const ResourceManager& resource)
{
   if (FileUtils::fileExists(exepath))
   {
      DEBUG("Running new proc for " + exepath);

      string classpath = resource.saveJarInTempFile();
      if (forceFullClasspath && (JavaHome != ""))
      {
            vector<string> cpzips = FileUtils::recursiveSearch(JavaHome, "*.zip");
            vector<string> cpjars = FileUtils::recursiveSearch(JavaHome, "*.jar");
            vector<string> fullcp;
            fullcp.insert(fullcp.end(), cpzips.begin(), cpzips.end());
            string lcp = StringUtils::join(fullcp, ";");
            
            classpath += string(";") + lcp;
      }
      
      string classname = resource.getProperty(string(ResourceManager::KEY_MAINCLASSNAME));
      string arguments = "-cp \"" + classpath + "\" " + classname;
      
      STARTUPINFO info;
      GetStartupInfo(&info);
      PROCESS_INFORMATION procinfo;
      string exeline = exepath + " " + arguments;
//      int res = CreateProcess((char*)exepath.c_str(), (char*)arguments.c_str(), NULL, NULL, TRUE, DETACHED_PROCESS, NULL, NULL, &info, &procinfo);
      int res = CreateProcess(NULL, (char*)exeline.c_str(), NULL, NULL, TRUE, DETACHED_PROCESS, NULL, NULL, &info, &procinfo);

      if (res != 0)
            return true;
   }

   return false;
}

Version SunJVMLauncher::guessVersionByProcess()
{

}
