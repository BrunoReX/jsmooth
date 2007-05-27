/*
  JSmooth: a VM wrapper toolkit for Windows
  Copyright (C) 2003-2007 Rodrigo Reyes <reyes@charabia.net>
 
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

package net.charabia.jsmoothgen.application.gui.skeleditors;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;
import java.awt.event.*;
import net.charabia.jsmoothgen.application.gui.util.*;
import java.io.*;

public class ImageSelectorEditor extends SkelPropEditor
{
    protected FileSelectionTextField m_comp = new FileSelectionTextField();

    public ImageSelectorEditor()
    {
	GenericFileFilter gff = new GenericFileFilter("Image");
	gff.addSuffix("gif");
	gff.addSuffix("png");
	gff.addSuffix("jpg");
	gff.addSuffix("bmp");
	m_comp.setFileFilter(gff);
	
    }

    public java.awt.Component getGUI()
    {
	return m_comp;
    }

    public void valueChanged(String val)
    {
	m_comp.setFile(new File(val));
    }

    public boolean labelAtLeft()
    {
	return true;
    }

    public void set(String o) { m_comp.setFile(new File(o.toString())); }

    public String get() { if (m_comp.getFile()!=null) return m_comp.getFile().toString();  return "";}

    public void setBaseDir(java.io.File base)
    {
	m_comp.setBaseDir(base);
    }

    public boolean isLocalFile()
    {
	return true;
    }


}
