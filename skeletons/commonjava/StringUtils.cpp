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

vector<string> StringUtils::split(const string& str, const string& separators, const string& quotechars)
{
    vector<string> result;
    string buf = "";

    for (int i=0; i<str.length(); i++)
    {
        if (str[i] == '\\')
        {
                i++;
                if (i<str.length())
                {
                    switch(str[i])
                    {
                       case '\'':
                       case '\"':
                          buf += str[i];
                          break;
                       
                       case 'n':
                          buf += '\n';
                          break;
                       case 'r':
                          buf += '\r';
                          break;
                       case 't':
                          buf += '\t';
                          break;
                    }
                }
        }
        else if (separators.find(str[i], 0) != separators.npos)
        {
                if (buf.length() > 0)
                {
                                result.push_back(buf);
                                buf = "";
                }
        }
        else if (quotechars.find(str[i], 0) != separators.npos)
        {
                if (buf.length() > 0)
                {
                      result.push_back(buf);
                      buf = "";
                }
                        
                char qc = quotechars[ quotechars.find(str[i], 0) ];
                i++;
                while ( (i<str.length()) && (str[i] != qc) )
                {
                    buf += str[i++];
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

string StringUtils::trim(string& str)
{
    string result = "";
    int start = str.length();
    int end = 0;
    
    for (int i=0; i<str.length(); i++)
    {
        switch(str[i])
        {
                case ' ':
                case '\n':
                case '\t':
                case '\r':
                                
                   break;
                   
                default:
                   start = i;
                   i = str.length();
                   break;
        }
    }
    
    for (int i=str.length()-1; i>start; i--)
    {
        switch(str[i])
        {
                case ' ':
                case '\n':
                case '\t':
                case '\r':
                                
                   break;
                   
                default:
                   end = i;
                   i = -1;
                   break;
        }
    }   
    
    result = str.substr(start, end-start+1);    
    return result;
}

string StringUtils::replaceEnvironmentVariable(const string& str)
{
    string result = str;
    int start = 0;

    while ( (start=result.find('%', start)) != str.npos)
    {
        start++;
        int end = result.find('%', start);
        if (end != str.npos)
        {
                int replacelen = end - start;
                string envname = result.substr(start, replacelen);
                
                char buffer[512];
                buffer[0]=0;
                
                GetEnvironmentVariable(envname.c_str(), buffer, 512);
                result.replace(start-1, replacelen+2, buffer);
                start+= strlen(buffer);                
        }
        else
                start = end;
    }
    
    return result;
}

