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

#include <iostream>
#include <stdlib.h>

#include "StringUtils.h"
#include "guihelper.h"

#define DEBUG(x) _debugOutput(x)
#define DEBUGWAITKEY() _debugWaitKey()

void _debugOutput(const std::string& text)
{
  //  std::cerr << text << "\r\n";
  printf("%s\n", text.c_str());
  fflush(stdout);
}

void _debugWaitKey()
{
}


int main(int argc, char *argv[])
{
  guihelper_run();
  exit(0);

}
