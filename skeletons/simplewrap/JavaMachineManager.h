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

#ifndef __JAVAMACHINEMANAGER_H_
#define __JAVAMACHINEMANAGER_H_

#include <string>
#include <vector>

#include "common.h"
#include "StringUtils.h"
#include "SunJVMLauncher.h"
#include "JVMRegistryLookup.h"
#include "JVMEnvVarLookup.h"
#include "MSJViewLauncher.h"

class JavaMachineManager
{
    vector<SunJVMLauncher> m_registryVms;
    vector<SunJVMLauncher> m_javahomeVm;
    vector<SunJVMLauncher> m_jrepathVm;
    vector<SunJVMLauncher> m_jdkpathVm;
    
    bool                   m_localVMenabled;
    SunJVMLauncher         m_localVM;
    
    MSJViewLauncher        m_jviewVm;
    
    const ResourceManager& m_resman;

    public:
        JavaMachineManager(const ResourceManager& resman) ;

        bool run();
};

#endif
