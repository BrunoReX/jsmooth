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

package net.charabia.jsmoothgen.application.gui;

import net.charabia.jsmoothgen.application.*;
import net.charabia.jsmoothgen.application.gui.util.*;
import javax.swing.*;
import java.io.File;
import java.lang.reflect.*;

public class Executable extends javax.swing.JPanel implements ModelUpdater
{
    private JSmoothModelBean m_model;
    private String m_iconLocation;
    java.io.File m_basedir;
	
    /** Creates new form BeanForm */
    public Executable()
    {
	initComponents();
	JFileChooser fc = new JFileChooser();
	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	m_currentDirectory.setFileChooser(fc);
                
	JFileChooser exefc = new JFileChooser();
	exefc.setFileFilter(new SimpleFileFilter("exe", "Executable"));
	m_executableNameField.setFileChooser(exefc);
    }
	
    public void setModel(java.io.File basedir, JSmoothModelBean model)
    {
	m_basedir = basedir;
	m_model = model;
	m_executableNameField.setBaseDir(basedir);
	if (m_model.getExecutableName() != null)
	    {
		java.io.File exefile = new java.io.File(m_model.getExecutableName());
		m_executableNameField.setFile(exefile);
		m_currentDirectory.setBaseDir(exefile.getParentFile());
	    }

	if (m_model.getCurrentDirectory() != null)
	    m_currentDirectory.setFile(new java.io.File(m_model.getCurrentDirectory()));
                
	m_iconLocation = model.getIconLocation();
	m_iconChooser.setCurrentDirectory(basedir);

	if (m_iconLocation != null)
	    {
		File il = new File(m_iconLocation);
		if (il.isAbsolute() == false)
		    {
			il = new File(basedir, m_iconLocation);
		    }
		m_iconChooser.setCurrentDirectory(il.getParentFile());
	    }

	if (m_iconLocation != null)
	    {
		String iconpath;

		if (new java.io.File(m_iconLocation).isAbsolute())
		    iconpath = m_iconLocation;
		else
		    iconpath = new java.io.File(m_basedir, model.getIconLocation()).getAbsolutePath();

		try {
		    //		    Class c = Class.forName("com.sun.jimi.core.Jimi");
		    //		    java.awt.Image img = com.sun.jimi.core.Jimi.getImage(iconpath);

		    Class c = Class.forName("com.sun.jimi.core.Jimi");
		    Method m = c.getDeclaredMethod("getImage", new Class[] { String.class });
		    java.awt.Image img = (java.awt.Image) m.invoke(null, new Object[] { iconpath });

		    if (img != null)
			{
			    ImageIcon icon = new ImageIcon(img);
			    m_iconDisplay.setIcon(icon);
			}
		} catch (Exception exc)
		    {
			ImageIcon icon = new ImageIcon(iconpath, "default icon");
			m_iconDisplay.setIcon(icon);
		    }
	    }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()//GEN-BEGIN:initComponents
    {
        java.awt.GridBagConstraints gridBagConstraints;

        m_iconChooser = new javax.swing.JFileChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        m_executableNameField = new net.charabia.jsmoothgen.application.gui.util.FileSelectionTextField();
        jLabel3 = new javax.swing.JLabel();
        m_currentDirectory = new net.charabia.jsmoothgen.application.gui.util.FileSelectionTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        m_iconDisplay = new javax.swing.JLabel();
        m_buttonIconChooser = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("<html><small>Specify here the location of the executable binary that is created by the project.<p>You can optionnally specify the current directory of the executable. The default current directory is where the binary is located.</small></html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(jLabel4, gridBagConstraints);

        jLabel1.setText("Executable Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.1;
        add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        add(m_executableNameField, gridBagConstraints);

        jLabel3.setText("Current Directory");
        add(jLabel3, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(m_currentDirectory, gridBagConstraints);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Executable Icon");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jLabel2, gridBagConstraints);

        jPanel2.setLayout(new java.awt.BorderLayout());

        m_iconDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        m_iconDisplay.setIcon(new javax.swing.ImageIcon(""));
        m_iconDisplay.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(m_iconDisplay, java.awt.BorderLayout.CENTER);

        m_buttonIconChooser.setText("...");
        m_buttonIconChooser.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                buttonIconChooserActionPerformed(evt);
            }
        });

        jPanel2.add(m_buttonIconChooser, java.awt.BorderLayout.EAST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        gridBagConstraints.weightx = 1.0;
        add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

    }//GEN-END:initComponents

    private void buttonIconChooserActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_buttonIconChooserActionPerformed
    {//GEN-HEADEREND:event_buttonIconChooserActionPerformed
	// Add your handling code here:
	if (m_iconChooser.showOpenDialog(this) == m_iconChooser.APPROVE_OPTION)
	    {
		System.out.println("Icon choosen : " + m_iconChooser.getSelectedFile().toString());
		String iconpath = m_iconChooser.getSelectedFile().toString();
 		ImageIcon icon = null;
		try {
		    Class c = Class.forName("com.sun.jimi.core.Jimi");

		    Method m = c.getDeclaredMethod("getImage", new Class[] { String.class });
		    java.awt.Image img = (java.awt.Image) m.invoke(null, new Object[] { iconpath });

		    if (img != null)
			{
			    icon = new ImageIcon(img);
			    m_iconDisplay.setIcon(icon);
			}
		} catch (Exception exc)
		    {
			icon = new ImageIcon(iconpath, "default icon");
			m_iconDisplay.setIcon(icon);
		    }


		// 		ImageIcon icon = new ImageIcon(m_iconChooser.getSelectedFile().getAbsolutePath(), "default icon");
 		int width = icon.getIconWidth();
 		int height = icon.getIconHeight();
// 		System.out.println("ICON, w:" + width + ", h:" + height);

// 		if ((width != height) || ((width != 8) && (width != 16) && (height != 32)))
// 		    {
// 			JOptionPane.showMessageDialog(this, "<html>The icon must be 8x8, 16x16, or 32x32! Your icon is " + width + "x" + height+ ", which doesn't match.</html>", "Icon issue", JOptionPane.WARNING_MESSAGE);
// 		    }
// 		else

		if (icon != null)
		    {
			m_iconDisplay.setIcon(icon);
			m_iconLocation = m_iconChooser.getSelectedFile().getAbsolutePath();
			this.validate();
			this.invalidate();
		    }

	    }
    }//GEN-LAST:event_buttonIconChooserActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton m_buttonIconChooser;
    private net.charabia.jsmoothgen.application.gui.util.FileSelectionTextField m_currentDirectory;
    private net.charabia.jsmoothgen.application.gui.util.FileSelectionTextField m_executableNameField;
    private javax.swing.JFileChooser m_iconChooser;
    private javax.swing.JLabel m_iconDisplay;
    // End of variables declaration//GEN-END:variables
		
    public String getExecutableName()
    {
	return m_executableNameField.getFile().toString();
    }
	
    public void updateModel()
    {
	if (m_executableNameField.getFile() != null)
	    m_model.setExecutableName(m_executableNameField.getFile().toString());
	else
	    m_model.setExecutableName(null);
            
	if (m_currentDirectory.getFile() != null)
	    m_model.setCurrentDirectory(m_currentDirectory.getFile().toString());
	else
	    m_model.setCurrentDirectory(null);
            
	m_model.setIconLocation(m_iconLocation);
    }
	
	
}
