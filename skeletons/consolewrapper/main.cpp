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

#include <iostream>
#include <stdlib.h>
#include <windows.h>

#include "resource.h"

#include "common.h"
#include "ResourceManager.h"
#include "JVMRegistryLookup.h"
#include "JavaMachineManager.h"

using namespace std;

bool enableDebug = false;

void _debugOutput(const std::string& text)
{
  if (enableDebug)
    std::cerr << text << "\r\n";
}

void _debugWaitKey()
{
}

int main(int argc, char *argv[])
{    
    ResourceManager* globalResMan = new ResourceManager("JAVA", PROPID, JARID);

    for (int i=0; i<argc; i++)
      {
	DEBUG("ARGUMENT[" + StringUtils::toString(i) + "]=" + std::string(argv[i]));
      }
    
    // sets up the arguments
    //
    if (argc > 1)
      globalResMan->setProperty(string(ResourceManager::KEY_ARGUMENTS), "");
    for (int i=1; i<argc; i++)
      globalResMan->addUserArgument(argv[i]);

    //
    // sets up the debug mode, if requested
    std::string dodebug = globalResMan->getProperty("skel_Debug");
    if (StringUtils::parseInt(dodebug) != 0)
      {
	enableDebug = true;
	globalResMan->printDebug();
      }

    char curdir[256];
    GetCurrentDirectory(256, curdir);

    string newcurdir = globalResMan->getCurrentDirectory();
    SetCurrentDirectory(newcurdir.c_str());

    JavaMachineManager man(*globalResMan);
    if (man.run(false, false) == false)
    {
        std::string errmsg = globalResMan->getProperty("skel_Message");
        cerr << errmsg.c_str();
        cerr << "\r\n";
    }

  int waitkey = atoi(globalResMan->getProperty("skel_PressKey").c_str());
  if (waitkey != 0)
  {
    system("PAUSE");
  }
  
  delete globalResMan;  
  return 0;
}

