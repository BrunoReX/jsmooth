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

#include "StringUtils.h"

vector<string> StringUtils::split(const string& str, char separator)
{
    vector<string> result;
    string buf = "";

    for (int i=0; i<str.length(); i++)
    {
        if (separator == str[i])
        {
                if (buf.length() > 0)
                {
                                result.push_back(buf);
                                buf = "";
                }
        }
        else
        {
                buf += str[i];
        }
    }
    if (buf.length() > 0)
    {
        result.push_back(buf);
    }

    return result;
}


int StringUtils::parseInt(const string& val)
{
    return atoi(val.c_str());
}

string StringUtils::toString(int val)
{
    char buf[32];
    sprintf(buf, "%d", val);
    return string(buf);
}

string StringUtils::toString(const vector<string>& seq)
{
    string result = "[";
    for (vector<string>::const_iterator i=seq.begin(); i != seq.end(); i++)
    {
        result += *i;
        if ((i+1) != seq.end())
                result += ", ";
    }
    result += "]";
    return result;
}

void StringUtils::copyTo(const string& from, char* to, int length)
{
    int max = (from.length()+1>length)?length-1:from.length();
    for (int i=0; i<max; i++)
    {
        to[i] = from[i];
    }
    to[max] = 0;
}

string StringUtils::join(const vector<string>& seq, const string& separator)
{
    string result = "";
    for (vector<string>::const_iterator i=seq.begin(); i != seq.end(); i++)
    {
        result += *i;
        result += separator;
    }
    return result;
}
