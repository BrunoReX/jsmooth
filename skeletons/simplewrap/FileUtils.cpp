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
    
    DEBUG("looking for " + file);
    
    HANDLE handle = FindFirstFile(file.c_str(), &data);
    if (handle != INVALID_HANDLE_VALUE)
    {
        DEBUG(string("Found first file ") + data.cFileName);
        result.push_back(path + ((path[path.length()-1]=='\\')?"":"\\") + data.cFileName);
        for ( ; FindNextFile(handle, &data) == TRUE ; )
        {
              result.push_back(path + ((path[path.length()-1]=='\\')?"":"\\") + data.cFileName);
        }    
    
        FindClose(handle);
    }

    DEBUG("rec search in " + path);
    handle = FindFirstFile((path + ((path[path.length()-1]=='\\')?"":"\\") + "*").c_str(), &data);
    if (handle == INVALID_HANDLE_VALUE)
        return result;

    do {
        DEBUG(string("REC DIR FOUND ") + data.cFileName );
        string foundpath(data.cFileName);
        if ((foundpath != ".") && (foundpath != ".."))
        {
            string npath = path + ((path[path.length()-1]=='\\')?"":"\\") + data.cFileName;
            vector<string> tres = FileUtils::recursiveSearch(npath, pattern);
            result.insert(result.end(), tres.begin(), tres.end());
        }
    } while (FindNextFile(handle, &data));
    FindClose(handle);
    
    return result;
}
