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

#ifndef __PROPERTIES_H_
#define __PROPERTIES_H_

#include <string>
#include <map>
#include "common.h"
#include "StringUtils.h"

using namespace std;

class Properties
{
    map<string, string> m_data;

    public:
        Properties(const char *data, int datalength);
        Properties();
        
        void setData(const char *data, int datalength);
        string getNextToken(const char* data, int datalen, int& cursor, char stopchar);
        
        string get(const string& key) const;
        void set(const string& key, const string& value);
        
    private:
    
        std::string unescape(const string& val);
};

#endif
