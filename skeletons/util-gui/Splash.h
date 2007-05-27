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

#ifndef __SPLASH_H_
#define __SPLASH_H_
#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Tile.H>
#include <FL/Fl_Progress.H>
#include <FL/Fl_Pack.H>
#include <FL/Fl_Box.H>
#include <FL/Fl_Group.H>
#include <FL/Fl_Shared_Image.H>
#include <FL/fl_draw.H>
#include <FL/x.H>

#include <string>
#include "Thread.h"

/**
 * Manages versions as used by Sun's JVM. The version scheme used is
 * major.minor.sub, for instance 1.1.8 or 1.4.2.
 *
 * @author Rodrigo Reyes <reyes@charabia.net>
 */

class SplashContent : public Fl_Group
{
 private:
  Fl_Shared_Image* m_splashImage;
  Fl_Progress*     m_progressBar;
  std::string      m_label;

 public:
  SplashContent(int x, int y, int w, int h, const char *label = 0, Fl_Shared_Image* image=0) : Fl_Group(x,y,w,h,label)
    {
      m_splashImage = image;
      buildGui();
    }

  void buildGui();
  void setProgress(int cur, int max);
  void setLabel(const std::string& label);

  virtual void draw();
};

class SplashWindow : public Fl_Double_Window
{
 private:
  SplashContent*   m_content;
  Fl_Shared_Image* m_splashImage;
  Thread*          m_closer;
  DWORD            m_watchedProcessId;

  static SplashWindow* s_singleton;

 public:
  SplashWindow(const std::string& splashfile);

  // Warning: NOT THREAD-SAFE
  static SplashWindow* getSingleton();

  void setProgress(int cur, int max);
  void setLabel(const std::string& label);

  void splashCheck(HWND tocheck);
  void splashOn();
  void splashOff();

  void setWatchedProcessId(DWORD pid);
  DWORD getWatchedProcessId();
};


#endif
