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

#include "MSJViewLauncher.h"

bool MSJViewLauncher::runProc(const ResourceManager& resource)
{
      DEBUG("Running JVIEW new process");
    
      string classpath = resource.saveJarInTempFile();

      string classname = resource.getProperty(string(ResourceManager::KEY_MAINCLASSNAME));
      string arguments = "/cp:p \"" + classpath + "\" " + classname;
      
      STARTUPINFO info;
      GetStartupInfo(&info);
      PROCESS_INFORMATION procinfo;
      string exeline = "jview.exe " + arguments;
      DEBUG("EXELINE: " + exeline);
      int res = CreateProcess(NULL, (char*)exeline.c_str(), NULL, NULL, TRUE, DETACHED_PROCESS, NULL, NULL, &info, &procinfo);

      DEBUG("JVIEW result = " + StringUtils::toString(res));

    
      if (res != 0)
            return true;

      DEBUG("ERROR JVIEW : " + StringUtils::toString(GetLastError()));

      return false;
}

