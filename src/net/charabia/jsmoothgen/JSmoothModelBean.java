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

/*
 * JSmoothModelBean.java
 *
 * Created on 7 août 2003, 18:32
 */

package net.charabia.jsmoothgen;

import java.util.*;

public class JSmoothModelBean
{
	private String m_executableName = "";
	private String m_iconLocation = "";
	private String m_jarLocation = "";
	private String m_mainClassName = "";
	private String[] m_arguments;
	private String[] m_classPath;
	
	transient Vector m_listeners = new Vector();

	public static interface Listener
	{
		public void dataChanged();
	}
	
	/** Creates a new instance of JSmoothModelBean */
	public JSmoothModelBean()
	{
	}
	
	public void addListener(JSmoothModelBean.Listener l)
	{
		m_listeners.add(l);
	}
	
	public void removeListener(JSmoothModelBean.Listener l)
	{
		m_listeners.remove(l);
	}
	
	private void fireChanged()
	{
		for (Iterator i=m_listeners.iterator(); i.hasNext(); )
		{
			JSmoothModelBean.Listener l = (JSmoothModelBean.Listener)i.next();
			l.dataChanged();
		}
	}
	
	public void setExecutableName(String name)
	{
		fireChanged();
		m_executableName = name;
	}
		
	public String getExecutableName()
	{
		return m_executableName;
	}

	public void setIconLocation(String name)
	{
		fireChanged();
		m_iconLocation = name;
	}

	public String getIconLocation()
	{
		return m_iconLocation;
	}

	public void setJarLocation(String name)
	{
		fireChanged();
		m_jarLocation = name;
	}

	public String getJarLocation()
	{
		return m_jarLocation;
	}


	public void setMainClassName(String name)
	{
		fireChanged();
		m_mainClassName = name;
	}

	public String getMainClassName()
	{
		return m_mainClassName;
	}

	public void setArguments(String[]args)
	{
		fireChanged();
		m_arguments = args;
	}
	
	public String[] getArguments()
	{
		return m_arguments;
	}

	public void setClassPath(String[] cp)
	{
		fireChanged();
		m_classPath = cp;
	}
	
	public String[] getClassPath()
	{
		return m_classPath;
	}

}
