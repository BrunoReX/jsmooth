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

#include <iostream>
#include <stdlib.h>
#include <windows.h>

#include "resource.h"

#include "common.h"
#include "ResourceManager.h"
#include "JVMRegistryLookup.h"
#include "JavaMachineManager.h"

using namespace std;

int main(int argc, char *argv[])
{    
    ResourceManager* globalResMan = new ResourceManager("JAVA", PROPID, JARID);
    char curdir[256];
    GetCurrentDirectory(256, curdir);

    {
        char tmp[256];
        char *fn;
        GetFullPathName("..\\test", 256, tmp, &fn);
        cerr << "fullpathname " << tmp << "\n";
        cerr << "filename " << fn << "\n";
        
        std::string exepath = FileUtils::getExecutablePath();
        std::string p = FileUtils::concFile(exepath, "..\\test");
        
        cerr << "test : " << p << "\n";
    }

    string newcurdir = globalResMan->getProperty(ResourceManager::KEY_CURRENTDIR);
    SetCurrentDirectory(newcurdir.c_str());

    std::string args = "";
    for (int i=1; i<argc; i++)
    {
        args += string("\"") + argv[i] + "\" ";
    }

    globalResMan->setProperty(string(ResourceManager::KEY_ARGUMENTS), args);
    
    JavaMachineManager man(*globalResMan);
    if (man.run(false, false) == false)
    {
        std::string errmsg = globalResMan->getProperty("skel_Message");
        cerr << errmsg.c_str();
        cerr << "\r\n";
    }

  int waitkey = atoi(globalResMan->getProperty("skel_KeyPress").c_str());
  if (waitkey != 0)
  {
    system("PAUSE");
  }
  
  delete globalResMan;  
  return 0;
}

