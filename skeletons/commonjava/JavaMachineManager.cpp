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

JavaMachineManager::JavaMachineManager(ResourceManager& resman): m_resman(resman)
{
    m_registryVms = JVMRegistryLookup::lookupJVM();
    m_javahomeVm = JVMEnvVarLookup::lookupJVM("JAVA_HOME");
    m_jrepathVm = JVMEnvVarLookup::lookupJVM("JRE_PATH");
    m_jdkpathVm = JVMEnvVarLookup::lookupJVM("JDK_PATH");
    if (resman.getProperty("localvmdir").length() > 0)
    {
        m_localVMenabled = true;
        m_localVM.JavaHome = resman.getProperty("localvmdir");
    } else
    {
        m_localVMenabled = false;
    }
}

bool JavaMachineManager::run(bool dontUseConsole, bool preferSingleProcess)
{
    string vmorder = m_resman.getProperty(ResourceManager::KEY_JVMSEARCH);

    if (m_localVMenabled)
    {
        if (m_localVM.run(m_resman))
                return true;
        
        if (m_localVM.runProc(m_resman, dontUseConsole))
                return true;
    }

    if (vmorder == "")
    {
        vmorder = "registry;jdkpath;jrepath;javahome;jview;exepath";
    }
    
    DEBUG("VMORDER == " + vmorder);
    
    vector<string> jvmorder = StringUtils::split(vmorder, ";,", "");

    for (vector<string>::const_iterator i = jvmorder.begin(); i != jvmorder.end(); i++)
    {
        if (*i == "registry")
        {
            DEBUG("Lookup " + *i + " :: " + StringUtils::toString(m_registryVms.size()));
            for (int i=0; i<m_registryVms.size(); i++)
            {
                DEBUG("trying registry: " + m_registryVms[i].toString());

                if (dontUseConsole)
                {
                     //
                     // If we are here, then we prefer to launch the java
                     // application detached from any console. Typically
                     // for a Windows app.
                     //
                     
                     if (preferSingleProcess)
                     {
                        if (m_registryVms[i].run(m_resman))
                        {
                           return true;
                        } else if (m_registryVms[i].runProc(m_resman, dontUseConsole))
                        {
                           return true;
                        }
                     }
                     else
                     {
                          DEBUG("DONT USE CONSOLE == TRUE");
                          if (m_registryVms[i].runProc(m_resman, dontUseConsole))
                          {
                              return true;
                          } else if (m_registryVms[i].run(m_resman))
                          {
                              return true;
                          }
                     
                     }
                          
                }
                else
                {
                     DEBUG("DONT USE CONSOLE == FALSE");
                     if (m_registryVms[i].runProc(m_resman, dontUseConsole))
                     {
                        return true;
                     } else if (m_registryVms[i].run(m_resman))
                     {
                        return true;
                     }
                }
            }
        } else if (*i == "jview")
        {
                DEBUG("trying JVIEW");
                if (m_jviewVm.runProc(m_resman, dontUseConsole))
                {
                    return true;
                }

        } else if (*i == "javahome")
        {
                DEBUG("trying JAVAHOME");
                if (m_javahomeVm.size()>0)
                {
                DEBUG("JAVAHOME exists..." + m_javahomeVm[0].toString());
                                
                    if (m_javahomeVm[0].runProc(m_resman, dontUseConsole))
                    {
                        return true;
                    }
                }
                
            for (int i=0; i<m_registryVms.size(); i++)
            {
                DEBUG("trying registry PROC: " + m_registryVms[i].toString());
                if (m_registryVms[i].runProc(m_resman, dontUseConsole))
                {
                        return true;
                }
            }                
        } else if (*i == "jrepath")
        {
                DEBUG("trying JREPATH");
                if (m_jrepathVm.size()>0)
                {
                    if (m_jrepathVm[0].runProc(m_resman, dontUseConsole))
                    {
                        return true;
                    }
                }
        } else if (*i == "jdkpath")
        {
                DEBUG("trying JDKPATH");
                if (m_jdkpathVm.size()>0)
                {
                    if (m_jdkpathVm[0].runProc(m_resman, dontUseConsole))
                    {
                        return true;
                    }
                }
        }
    }

    return false;
}
