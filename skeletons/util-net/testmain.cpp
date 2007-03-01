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

#include <winsock2.h>

#include "StringUtils.h"
#include "URL.h"
#include "HttpClient.h"

string url = "http://jsmooth.sf.net:9091/test1/test2&ici#123";

#include <FL/Fl.H>

#include <FL/Fl_Window.H>
#include <FL/Fl_Box.H>

#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Tile.H>
#include <FL/Fl_Progress.H>
#include <FL/Fl_Browser.H>

#include "downloadgui.h"

#include <windows.h>
#include <process.h>

#include "httpdownload.h"



int main(int argc, char *argv[])
{
  printf("Starting...\n");

  //httpDownload("http://ftpclubic1.clubic.com/temp-clubic-rx270/logiciel/exstora_exstora_1.2_francais_22607.exe");
  //  string file = httpDownload("http://java.sun.com/update/1.5.0/jinstall-1_5_0-windows-i586.cab");

  //  string file = httpDownload("http://localhost/jinstall-1_4_2-windows-i586.cab");
  string file = httpDownload("http://java.sun.com/products/plugin/autodl/jinstall-1_3_0_05-win.cab");

  printf("Downloaded: %s\n", file.c_str());
  exit(0);

}

//int receiveData(char* buffer, int maxbuffersize,
