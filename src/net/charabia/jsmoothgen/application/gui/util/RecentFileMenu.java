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


import javax.swing.*;
import java.util.prefs.*;
/**
 *
 * @author  'Rodrigo Reyes"
 */
public class RecentFileMenu
{
	private JMenu m_root;
	private String[] m_recent;
	private Class m_prefAttach;
	private RecentFileMenu.Action m_action;
	
	public interface Action
	{
		public void action(String path);
	}
	
	/** Creates a new instance of RecentFileMenu */
	public RecentFileMenu(JMenu menuroot, int recentCount, Class prefAttach, RecentFileMenu.Action action)
	{
		m_recent = new String[recentCount];
		m_root = menuroot;
		m_prefAttach = prefAttach;
		m_action = action;
		
		loadRecentPrefs();
	}
	
	private void loadRecentPrefs()
	{
		Preferences p = Preferences.systemNodeForPackage(m_prefAttach);
		int count = p.getInt("recentfilecount", 0);
		
		int offset = 0;
		for (int i=0; i<count; i++)
		{
			String rf = p.get("recentfile_" + i, null);
			if ((rf != null) && (offset<m_recent.length))
			{
				m_recent[offset++] = rf;
			}
		}
		
		buildMenu();
	}
	
	public void savePrefs()
	{
		Preferences p = Preferences.systemNodeForPackage(m_prefAttach);
		int offset = 0;
		for (int i=0; i<m_recent.length; i++)
		{
			if (m_recent[i] != null)
			{
				p.put("recentfile_" + offset, m_recent[i]);
				offset ++;
			}
		}
		p.putInt("recentfilecount", offset);
	}

	public class ActionRecent implements java.awt.event.ActionListener
	{
		public int Offset;

		public void actionPerformed(java.awt.event.ActionEvent evt)
		{
			if (m_recent[Offset]!=null)
				RecentFileMenu.this.m_action.action(m_recent[Offset]);
		}
	}
	
	private void buildMenu()
	{
		m_root.removeAll();
		for (int i=0; i<m_recent.length; i++)
		{
			if (m_recent[i] != null)
			{
				JMenuItem item = new JMenuItem(m_recent[i]);
				ActionRecent ar = new ActionRecent();
				ar.Offset = i;
				item.addActionListener(ar);
				m_root.add(item);
			}
		}
		m_root.addSeparator();
		JMenuItem clear = new JMenuItem("Clear");
		clear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				for (int i=0; i<m_recent.length; i++)
				{
					m_recent[i] = null;
				}
				buildMenu();
			}
		});
		m_root.add(clear);
	}
	
	public void add(String rec)
	{
		for (int i=m_recent.length-1; i>0; i--)
		{
			m_recent[i] = m_recent[i-1];
		}
		m_recent[0] = rec;
		buildMenu();
	}
}
