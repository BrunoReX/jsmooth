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

#ifndef __REGISTRYKEY_H_
#define __REGISTRYKEY_H_

#include <string>
#include <vector>
#include <windows.h>

class RegistryKey
{
 private:
  HKEY m_key;

 public:
  enum KEYBASE { CURRENT_USER = 1,
	 LOCAL_MACHINE = 2,
	 USERS = 3
  } ;

  RegistryKey(KEYBASE parent, const std::string& subkey);
  RegistryKey(const RegistryKey& parent, const std::string& subkey);
  ~RegistryKey();

  void set(const std::string& name, const std::string& value);
  void set(const std::string& name, DWORD value);

  std::string getString(const std::string& name);
  DWORD getInt(const std::string& name);
  
 private:
  void init(HKEY key, const std::string& subkey);

};

#endif
