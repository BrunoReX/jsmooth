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

import net.charabia.jsmoothgen.skeleton.*;
import net.charabia.jsmoothgen.application.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 *
 * @author  Rodrigo
 */
public class SkeletonPropertiesPanel extends javax.swing.JPanel  implements ModelUpdater
{
	private JSmoothModelBean m_model;
	private SkeletonProperty[] m_skelprops = null;
	private Hashtable m_propIdToGUI = new Hashtable();
	
	/** Creates new form SkeletonSpecificsPanel */
	public SkeletonPropertiesPanel()
	{
		initComponents();
	}
	
	public void setSkeletonProperties(SkeletonProperty[] props)
	{
		if (props == null)
			props = new SkeletonProperty[0];
		
		m_skelprops = props;
		this.removeAll();
		m_propIdToGUI = new Hashtable();
		
		for (int i=0; i<props.length; i++)
		{
			addPropertyGUI(props[i]);
		}
	}
	
	public void addPropertyGUI(SkeletonProperty prop)
	{
		GridBagLayout layout = (GridBagLayout)this.getLayout();

		JLabel desc = null;
		GridBagConstraints descconst = null;
		if ((prop.getDescription() != null) && (prop.getDescription().trim().length() > 0))
		{
			desc = new JLabel("<html><small>" + prop.getDescription() + "</small></html>");
			descconst = new GridBagConstraints();
			descconst.fill = GridBagConstraints.HORIZONTAL;
			descconst.gridwidth = GridBagConstraints.REMAINDER;
			descconst.weightx = 1.0;
			// layout.setConstraints(desc, descconst);
			//			this.add(desc);
		}
		
		System.out.println("creating gui " + prop.getLabel());
		JLabel lab = new JLabel(prop.getLabel());
		GridBagConstraints labconst = new GridBagConstraints();
		labconst.fill = GridBagConstraints.BOTH;
		
		if (prop.getType().equals(SkeletonProperty.TYPE_STRING))
		{
			String val = prop.getValue();
			if ((val == null) || (val.length() == 0))
				val = "";
			JTextField tf = new JTextField(val);
			m_propIdToGUI.put(prop.getIdName(), tf);
			
			layout.setConstraints(lab, labconst);
			
			GridBagConstraints compconst = new GridBagConstraints();
			compconst.weightx = 1.0;
			compconst.fill = GridBagConstraints.HORIZONTAL;
			compconst.gridwidth = GridBagConstraints.REMAINDER;
			layout.setConstraints(tf, compconst);

			if (desc != null)
			    {
				layout.setConstraints(desc, descconst);
				this.add(desc);
			    }
			
			this.add(lab);
			this.add(tf);
			
		} else if (prop.getType().equals(SkeletonProperty.TYPE_TEXTAREA))
		{
			String val = prop.getValue();
			if ((val == null) || (val.length() == 0))
				val = "";
			JTextArea area = new JTextArea(val);
			m_propIdToGUI.put(prop.getIdName(), area);
			
			labconst.gridwidth = GridBagConstraints.REMAINDER;
			layout.setConstraints(lab, labconst);
			
			GridBagConstraints compconst = new GridBagConstraints();
			compconst.weightx = 1.0;
			compconst.weighty = 1.0;
			compconst.fill = GridBagConstraints.BOTH;
			compconst.gridwidth = GridBagConstraints.REMAINDER;

			if (desc != null)
			    {
				layout.setConstraints(desc, descconst);
				this.add(desc);
			    }
			
			JScrollPane jsp = new JScrollPane(area);
			layout.setConstraints(jsp, compconst);
			this.add(lab);
			this.add(jsp);

                } else if (prop.getType().equals(SkeletonProperty.TYPE_BOOLEAN))
		{
			String val = prop.getValue();
			if ((val == null) || (val.length() == 0))
				val = "0";
			int ival = Integer.parseInt(val);
			System.out.println("BOOLEAN JCB : " + prop.getDescription());
			String label = "";

			if (prop.getDescription() != null)
			    lab.setText("<html><small>" + prop.getDescription() + "</small></html>");
                        else
                            lab.setText("");
                        label = prop.getLabel();

			JCheckBox jcb = new JCheckBox(label, ival!=0);
			m_propIdToGUI.put(prop.getIdName(), jcb);
			
			GridBagConstraints compconst = new GridBagConstraints();
			compconst.fill = GridBagConstraints.HORIZONTAL;
			compconst.gridwidth = GridBagConstraints.REMAINDER;

                        labconst.gridwidth = GridBagConstraints.REMAINDER;
			layout.setConstraints(lab, labconst);
			
			layout.setConstraints(jcb, compconst);
			this.add(lab);
			this.add(jcb);
		}
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    private void initComponents()//GEN-BEGIN:initComponents
    {

        setLayout(new java.awt.GridBagLayout());

    }//GEN-END:initComponents

	public void setModel(java.io.File basedir, net.charabia.jsmoothgen.application.JSmoothModelBean model)
	{
		m_model = model;
		JSmoothModelBean.Property[] props = model.getSkeletonProperties();
		if (props != null)
		{
			for (int i=0; i<props.length; i++)
			{
				if (props[i].Key == null)
					props[i].Key = "";
		
				JComponent comp = (JComponent) m_propIdToGUI.get(props[i].Key);
				if (comp instanceof JTextComponent)
				{
					if (props[i].Value == null)
						props[i].Value = "";
					((JTextComponent)comp).setText(props[i].Value);
				} else if (comp instanceof JCheckBox)
				    {
					((JCheckBox)comp).setSelected("1".equals(props[i].Value));
				    }
			}
		}
	}	
	
	public void updateModel()
	{
		Vector result = new Vector();
		for (int i=0; i<m_skelprops.length; i++)
		{
			SkeletonProperty sp = m_skelprops[i];
			JComponent comp = (JComponent)m_propIdToGUI.get(sp.getIdName());
			if (comp instanceof JTextComponent)
			{
				JSmoothModelBean.Property p = new JSmoothModelBean.Property();
				p.Key = sp.getIdName();
				p.Value = ((JTextComponent)comp).getText();
				result.add(p);
			} else if (comp instanceof JCheckBox)
			    {
				JSmoothModelBean.Property p = new JSmoothModelBean.Property();
				p.Key = sp.getIdName();
				p.Value = ((JCheckBox)comp).isSelected()?"1":"0";
				result.add(p);
			    }
		}
		JSmoothModelBean.Property[] ra = new JSmoothModelBean.Property[result.size()];
		for (int i=0; i<result.size(); i++)
		{
			ra[i] = (JSmoothModelBean.Property)result.get(i);
		}

		m_model.setSkeletonProperties(ra);
	}	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
	
}
