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

#ifndef __FILEUTILS_H_
#define __FILEUTILS_H_

#include <windows.h>
#include <string>
#include <vector>

#include "common.h"
#include "StringUtils.h"

using namespace std;

class FileUtils
{
public:
    static string createTempFileName(const string& suffix);
    static bool fileExists(const string& filename);    
    static vector<string> recursiveSearch(const string& path, const string& pattern);
    static std::string FileUtils::extractFileName(const std::string& filename);
};

#endif


