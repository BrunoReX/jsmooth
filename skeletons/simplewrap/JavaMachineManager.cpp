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

#include "JavaMachineManager.h"

JavaMachineManager::JavaMachineManager(const ResourceManager& resman): m_resman(resman)
{
    m_registryVms = JVMRegistryLookup::lookupJVM();
    m_javahomeVm = JVMEnvVarLookup::lookupJVM("JAVA_HOME");
    m_jrepathVm = JVMEnvVarLookup::lookupJVM("JRE_PATH");
    m_jdkpathVm = JVMEnvVarLookup::lookupJVM("JDK_PATH");
}

bool JavaMachineManager::run()
{
    DEBUG("RUNNING!");
    for (int i=0; i<m_registryVms.size(); i++)
    {
        DEBUG("trying: " + m_registryVms[i].toString());
        if (m_registryVms[i].run(m_resman))
        {
                return true;
        }
    }
    return false;
}
