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

#include "common.h"
#include "Properties.h"
#include "FileUtils.h"

class ResourceManager
{
private:
    std::string m_mainName;
    std::string m_resourceCategory;
    Properties m_props;
    
    int m_resourcePropsId;
    int m_resourceJarId;
    std::string m_lastError;    
    HGLOBAL m_jarHandler;
    int m_jarSize;
    
    std::vector<std::string> m_deleteOnFinalize;
    
public:

    static char * const KEY_MAINCLASSNAME = "mainclassname";
    static char * const KEY_ARGUMENTS     = "arguments";
    static char * const KEY_CLASSPATH     = "classpath";
    static char * const KEY_JVMSEARCH     = "jvmsearch";
    static char * const KEY_MINVERSION    = "minversion";
    static char * const KEY_MAXVERSION    = "maxversion";
    static char * const KEY_NOJVMMESSAGE  = "nojvmmessage";
    static char * const KEY_NOJVMURL      = "nojvmurl";
    static char * const KEY_BUNDLEDVM     = "bundledvm";
    static char * const KEY_CURRENTDIR    = "currentdir";
 
    ResourceManager(std::string category, int propsId, int jarId);
    ~ResourceManager();
    
    std::string saveJarInTempFile();
    std::string getMainName() const;
    std::string getLastErrorString()
    {
        return m_lastError;
    }
    
    std::string getProperty(const std::string& key) const;
    
    std::string idToResourceName(int id) const
    {
        char buffer[32];
        sprintf(buffer, "%d", id);
        std::string result("#");
        result += buffer;
        return result;
    }
private:
    void saveTemp(std::string tempname);
    
};


#endif

