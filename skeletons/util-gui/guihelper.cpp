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

#include "guihelper.h"
#include <FL/Fl.H>
#include "Thread.h"
#include "Splash.h"

bool guihelper_running = false;

void guihelper_run_thread(void* param)
{
  if (guihelper_running == false)
    {
      guihelper_running = true;
      Fl::lock();
      if (SplashWindow::getSingleton() != 0)
	SplashWindow::getSingleton()->splashOn();
      Fl::run();
      Fl::unlock();
    }
}

void guihelper_run()
{
  Thread* guithread = new Thread();
  guithread->setAutoDelete(true);
  guithread->start(guihelper_run_thread, NULL);
}
