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

/**
 * Manages a Sun's JVM available on the computer.
 * @author Rodrigo Reyes <reyes@charabia.net>
 */ 

class SunJVMLauncher
{
 public:
  /**
   * The path to the .DLL that can be used to create a JVM
   * instance. The string may be empty is the information is not
   * known.
   */ 
  std::string RuntimeLibPath;

 protected:
  JavaVM *m_javavm;
  JNIEnv *m_javaenv;
  
 public:
  /**
   * The path to a directory where a JRE is installed. It is expected
   * that the executable bin\\java.exe, bin\\javaw.exe, bin\\jre.exe,
   * or bin\\jrew.exe exist. The string may be empty if the
   * information is not known.
   */ 
  std::string JavaHome;

  /**
   * The version of the JVM targetted by this object. The object may
   * be invalid, if the information is not known.
   */
  Version VmVersion;
               
  /**
   * Launches the JVM target of this object. The object is run using
   * the RuntimeLibPath and trying to load the .dll.
   *
   * @param resource a ResourceManager instance that describes the
   * java program to launch.
   *
   * @return true if the java application was successfully launched,
   * false otherwise.
   */ 
  virtual bool run(ResourceManager& resource, const string& origin);


  /**
   * Runs the JVM as a process and launches the application. The
   * command line is created according to the parameters of the
   * application (defined in the ResourceManager object), and the
   * version of the JRE (guessed at runtime). The process is then
   * created with this command line.
   *
   * @param resource a ResourceManager instance that describes the
   * java program to launch.
   * @param noConsole if true, the process created is detached from
   * the current process, and no console i/o is inherited. Otherwise,
   * the process shares the i/o with the current process.
   *
   * @return true if the java application was successfully launched,
   * false otherwise.
   */ 
  virtual bool runProc(ResourceManager& resource, bool noConsole, const string& origin);

  std::string toString() const;

  Version guessVersionByProcess(const string& exepath);

  //  int operator<(const SunJVMLauncher& other) const;

friend bool operator < (const SunJVMLauncher& v1, const SunJVMLauncher& v2);

  bool runExe(const string& exepath, bool forceFullClasspath, ResourceManager& resource, bool noConsole, const std::string& version, const string& origin);

 private:
     
  //  bool runVM12DLL(ResourceManager& resource, const string& origin);
  //  bool runVM11DLL(ResourceManager& resource, const string& origin);
  bool runVMDLL(ResourceManager& resource, const string& origin);

  bool setupVM12DLL(ResourceManager& resource, const string& origin);
  bool setupVM11DLL(ResourceManager& resource, const string& origin);

  bool runVM11proc(ResourceManager& resource, bool noConsole, const string& origin);
  bool runVM12proc(ResourceManager& resource, bool noConsole, const string& origin);
       
  std::string sizeToString(int size);
  std::string sizeToString(std::string size);
};


#endif
