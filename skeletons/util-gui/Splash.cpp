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

#include "Splash.h"
#include <stdio.h>
#include "StringUtils.h"

void SplashContent::buildGui()
{
  m_progressBar = new Fl_Progress(x()+10,h()-20,w()-20,10);
  //  redraw();
  m_progressBar->hide();
}

void SplashContent::setProgress(int cur, int max)
{
  Fl::lock();
  if (!m_progressBar->visible())
    m_progressBar->show();
  m_progressBar->maximum((float)max);
  m_progressBar->value((float)cur);
  m_progressBar->redraw();
  Fl::awake();
  Fl::unlock();
}

void SplashContent::setLabel(const std::string& label)
{
  Fl::lock();
  m_label = label;
  if (m_label != "")
    {
      m_progressBar->position(x()+10, h()-32);
      m_progressBar->size(w()-20,22);
    }
  else
    {
      m_progressBar->position(x()+10, h()-20);
      m_progressBar->size(w()-20,10);
    }
  m_progressBar->label(m_label.c_str());
  Fl::awake();
  Fl::unlock();
}

void SplashContent::draw()
{
  fl_color(FL_BLACK); 
  fl_rectf(0,0,w(),h());

  if (m_splashImage != 0)
    { 
      m_splashImage->draw(0,0);
    }

  Fl_Widget *const*a = array();
   // now draw all the children atop the background:
    for (int i = children(); i --; a ++) {
      draw_child(**a);
      draw_outside_label(**a); // you may not need to do this
    }
}


SplashWindow::SplashWindow(const std::string& splashfile)  : Fl_Double_Window(0,0)
{
  if (s_singleton == 0)
    s_singleton = this;

  m_closer = new Thread();
  m_watchedProcessId = 0;

  m_splashImage = Fl_Shared_Image::get(splashfile.c_str());

  int w = 200, h = 200;

  if (m_splashImage != NULL)
    {
      w = m_splashImage->w();
      h = m_splashImage->h();

      printf("Splash window dimension: %dx%d\n", w,h); fflush(stdout);
    }

  size(w,h);
  border(0);
  
  position(Fl::w() / 2 - w/2,
		  Fl::h() / 2 - h/2);

  m_content = new SplashContent(0,0, w, h, "splash", m_splashImage);

  end();
}


BOOL CALLBACK thread_closer_enumerator(HWND hwnd, LPARAM param) 
{
  SplashWindow* splash = (SplashWindow*)param;
  DWORD hpid = 0;
  DWORD curpid = splash->getWatchedProcessId(); // GetCurrentProcessId();
  GetWindowThreadProcessId(hwnd, &hpid);
  if (hpid == curpid)
    {
      if (GetWindowLong(hwnd, GWL_STYLE) & WS_VISIBLE)
	{
	  splash->splashCheck(hwnd);
	}
    }
  return TRUE;
}


void thread_closer(void* param)
{
  SplashWindow* splash = (SplashWindow*)param;
  //  int cur = 0;
  while (1)
    {
      Sleep(500);
      printf("Watching splash process %d\n", splash->getWatchedProcessId());
      fflush(stdout);

      EnumWindows(thread_closer_enumerator, (LPARAM)splash);
      if (splash->visible() == 0)
	{
	  return;
	}
    }
}

void SplashWindow::splashCheck(HWND tocheck)
{
  if (tocheck != fl_xid(this))
    {
      splashOff();
    }
}

void SplashWindow::splashOn()
{
  show();
  m_closer->start(thread_closer, this);
}

void SplashWindow::splashOff()
{
  Fl::lock();
  hide();
  Fl::unlock();
}

void SplashWindow::setProgress(int cur, int max)
{
  Fl::lock();
  m_content->setProgress(cur, max);
  //  Fl::redraw();
  //  Fl::awake();
  Fl::unlock();
}

void SplashWindow::setLabel(const std::string& label)
{
  m_content->setLabel(label);
}

void SplashWindow::setWatchedProcessId(DWORD pid)
{
  m_watchedProcessId = pid;
}

DWORD SplashWindow::getWatchedProcessId()
{
  return m_watchedProcessId;
}

SplashWindow* SplashWindow::getSingleton()
{
  return s_singleton;
}

SplashWindow* SplashWindow::s_singleton = 0;
