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

class JavasoftVM
{
    public:
    std::string Path;
    std::string Version;
};

class JavasoftRuntimeList
{
private:
    std::vector<JavasoftVM> m_jvms;

public:
    std::string Message;
    
  JavasoftRuntimeList();
  
  int getCount()
  {
    return m_jvms.size();
  }
  
  const JavasoftVM& getAt(int i)
  {
    return m_jvms.at(i);
  }

  void run();
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
