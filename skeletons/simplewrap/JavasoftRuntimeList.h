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

#include <windows.h>
#include <string>
#include <vector>
#include <jni.h>

#include "common.h"
#include "Version.h"
#include <functional>

//
// This class manages a list of all the Sun's VMs registered in the Windows' 
// Registry.
//
//
class JavasoftVM
{
    public:
    std::string Path;
    Version VmVersion;    
    friend bool operator < (JavasoftVM&  v1, JavasoftVM& v2);
};

bool operator<(const JavasoftVM& jvm1, const JavasoftVM& jvm2);
bool operator<(JavasoftVM& jvm1, JavasoftVM& jvm2);

class JavasoftRuntimeList
{
private:
    std::vector<JavasoftVM> m_jvms;

public:
    std::string Message;
    JavasoftVM INVALID;
    
  JavasoftRuntimeList();
  
  int getCount()
  {
    return m_jvms.size();
  }
  
  const JavasoftVM& getAt(int i)
  {
    return m_jvms.at(i);
  }

  const JavasoftVM& findVersionOrHigher(Version v);

  void run(const JavasoftVM& vm, std::string jarpath, std::string classname);

  void runVM12(const JavasoftVM& vm, const std::string& jarpath, const std::string& classname);
//  void run2();

  void copyString(const std::string& str, char* buffer, int max)
  {
       int size = str.size();
       if (size+2 > max)
              size = max-2;
	   str.copy(buffer, size);
	   buffer[size] = 0;
  }

};
