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

#include "splashhelper.h"
#include "Splash.h"
#include <FL/Fl_Shared_Image.H>
#include <FL/Fl.H>
#include "FileUtils.h"
#include "guihelper.h"

void splashwindow_start(const std::string& filename)
{
  if (FileUtils::fileExists(filename))
    {
      fl_register_images();
      printf("Creating splash with image %s\n",filename.c_str()); fflush(stdout);
      SplashWindow* sw = new SplashWindow(filename);
      //      SplashWindow::getSingleton()->splashOn();
      guihelper_run();
    }
  
}

bool splashwindow_isVisible()
{
  SplashWindow* sp = SplashWindow::getSingleton();
  if (sp != 0)
    return sp->visible();
}

void  splashwindow_hide()
{
  SplashWindow* sp = SplashWindow::getSingleton();
  if (sp != 0)
    sp->hide();
}

void  splashwindow_setProgress(int cur, int max)
{
  SplashWindow* sp = SplashWindow::getSingleton();
  if (sp != 0)
    sp->setProgress(cur, max);
}

void  splashwindow_setProcessId(DWORD pid)
{
  SplashWindow* sp = SplashWindow::getSingleton();
  if (sp != 0)
    sp->setWatchedProcessId(pid);  
}
