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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import com.l2fprod.common.swing.*;

/**
 * Represents a JTextField associated with a button that pops up a
 * file dialog selector.
 *
 */

public class HTMLPane extends JPanel
{
    private JScrollPane m_scroller;
    private JEditorPane m_html;

    public HTMLPane()
    {
	m_html = new JEditorPane("text/html","<html></html>");
	m_scroller = new JScrollPane(m_html);
	setLayout(new BorderLayout());
	m_html.setEditable(false);
	add(BorderLayout.CENTER, m_scroller);
    }

    public void setText(String s)
    {
	m_html.setText(s);
	m_html.setCaretPosition(0);
    }

}
