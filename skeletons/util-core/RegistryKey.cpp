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

#include "RegistryKey.h"
#include "StringUtils.h"

RegistryKey::RegistryKey(KEYBASE parent, const std::string& subkey)
{
  switch(parent)
    {
    case CURRENT_USER:
      init(HKEY_CURRENT_USER, subkey);
      break;
    case LOCAL_MACHINE:
      init(HKEY_LOCAL_MACHINE, subkey);
      break;
    case USERS:
      init(HKEY_USERS, subkey);
      break;
    }
}

RegistryKey::RegistryKey(const RegistryKey& parent, const std::string& subkey)
{
  init(parent.m_key, subkey);
}

RegistryKey::~RegistryKey()
{
  if (m_key != 0)
    RegCloseKey(m_key);
}
  
void RegistryKey::init(HKEY key, const std::string& subkey)
{
  DWORD lpdwDisposition;
  if (RegCreateKeyEx(key, subkey.c_str(), 0, NULL, REG_OPTION_NON_VOLATILE, KEY_ALL_ACCESS, NULL, &m_key, &lpdwDisposition) != ERROR_SUCCESS)
    m_key = 0;
}

void RegistryKey::set(const std::string& name, const std::string& value)
{
  if (m_key != 0)
    RegSetValueEx(m_key, name.c_str(), 0, REG_SZ, (const BYTE*) value.c_str(), value.size()+1);
}

void RegistryKey::set(const std::string& name, DWORD value)
{
  if (m_key != 0)
    RegSetValueEx(m_key, name.c_str(), 0, REG_DWORD, (const BYTE*)&value, 4);
}

std::string RegistryKey::getString(const std::string& name)
{
  DWORD lpType, size = 1024;
  byte buffer[1024];
  byte*data = buffer;

  LONG res = RegQueryValueEx(m_key, name.c_str(), NULL, &lpType, data, &size);
  if (res == ERROR_SUCCESS)
    {
      switch(lpType)
	{
	case REG_DWORD:
	  return StringUtils::toString(* ( (int*)data ));
	  break;

	case REG_SZ:
	  buffer[size] = 0;
	  return std::string((char*)data);
	}
    }

  return "";
}

DWORD RegistryKey::getInt(const std::string& name)
{
  DWORD lpType, size = 1024;
  byte buffer[1024];
  byte*data = buffer;

  LONG res = RegQueryValueEx(m_key, name.c_str(), NULL, &lpType, data, &size);
  if (res == ERROR_SUCCESS)
    {
      switch(lpType)
	{
	case REG_DWORD:
	  return * ( (int*)data );
	  break;

	case REG_SZ:
	  buffer[size] = 0;
	  return StringUtils::parseInt((char*)data);
	}
    }

  return 0;
}

