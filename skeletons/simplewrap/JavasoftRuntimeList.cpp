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

#include "JavasoftRuntimeList.h"

bool operator<(const JavasoftVM& jvm1, const JavasoftVM& jvm2)
{
     return jvm1.VmVersion < jvm2.VmVersion;
}

bool operator<(JavasoftVM& jvm1, JavasoftVM& jvm2)
{
     return jvm1.VmVersion < jvm2.VmVersion;
}

JavasoftRuntimeList::JavasoftRuntimeList()
{
    HKEY hKey;
	LONG error = ERROR_SUCCESS;
	LONG val = RegOpenKeyEx(HKEY_LOCAL_MACHINE, TEXT("SOFTWARE\\JavaSoft\\Java Runtime Environment"), 0,
             KEY_READ, &hKey);

	unsigned long buffersize = 255;
	char buffer[255];

	for (int i=0; RegEnumKey(hKey ,i, buffer, buffersize) == ERROR_SUCCESS; i++)
	{
		int v = i;
		HKEY version;
		int foundver = RegOpenKeyEx(hKey, buffer, 0, KEY_READ, &version);
		if (foundver == ERROR_SUCCESS)
		{
		    std::string versionname(buffer);
		
			HKEY runtimelib;
			unsigned long datatype;
			unsigned char *b = (unsigned char*)buffer;
			int foundlib = RegQueryValueEx(version, TEXT("RuntimeLib"), 
								NULL, 
								&datatype, 
								b, 
								&buffersize);
								
								
			if (foundlib == ERROR_SUCCESS)
			{
				char *rt = buffer;
				std::string s(buffer);
				bool found = false;
				for (std::vector<JavasoftVM>::iterator i=m_jvms.begin(); i != m_jvms.end(); i++)
				{
				    if ((*i).Path == s)
				            found = true;
				}
				if (found == false)
				{
				    JavasoftVM vm;
				    vm.Path = s;
				    vm.VmVersion = Version(versionname);
				    m_jvms.push_back(vm);
				    
				    DEBUG(std::string("Found new VM: ") + vm.Path + " : " + vm.VmVersion.Value);
				    char buffer[244];
				    sprintf(buffer, "V(%d)(%d)(%d)", vm.VmVersion.getMajor(), vm.VmVersion.getMinor(), vm.VmVersion.getSubMinor());
				    DEBUG(std::string("ANALYZED AS ") + buffer);
                }
				    
			}
		}

	}

}

const JavasoftVM& JavasoftRuntimeList::findVersionOrHigher(Version v)
{
   std::sort(m_jvms.begin(),m_jvms.end());  
   return *m_jvms.begin();
   
    for (int i=0; i<m_jvms.size(); i++)
    {
        const JavasoftVM& vm = m_jvms[i];
        if (v < vm.VmVersion)
        {
                return vm;
        }
    }
    return INVALID;
}

void JavasoftRuntimeList::run(const JavasoftVM& vm, std::string jarpath, std::string classname)
{
    if ((vm.VmVersion.getMajor() == 1) && (vm.VmVersion.getMinor() >= 2))
    {
        DEBUG(std::string("Found 1.2 VM!"));
        runVM12(vm, jarpath, classname);
    }
}

typedef jint (JNICALL *CreateJavaVM_t)(JavaVM **pvm, JNIEnv **env, void *args);
typedef jint (JNICALL *GetDefaultJavaVMInitArgs_t)(void *args);

void JavasoftRuntimeList::runVM12(const JavasoftVM& vm, const std::string& jarpath, const std::string& classname)
{
//    char buffer[512];
    HINSTANCE vmlib = LoadLibrary(vm.Path.c_str());
    if (vmlib != 0)
    {
        DEBUG("VM LOADED!");
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
                JavaVMOption options[1];
                std::string cpoption = "-Djava.class.path=";
                cpoption += jarpath;
                options[0].optionString =  (char*)cpoption.c_str();
                vm_args.version = 0x00010002;
                vm_args.options = options;
                vm_args.nOptions = 1;
                vm_args.ignoreUnrecognized = JNI_TRUE;
                
                GetDefaultJavaVMInitArgs(&vm_args);

//                std::string classpathstr = vm_args.classpath;
//                classpathstr += ";gen-application.jar";
 //               copyString(classpathstr, classpath, 2047);
//                vm_args.classpath = classpath;                
//                this->Message = classpathstr;
//                 res = JNI_CreateJavaVM(&vm,(void**)&env,&vm_args);

              res = CreateJavaVM( &vm, &env, &vm_args);
                if (res < 0)
                                DEBUG("Can't create VM");
                else
                            DEBUG("VM Created !!");

                cls = (env)->FindClass(classname.c_str());
                if (cls == 0)
                                DEBUG("Can't Find CLASS");
                else
                            DEBUG("CLASS FOUND");

                mid = (env)->GetStaticMethodID(cls, "main", "([Ljava/lang/String;)V");
                args = (env)->NewObjectArray(0, (env)->FindClass("java/lang/String"), jstr);
                env->CallStaticVoidMethod(cls, mid, args);
                DEBUG("VM CALLED !!");
        }
    }
    else
    {
        DEBUG("CAN'T LOAD DLL");
    }
    
}

