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

#include "ResourceManager.h"

char * const ResourceManager::KEY_MAINCLASSNAME = "mainclassname";
char * const ResourceManager::KEY_ARGUMENTS     = "arguments";
char * const ResourceManager::KEY_CLASSPATH     = "classpath";
char * const ResourceManager::KEY_JVMSEARCH     = "jvmsearch";
char * const ResourceManager::KEY_MINVERSION    = "minversion";
char * const ResourceManager::KEY_MAXVERSION    = "maxversion";
char * const ResourceManager::KEY_NOJVMMESSAGE  = "nojvmmessage";
char * const ResourceManager::KEY_NOJVMURL      = "nojvmurl";
char * const ResourceManager::KEY_BUNDLEDVM     = "bundledvm";
char * const ResourceManager::KEY_CURRENTDIR    = "currentdir";
char * const ResourceManager::KEY_EMBEDJAR      = "embedjar";

ResourceManager::ResourceManager(std::string category, int propsId, int jarId)
{
  m_resourceCategory = category;
  m_resourcePropsId = propsId;
  m_resourceJarId = jarId;

  //
  // Load the Properties
  //
  std::string propsidstr = this->idToResourceName(propsId);
  HRSRC resprop = FindResource(NULL, propsidstr.c_str(), category.c_str());
  if (resprop != NULL)
    {
      int mainsize = 0;
      mainsize = SizeofResource(NULL, resprop);
      // char mainbuf[mainsize+1];

      HGLOBAL main = LoadResource(NULL, resprop);
      m_props.setData((const char*)main, mainsize);
        
    }
  else
    {
      m_lastError = "Can't find resource 'main name'";
      return;
    }
    
  //
  // loads the jar information
  // 
  std::string jaridstr = this->idToResourceName(jarId);
  HRSRC resjar = FindResource(NULL, jaridstr.c_str(), category.c_str());
  if (resjar != NULL)
    {
      m_jarSize = SizeofResource(NULL, resjar);
      m_jarHandler =  LoadResource(NULL, resjar);
    }
  else
    {
      m_lastError = "Can't find JAR resource!";
      return;
    }

  //
  // Extract the java properties from the Property
  //
  std::string jpropcountstr = m_props.get("javapropertiescount");
    
  string exepath = FileUtils::getExecutablePath();
  string exename = FileUtils::getExecutableFileName();
  string computername = FileUtils::getComputerName();
    
  int jpropcount = StringUtils::parseInt(jpropcountstr);
  for (int i=0; i<jpropcount; i++)
    {
      string namekey = string("javaproperty_name_") + StringUtils::toString(i);
      string valuekey = string("javaproperty_value_") + StringUtils::toString(i);
    
      string name = m_props.get(namekey);
      string value = m_props.get(valuekey);

      DEBUG("JPROP: " + name + "=" + StringUtils::StringUtils::replaceEnvironmentVariable(value));
        
      value = StringUtils::replaceEnvironmentVariable(value);
      value = StringUtils::replace(value, "${EXECUTABLEPATH}", exepath);
      value = StringUtils::replace(value, "${EXECUTABLENAME}", exename);
      value = StringUtils::replace(value, "${COMPUTERNAME}", computername);
        
      JavaProperty jprop(name, value);
      m_javaProperties.push_back(jprop);
    }

    std::string curdirmodifier = m_props.get(ResourceManager::KEY_CURRENTDIR);
    if (curdirmodifier.length()>0)
        m_currentDirectory = FileUtils::concFile(FileUtils::getExecutablePath(), curdirmodifier);
    else
        m_currentDirectory = FileUtils::getExecutablePath();    
}

ResourceManager::~ResourceManager()
{
    for (std::vector<std::string>::iterator i=m_deleteOnFinalize.begin(); i != m_deleteOnFinalize.end(); i++)
    {
//        MessageBox(NULL, i->c_str(), "ERASING", MB_OK);
        int res = DeleteFile(i->c_str());
//        MessageBox(NULL, ("DONE " + StringUtils::toString(res) + " / " + StringUtils::toString(GetLastError())).c_str(), "ERASING", MB_OK);
    
//    LPVOID lpMsgBuf;
// 
//FormatMessage( 
//    FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM,
//    NULL,
//    GetLastError(),
//    MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), // Default language
//    (LPTSTR) &lpMsgBuf,
//    0,
//    NULL 
//);
//
//// Display the string.
//MessageBox( NULL, (const CHAR*)lpMsgBuf, "GetLastError", MB_OK|MB_ICONINFORMATION );
//
//// Free the buffer.
//LocalFree( lpMsgBuf );

    }
}
void ResourceManager::setProperty(const std::string& key, const std::string& value)
{
     m_props.set(key, value);
}

void ResourceManager::saveTemp(std::string tempname)
{
  HANDLE temp = CreateFile(tempname.c_str(),
			   GENERIC_WRITE,
			   FILE_SHARE_WRITE,
			   NULL, 
			   CREATE_ALWAYS, 
			   FILE_ATTRIBUTE_NORMAL,
			   NULL);
    
  if (temp != NULL)
    {    
      DWORD reallyWritten;
      WriteFile(temp, m_jarHandler, m_jarSize, &reallyWritten, NULL);
        
      // TODO: check the reallyWritten value for errors
        
      CloseHandle(temp);
      string s = tempname;
      m_deleteOnFinalize.push_back(s);
    }
    
}

std::string ResourceManager::getMainName()  const
{
  return getProperty(string("mainclassname"));
}

std::string ResourceManager::getProperty(const std::string& key)  const
{
  return m_props.get(key);
}

std::string ResourceManager::saveJarInTempFile()
{
  if (useEmbeddedJar() == false)
    return "";

  std::string tempfilename = FileUtils::createTempFileName(".jar");
  DEBUG("Created tempfilename " + tempfilename);
  saveTemp(tempfilename);
  return tempfilename;
}

const vector<JavaProperty>& ResourceManager::getJavaProperties()
{
  return m_javaProperties;
}

std::vector<std::string> ResourceManager::getNormalizedClassPathVector() const
{
  std::string basepath = FileUtils::getExecutablePath();
  std::string curdirmodifier = getProperty(string(ResourceManager::KEY_CURRENTDIR));
  basepath = FileUtils::concFile(basepath, curdirmodifier);

  DEBUG("basepath for classpath = " + basepath);

  std::string cp = getProperty(string(ResourceManager::KEY_CLASSPATH));
  vector<string>cps = StringUtils::split(cp, ";", "", false);
  for (int i=0; i<cps.size(); i++)
    {
      string lib = cps[i];
      cps[i] = FileUtils::concFile(basepath, cps[i]);
      DEBUG("FOUND " + basepath + " :: " + lib + " -> " + cps[i]);
    }

  return cps;
}

std::string ResourceManager::getNormalizedClassPath() const
{
  vector<string> cps = getNormalizedClassPathVector();
  return StringUtils::join(cps, ";");
}

std::string ResourceManager::getCurrentDirectory() const
{
    return m_currentDirectory;
}

bool ResourceManager::useEmbeddedJar() const
{
  std::string value = m_props.get(ResourceManager::KEY_EMBEDJAR);
  if (value == "true")
    return true;
  return false;
}
