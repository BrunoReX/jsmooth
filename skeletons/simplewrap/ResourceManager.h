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

#ifndef RESOURCEMANAGER_H
#define RESOURCEMANAGER_H

#include <windows.h>
#include <string>
#include <vector>

class ResourceManager
{
private:
    std::string m_mainName;
    std::string m_resourceCategory;
    int m_resourceMainId;
    int m_resourceJarId;
    std::string m_lastError;    
    HGLOBAL m_jarHandler;
    int m_jarSize;
public:

    ResourceManager(std::string category, int mainNameId, int jarId);
    void saveTemp(std::string tempname);
    std::string getMainName();
    std::string getLastErrorString()
    {
        return m_lastError;
    }
    
    std::string idToResourceName(int id)
    {
        char buffer[32];
        sprintf(buffer, "%d", id);
        std::string result("#");
        result += buffer;
        return result;
    }
    
};


#endif

