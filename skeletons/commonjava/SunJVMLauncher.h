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

#ifndef __SUNJVMLAUNCHER_H_
#define __SUNJVMLAUNCHER_H_

#include <string>
#include <jni.h>


#include "Version.h"
#include "StringUtils.h"
#include "FileUtils.h"
#include "ResourceManager.h"
#include "JavaProperty.h"

typedef jint (JNICALL *CreateJavaVM_t)(JavaVM **pvm, JNIEnv **env, void *args);
typedef jint (JNICALL *GetDefaultJavaVMInitArgs_t)(void *args);

class SunJVMLauncher
{
    public:
        std::string RuntimeLibPath;
        std::string JavaHome;
        Version VmVersion;
               
        virtual bool run(ResourceManager& resource);
        virtual bool runProc(ResourceManager& resource, bool noConsole);

       std::string toString() const;

       Version guessVersionByProcess(const string& exepath);

     private:
     
       bool runVM12DLL(ResourceManager& resource);
       bool runVM11DLL(ResourceManager& resource);
       bool runVM11proc(ResourceManager& resource, bool noConsole);
       bool runVM12proc(ResourceManager& resource, bool noConsole);

       bool runExe(const string& exepath, bool forceFullClasspath, ResourceManager& resource, bool noConsole, const std::string& version);
       
       std::string sizeToString(int size);
};

#endif
