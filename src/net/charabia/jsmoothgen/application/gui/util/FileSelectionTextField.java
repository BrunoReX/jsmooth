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
import java.io.*;

public class FileSelectionTextField extends javax.swing.JPanel
{
	
	private File m_basedir =  null;
    private java.util.Vector m_listeners = new java.util.Vector();
	
    public interface FileSelected
    {
	public void fileSelected(String filename);
    }

	/** Creates new form BeanForm */
	public FileSelectionTextField()
	{
		initComponents();
	}

    public void addListener(FileSelected fs)
    {
	m_listeners.add(fs);
    }

    public void removeListener(FileSelected fs)
    {
	m_listeners.remove(fs);
    }
	
    public void notifyListeners(String filename)
    {
	for (int i=0; i<m_listeners.size(); i++)
	    {
		FileSelected fs = (FileSelected)m_listeners.get(i);
		fs.fileSelected(filename);
	    }
    }

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents()//GEN-BEGIN:initComponents
	{
		m_fileChooser = new javax.swing.JFileChooser();
		m_filename = new javax.swing.JTextField();
		m_buttonFileSelection = new javax.swing.JButton();
		
		
		setLayout(new java.awt.BorderLayout());
		
		add(m_filename, java.awt.BorderLayout.CENTER);
		
		m_buttonFileSelection.setText("...");
		m_buttonFileSelection.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				buttonFileSelectionActionPerformed(evt);
			}
		});
		
		add(m_buttonFileSelection, java.awt.BorderLayout.EAST);
		
	}//GEN-END:initComponents

	private void buttonFileSelectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_buttonFileSelectionActionPerformed
	{//GEN-HEADEREND:event_buttonFileSelectionActionPerformed
		// Add your handling code here:
		java.io.File cur = new java.io.File(m_filename.getText());
		if ((cur.isAbsolute() == false) && (m_basedir != null))
		{
			cur = new File(m_basedir, cur.toString()).getAbsoluteFile();
			try {
				cur = cur.getCanonicalFile();
			} catch (IOException iox)
			{
				iox.printStackTrace();
			}
		}
		m_fileChooser.setSelectedFile(cur);
		if (m_fileChooser.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION)
		{
			java.io.File f = m_fileChooser.getSelectedFile();
			if (m_basedir != null)
			{
				File rel = net.charabia.jsmoothgen.application.JSmoothModelPersistency.makePathRelativeIfPossible(m_basedir, f);
				m_filename.setText(rel.toString());
				notifyListeners(rel.toString());
			}
			else
			{
				m_filename.setText(f.getAbsolutePath());
				notifyListeners(f.getAbsolutePath());
			}
		}
	}//GEN-LAST:event_buttonFileSelectionActionPerformed
	
	public void setFile(java.io.File f)
	{
		if (f == null)
		{
			m_filename.setText("");
		}
		else if (m_basedir != null)
		{
			File rel = net.charabia.jsmoothgen.application.JSmoothModelPersistency.makePathRelativeIfPossible(m_basedir, f);
			m_filename.setText(rel.toString());
		}
		else
		{
			m_filename.setText(f.getAbsolutePath());
		}
	}
	
	public java.io.File getFile()
	{
		if (m_filename.getText().trim().length() == 0)
			return null;
		return new java.io.File(m_filename.getText());
	}
	
	public void setFileChooser(JFileChooser jfc)
	{
		m_fileChooser = jfc;
	}
	
	public void setBaseDir(File root)
	{
		m_basedir = root;
	}
	
	public File getBaseDir()
	{
		return m_basedir;
	}
	
	public void setEnabled(boolean b)
	{
		System.out.println("set enabled to " + b);
		m_buttonFileSelection.setEnabled(b);
		m_filename.setEnabled(b);
	}
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton m_buttonFileSelection;
	private javax.swing.JFileChooser m_fileChooser;
	private javax.swing.JTextField m_filename;
	// End of variables declaration//GEN-END:variables
	
}
