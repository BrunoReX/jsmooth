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

	      if (internalRun(m_registryVms[i], dontUseConsole, preferSingleProcess, "registry") == true)
		return true;
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
		  if (internalRun(m_javahomeVm[0], dontUseConsole, preferSingleProcess, "jrehome"))
		    return true;
                }
	    } else if (*i == "jrepath")
	      {
                DEBUG("- Trying to use JRE_HOME");
                if (m_jrepathVm.size()>0)
		  {
		    if (internalRun(m_jrepathVm[0], dontUseConsole, preferSingleProcess, "jrehome"))
		      return true;
		    }
		} else if (*i == "exepath")
		  {
		    DEBUG("- Trying to use PATH");

		    SunJVMLauncher launcher;
		    return launcher.runProc(m_resman, ! dontUseConsole, "path");
		    
// 		    string exename = dontUseConsole?"javaw.exe":"java.exe";
// 		    SunJVMLauncher launcher;
// 		    launcher.VmVersion = launcher.guessVersionByProcess("java.exe");

// 		    if (launcher.VmVersion.isValid()
// 			&& (!min.isValid() || (min <= launcher.VmVersion))
// 			&& (!max.isValid() || (launcher.VmVersion <= max)))
// 		      {
// 			DEBUG("Found valid java machine " + exename + " on PATH (" + launcher.VmVersion.toString() + ")");
// 			Version v12("1.2.0");
// 			if (launcher.runExe(exename, false, m_resman, dontUseConsole, (launcher.VmVersion<v12)?"1.1":"1.2", "path"))
// 			  return true;
// 		      }
		  }
    }

  DEBUG("Couldn't run any suitable JVM!");
  return false;
}


bool JavaMachineManager::internalRun(SunJVMLauncher& launcher, bool noConsole, bool preferSingleProcess, const string& org)
{
  // Need a console, and spawning a process is OK ? Then exec
  // [java/jre].exe.
  if ((noConsole == false) && (preferSingleProcess==false))
    {
      return launcher.runProc(m_resman, noConsole, org);
    }

  // No need for a console, and need a single process ? Use the DLL
  // and create a JVM with it
  if (noConsole && preferSingleProcess)
    {
      DEBUG("Trying to run the JVM as a DLL call (executing in this process)...");
      if (launcher.run(m_resman, org) == true)
	return true;

      DEBUG("Couldn't use the DLL at " + launcher.RuntimeLibPath);      
    }

  // If we have an embedded jar, always prefer the exec process
  // launching, as it garantees that the temporary jar is cleaned up
  // on exit (due to a bug in the JVM DLL that prevents it to
  // terminate nicely).
  if (m_resman.useEmbeddedJar())
    {
      if (launcher.runProc(m_resman, noConsole, org) == false)
	return launcher.run(m_resman, org);
    }
  else // Otherwise, preferring the JVM DLL call first is better
       // (it spares a new process)
    {
      if (launcher.run(m_resman, org) == false)
	return launcher.runProc(m_resman, noConsole, org);
    }
  
  return true;
}



SunJVMLauncher* JavaMachineManager::runDLLFromRegistry(bool justInstanciate)
{
  string vms = "DLL VM will be tried in the following order: ";
  for (int i=0; i<m_registryVms.size(); i++)
    {
      vms += m_registryVms[i].VmVersion.toString();
      vms += ";";
    }
  DEBUG(vms);

  for (int i=0; i<m_registryVms.size(); i++)
    {
      DEBUG("- Trying registry: " + m_registryVms[i].toString());

      bool res = m_registryVms[i].run(m_resman, "registry", justInstanciate);
      
      if (res)
	return &m_registryVms[i];
    }

  return NULL;
}
