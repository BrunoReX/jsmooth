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

package net.charabia.jsmoothgen.application.gui.util;

import java.io.*;

public class ClassPathFileFilter extends javax.swing.filechooser.FileFilter
{
	
	/** Creates a new instance of ClassPathFileFilter */
	public ClassPathFileFilter()
	{
	}
	
	public boolean accept(java.io.File f)
	{
		String suffix = getSuffix(f);
		if (suffix.equalsIgnoreCase(".jar"))
			return true;
		if (suffix.equalsIgnoreCase(".zip"))
			return true;
		if (f.isDirectory())
			return true;
		
		return false;
	}
	
	public String getDescription()
	{
		return ".jar, .zip, or directories";
	}
	
	private String getSuffix(File f)
	{
		String fstr = f.getAbsolutePath();
		int lastDot = fstr.lastIndexOf('.');
		if (lastDot >= 0)
		{
			return fstr.substring(lastDot);
		}
		return "";
	}
	
}
