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

#ifndef RESOURCEMANAGER_H
#define RESOURCEMANAGER_H

#include <windows.h>
#include <string>
#include <vector>

#include "common.h"
#include "Properties.h"
#include "FileUtils.h"
#include "JavaProperty.h"

/**
 * Manages the executable resources associated to a Java
 * application. This class manages the resources that are used to
 * store the data associated to a java application. Those resources
 * are: 
 * - The JAR file, stored as a raw resource.  
 * - The Property file, stored as a raw resource.
 *  
 * The Property file contains an 8-bit text, as parsed and used by the
 * Properties class, which defines information relative to the java
 * application (for instance the name of the main class, the java
 * properties, and so on).
 *
 * @author Rodrigo Reyes <reyes@charabia.net>
 */

class ResourceManager
{
 private:
  std::string m_mainName;
  std::string m_resourceCategory;
  Properties m_props;
  std::string m_currentDirectory;

  int m_resourcePropsId;
  int m_resourceJarId;
  std::string m_lastError;    
  HGLOBAL m_jarHandler;
  int m_jarSize;
    
  std::vector<std::string> m_deleteOnFinalize;
  std::vector<JavaProperty> m_javaProperties;
    
 public:

  static char * const KEY_MAINCLASSNAME = "mainclassname";
  static char * const KEY_ARGUMENTS     = "arguments";
  static char * const KEY_CLASSPATH     = "classpath";
  static char * const KEY_JVMSEARCH     = "jvmsearch";
  static char * const KEY_MINVERSION    = "minversion";
  static char * const KEY_MAXVERSION    = "maxversion";
  static char * const KEY_NOJVMMESSAGE  = "nojvmmessage";
  static char * const KEY_NOJVMURL      = "nojvmurl";
  static char * const KEY_BUNDLEDVM     = "bundledvm";
  static char * const KEY_CURRENTDIR    = "currentdir";

  /** 
   * Constructs a ResourceManager which extract the jar and property
   * files from the resources defined by the given parameters.  The
   * resource are loaded from the resource type and resource names
   * passed as parameters.
   *
   * @param category the resource type to look in
   * @param propsId the resource id, stored under the category type, for the property file
   * @param jarId the resource id, stored under the category type, for the jar file
   */ 
  ResourceManager(std::string category, int propsId, int jarId);

  /**
   * Default destructor.  The detructor tries to delete all the
   * temporary files that have been created by the object. This is
   * mainly the files created by the saveJarInTempFile() method.
   *
   * @sa ResourceManager::saveJarInTempFile
   */
  ~ResourceManager();
    
  /** Saves the jar in a temporary folder.  Extract the jar file
   * from the resource defined in the consructor, and saves it in
   * the temporary directory defined by the operating system.
   *
   * @return the filename of the temp file where the Jar can be found.
   */
  std::string saveJarInTempFile();

  /** Returns the name of the main class.  The main class is the
   *  class used to launch the java application. The static "public
   *  void main(String[])" method of this class is called to start
   *  the program.
   *
   * @return the name of the main class
   */ 
  std::string getMainName() const;

  /**
   * Returns the last error string. This is the string that describes
   * the last error that was raised by an operation on the object.
   * @return a string
   */
  std::string getLastErrorString()
    {
      return m_lastError;
    }
    
  /**
   * Retrieves a property value from the Properties resource defined
   * in the constructor.
   *
   * @param key the name of the property
   * @return a string that contains the value of the property, or an empty string if the property does not exist.
   */
  std::string getProperty(const std::string& key) const;

  /**
   * Adds a new property.
   *
   * @param key the name of the property
   * @param value the value associated to the property
   */
  void setProperty(const std::string& key, const std::string& value);

  std::vector<std::string> getNormalizedClassPathVector() const;
  std::string getNormalizedClassPath() const;

  /**
   * Return the list of JavaProperty elements defined in the property
   * resource. 
   *
   * @return a vector of JavaProperty elements, or an empty vector if none are defined.
   */ 
  const vector<JavaProperty>& getJavaProperties();
    
  std::string getCurrentDirectory() const;
  
 private:
  void saveTemp(std::string tempname);

  std::string idToResourceName(int id) const
    {
      char buffer[32];
      sprintf(buffer, "%d", id);
      std::string result("#");
      result += buffer;
      return result;
    }
    
};


#endif

