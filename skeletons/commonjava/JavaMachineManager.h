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

/** Manages the JVM available on the computer.  It builds the list of
 * all the JVM available on the computer, and provide a method to run
 * the application according to the ResourceManager object passed to
 * the constructor.
 *
 * @author Rodrigo Reyes <reyes@charabia.net>
 */

class JavaMachineManager
{
  vector<SunJVMLauncher> m_registryVms;
  vector<SunJVMLauncher> m_javahomeVm;
  vector<SunJVMLauncher> m_jrepathVm;
  vector<SunJVMLauncher> m_jdkpathVm;
    
  bool                   m_localVMenabled;
  SunJVMLauncher         m_localVM;
    
  MSJViewLauncher        m_jviewVm;
    
  ResourceManager& m_resman;

 public:
  /**
   * This constructor builds a JavaMachineManager with a ResourceManager.
   *
   * @param resman a ResourceManager object
   * @see ResourceManager
   */ 
  JavaMachineManager(ResourceManager& resman) ;


  /**
   * Start the Java application.  The java application started is
   * described by the ResourceManager passed to the constructor.
   *
   * @param noConsole if true, the application started is not attached to the console.
   * @return true if the application is successfully started, false otherwise.
   */
  bool run(bool noConsole);
};

#endif
