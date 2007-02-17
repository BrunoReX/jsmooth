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
  DEBUG("Now searching the JVM installed on the system...");

    m_registryVms = JVMRegistryLookup::lookupJVM();
    m_javahomeVm = JVMEnvVarLookup::lookupJVM("JAVA_HOME");
    m_jrepathVm = JVMEnvVarLookup::lookupJVM("JRE_HOME");
    m_jdkpathVm = JVMEnvVarLookup::lookupJVM("JDK_HOME");

    if (resman.getProperty("bundledvm").length() > 0)
    {
        string bjvm = resman.getProperty("bundledvm");
        DEBUG("Found a vm bundled with the application: (" + bjvm + ")");
        m_localVMenabled = true;
        m_localVM.JavaHome = FileUtils::concFile(resman.getCurrentDirectory(), bjvm);
    } else
    {
        m_localVMenabled = false;
    }
    DEBUG("Current directory is " + resman.getCurrentDirectory());
}

bool JavaMachineManager::run(bool dontUseConsole, bool preferSingleProcess)
{
  string vmorder = m_resman.getProperty(ResourceManager::KEY_JVMSEARCH);

  if (m_localVMenabled)
    {
        DEBUG("Trying to use bundled VM " + m_localVM.JavaHome);        
        if (m_localVM.runProc(m_resman, dontUseConsole, "bundled"))
	        return true;
        
        if (m_localVM.run(m_resman, "bundled"))
        	return true;
    }

  if (vmorder == "")
    {
      vmorder = "registry;jdkpath;jrepath;javahome;jview;exepath";
    }
    
  DEBUG("JSmooth will now try to use the VM in the following order: " + vmorder);
    
  vector<string> jvmorder = StringUtils::split(vmorder, ";,", "");

  Version max(m_resman.getProperty(ResourceManager:: KEY_MAXVERSION));
  Version min(m_resman.getProperty(ResourceManager:: KEY_MINVERSION));

  for (vector<string>::const_iterator i = jvmorder.begin(); i != jvmorder.end(); i++)
    {
      DEBUG("------------------------------");

      if (*i == "registry")
        {
	  DEBUG("Trying to use a JVM defined in the registry (" + StringUtils::toString(m_registryVms.size()) + " available)");
	  string vms = "VM will be tried in the following order: ";
	  for (int i=0; i<m_registryVms.size(); i++)
	    {
	      vms += m_registryVms[i].VmVersion.toString();
	      vms += ";";
	    }
	  DEBUG(vms);

	  for (int i=0; i<m_registryVms.size(); i++)
            {
	      DEBUG("- Trying registry: " + m_registryVms[i].toString());

	      if (dontUseConsole)
                {
		  //
		  // If we are here, then we prefer to launch the java
		  // application detached from any console. Typically
		  // for a Windows app.
		  //
                     
		  if (preferSingleProcess)
		    {
		      DEBUG("Trying to run the JVM as a DLL call (executing in this process)...");
		      if (m_registryVms[i].run(m_resman, "registry"))
                        {
			  return true;
                        }
		      DEBUG("Well, couldn't use a DLL (see traces above), so falling back into normal process creation mode");

		      if (m_registryVms[i].runProc(m_resman, dontUseConsole, "registry"))
			{
			  return true;
			}
		    }
		  else
		    {
		      if (m_registryVms[i].runProc(m_resman, dontUseConsole, "registry"))
			{
			  return true;
			} else if (m_registryVms[i].run(m_resman, "registry"))
                          {
			    return true;
                          }
                     
		    }
                          
                }
	      else
                {
		  DEBUG("DONT USE CONSOLE == FALSE");
		  if (m_registryVms[i].runProc(m_resman, dontUseConsole, "registry"))
		    {
		      return true;
		    } else if (m_registryVms[i].run(m_resman, "registry"))
		      {
                        return true;
		      }
                }
	      DEBUG("Couldn't use this VM, now trying something else");
            }
        } else if (*i == "jview")
	  {
	    DEBUG("- Trying to launch the application with JVIEW");
	    if (m_jviewVm.runProc(m_resman, dontUseConsole))
	      {
		return true;
	      }

	  } else if (*i == "javahome")
	    {
	      DEBUG("- Trying to use JAVAHOME");
	      if (m_javahomeVm.size()>0)
                {
		  DEBUG("JAVAHOME exists..." + m_javahomeVm[0].toString());
                                
		  if (m_javahomeVm[0].runProc(m_resman, dontUseConsole, "javahome"))
                    {
		      return true;
                    }
                }
	    } else if (*i == "jrepath")
	      {
                DEBUG("- Trying to use JRE_HOME");
                if (m_jrepathVm.size()>0)
		  {
                    if (m_jrepathVm[0].runProc(m_resman, dontUseConsole, "jrehome"))
		      {
                        return true;
		      }
		  }
	      } else if (*i == "jdkpath")
		{
		  DEBUG("- Trying to use JDK_HOME");
		  if (m_jdkpathVm.size()>0)
		    {
		      if (m_jdkpathVm[0].runProc(m_resman, dontUseConsole, "jdkhome"))
			{
			  return true;
			}
		    }
		} else if (*i == "exepath")
		  {
		    DEBUG("- Trying to use PATH");
		    string exename = dontUseConsole?"javaw.exe":"java.exe";
		    SunJVMLauncher launcher;
		    launcher.VmVersion = launcher.guessVersionByProcess(exename);

		    if (launcher.VmVersion.isValid()
			&& (!min.isValid() || (min <= launcher.VmVersion))
			&& (!max.isValid() || (launcher.VmVersion <= max)))
		      {
			DEBUG("Found valid java machine " + exename + " on PATH (" + launcher.VmVersion.toString() + ")");
			Version v12("1.2.0");
			if (launcher.runExe(exename, false, m_resman, dontUseConsole, (launcher.VmVersion<v12)?"1.1":"1.2", "path"))
			  return true;
		      }
		  }
    }

  DEBUG("Couldn't run any suitable JVM!");
  return false;
}
