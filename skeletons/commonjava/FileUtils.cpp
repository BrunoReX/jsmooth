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

#include "FileUtils.h"

string FileUtils::createTempFileName(const string& suffix)
{
    char temppath[512];
    int tplen = GetTempPath(512, temppath);

    int counter = 0;
    string temp;
    do {
    
       temp = string(temppath) + "temp" + StringUtils::toString(counter) + suffix;
       counter ++;
       
      } while (GetFileAttributes(temp.c_str()) !=  0xFFFFFFFF);

      DEBUG("TEMPFILE: " + temp);
    
    return temp;
}


bool FileUtils::fileExists(const string& filename)
{
    return GetFileAttributes(filename.c_str()) != 0xFFFFFFFF;
}

vector<string> FileUtils::recursiveSearch(const string& path, const string& pattern)
{
    vector<string> result;
    
    WIN32_FIND_DATA data;
    string file = path + ((path[path.length()-1]=='\\')?"":"\\") + pattern;
    
    DEBUG("scanning " + file);
    
    HANDLE handle = FindFirstFile(file.c_str(), &data);
    if (handle != INVALID_HANDLE_VALUE)
    {
        DEBUG(string("Found first file ") + data.cFileName);
        result.push_back(path + ((path[path.length()-1]=='\\')?"":"\\") + data.cFileName);
        for ( ; FindNextFile(handle, &data) == TRUE ; )
        {
              DEBUG(string("ADDED ") + data.cFileName);
              result.push_back(path + ((path[path.length()-1]=='\\')?"":"\\") + data.cFileName);
        }    
    
        FindClose(handle);
    }

    handle = FindFirstFile((path + ((path[path.length()-1]=='\\')?"":"\\") + "*").c_str(), &data);
    if (handle == INVALID_HANDLE_VALUE)
        return result;

    do {
        string foundpath(data.cFileName);
        if ((foundpath != ".") && (foundpath != "..") && ((data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) != 0))
        {
            DEBUG(string("REC DIR FOUND ") + data.cFileName  + " (attr: " + StringUtils::toString(data.dwFileAttributes) + ") / " + StringUtils::toString(FILE_ATTRIBUTE_DIRECTORY));
            string npath = path + ((path[path.length()-1]=='\\')?"":"\\") + data.cFileName;
            vector<string> tres = FileUtils::recursiveSearch(npath, pattern);
            result.insert(result.end(), tres.begin(), tres.end());
        }
    } while (FindNextFile(handle, &data));
    FindClose(handle);
    
    return result;
}

std::string FileUtils::extractFileName(const std::string& filename)
{
    int start = filename.rfind("\\", filename.length()-1);
    if (start != filename.npos)
    {
        return filename.substr(start+1);
    }
    return filename;
}

std::string FileUtils::getExecutablePath()
{
    char buffer[512];
    GetModuleFileName(NULL, buffer, 512);
    string full = buffer;
    int pos = full.rfind('\\', full.npos);
    
    return full.substr(0, pos+1);
}

std::string FileUtils::getExecutableFileName()
{
    char buffer[512];
    GetModuleFileName(NULL, buffer, 512);
    string full = buffer;
    int pos = full.rfind('\\', full.npos);
    
    return full.substr(pos+1);
}

std::string FileUtils::getComputerName()
{
    char buffer[MAX_COMPUTERNAME_LENGTH + 1];
    DWORD size = MAX_COMPUTERNAME_LENGTH+1;
    GetComputerName(buffer, &size);
    return buffer;
}

std::string FileUtils::concFile(std::string path, std::string file)
{
  if (path.length() > 0)
    {
      if (path[path.length()-1] != '\\')
	path += '\\';
    }
  path += file;
  
  return path;
}
