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
import javax.swing.*;

public class EditableListFileEditor implements SortedEditableList.Editor
{
	private File m_rootDir;
	private JFileChooser m_fileChooser = new JFileChooser();
	
	/** Creates a new instance of EditableListFileEditor */
	public EditableListFileEditor()
	{
	}

	public void setFileChooser(JFileChooser chooser)
	{
		m_fileChooser = chooser;
	}
	
	public void setRootDir(File dir)
	{
		m_rootDir = dir;
		m_fileChooser.setCurrentDirectory(dir);
	}
	
	public File getRootDir()
	{
		return m_rootDir;
	}
	
	public Object createNewItem(SortedEditableList selist)
	{
		if (m_fileChooser.showOpenDialog(selist) == JFileChooser.APPROVE_OPTION)
		{
			return m_fileChooser.getSelectedFile();
		}
		return null;
	}
	
	public Object editItem(SortedEditableList selist, Object item)
	{
		m_fileChooser.setSelectedFile((File)item);
		if (m_fileChooser.showOpenDialog(selist) == JFileChooser.APPROVE_OPTION)
		{
			return m_fileChooser.getSelectedFile();
		}
		return null;
	}
	
	public boolean removeItem(SortedEditableList selist, Object item)
	{
		return true;
	}
	
}
