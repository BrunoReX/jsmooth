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

#include "ResourceManager.h"

ResourceManager::ResourceManager(std::string category, int mainNameId, int jarId)
{
    m_resourceCategory = category;
    m_resourceMainId = mainNameId;
    m_resourceJarId = jarId;

    std::string mainidstr = this->idToResourceName(mainNameId);
    HRSRC resmain = FindResource(NULL, mainidstr.c_str(), category.c_str());
    if (resmain != NULL)
    {
        m_mainName = "";
    
        int mainsize = 0;
        mainsize = SizeofResource(NULL, resmain);
        char mainbuf[mainsize+1];
        
        HGLOBAL main = LoadResource(NULL, resmain);
        char* mainptr = (char*) main;
        for (int i=0; i<mainsize; i++)
        {
                m_mainName += *(mainptr++);
        }
    }
    else
    {
        m_lastError = "Can't find resource 'main name'";
        return;
    }
    std::string jaridstr = this->idToResourceName(jarId);
    HRSRC resjar = FindResource(NULL, jaridstr.c_str(), category.c_str());
    if (resjar != NULL)
    {
        m_jarSize = SizeofResource(NULL, resjar);
        m_jarHandler =  LoadResource(NULL, resjar);
    }
    else
    {
        m_lastError = "Can't find JAR resource!";
        return;
    }
    
}

void ResourceManager::saveTemp(std::string tempname)
{
    HANDLE temp = CreateFile(tempname.c_str(),
                            GENERIC_WRITE,
                            FILE_SHARE_WRITE,
                            NULL, 
                            CREATE_ALWAYS, 
                            FILE_ATTRIBUTE_HIDDEN,
                            NULL);
    
    if (temp != NULL)
    {    
        DWORD reallyWritten;
        WriteFile(temp, m_jarHandler, m_jarSize, &reallyWritten, NULL);
        CloseHandle(temp);
    }
}

std::string ResourceManager::getMainName()
{
    return m_mainName;
}

