#include "JavasoftRuntimeList.h"

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
				    vm.Version = versionname;
				    m_jvms.push_back(vm);
                }
				    
			}
		}

	}

}


typedef jint (JNICALL *CreateJavaVM_t)(JavaVM **pvm, JNIEnv **env, void *args);
typedef jint (JNICALL *GetDefaultJavaVMInitArgs_t)(void *args);

void JavasoftRuntimeList::run()
{
    char buffer[512];
    std::string pathToVm = (*m_jvms.begin()).Path;
    copyString(pathToVm, buffer, sizeof(buffer)-1);
    HINSTANCE vmlib = LoadLibrary(buffer);
    if (vmlib != 0)
    {
        this->Message = "LOADED!";
        CreateJavaVM_t CreateJavaVM = (CreateJavaVM_t)GetProcAddress(vmlib, "JNI_CreateJavaVM");
        GetDefaultJavaVMInitArgs_t GetDefaultJavaVMInitArgs = (GetDefaultJavaVMInitArgs_t)GetProcAddress(vmlib, "JNI_GetDefaultJavaVMInitArgs");
        
        if ((CreateJavaVM != NULL) && (GetDefaultJavaVMInitArgs != NULL))
        {
                this->Message = "CREATEJAVAVM FN LOADED";
                JavaVM *vm = new JavaVM();
                JNIEnv *env = new JNIEnv();
//                JavaVMInitArgs vm_args;
//                JDK1_1InitArgs vm_args;
                jint res;
                jclass cls;
                jmethodID mid;
                jstring jstr;
                jobjectArray args;
                char classpath[2048];
                
                JavaVMInitArgs vm_args;
     JavaVMOption options[1];
     options[0].optionString =  "-Djava.class.path=gen-application.jar" ;
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
                                this->Message = "Can't create VM";
                else
                            this->Message = "VM Created !!";

                cls = (env)->FindClass("net.charabia.generation.application.Application");
                if (cls == 0)
                                this->Message = "Can't Find CLASS";
                else
                            this->Message = "CLASS FOUND";
                            
                            
                mid = (env)->GetStaticMethodID(cls, "main", "([Ljava/lang/String;)V");
                args = (env)->NewObjectArray(0, (env)->FindClass("java/lang/String"), jstr);
                env->CallStaticVoidMethod(cls, mid, args);
        }
    }
    else
    {
        this->Message = "CAN'T LOAD ";
        this->Message += buffer;
    }
    
}

