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
#include "resource.h"

#include "common.h"
#include "ResourceManager.h"
#include "JVMRegistryLookup.h"
#include "JavaMachineManager.h"

using namespace std;

int main(int argc, char *argv[])
{
    ResourceManager* globalResMan = new ResourceManager("JAVA", PROPID, JARID);
    DEBUG(string("Main class: ") + globalResMan->getMainName());

    char curdir[256];
    GetCurrentDirectory(256, curdir);
    DEBUG(string("Currentdir: ") + curdir);

    string newcurdir = globalResMan->getProperty(ResourceManager::KEY_CURRENTDIR);
    SetCurrentDirectory(newcurdir.c_str());

    JavaMachineManager man(*globalResMan);
    if (man.run(false) == false)
    {
        std::string errmsg = globalResMan->getProperty("skel_Message");
        cerr << errmsg.c_str();
        cerr << "\r\n";
    }  

  system("PAUSE");	
  return 0;
}
