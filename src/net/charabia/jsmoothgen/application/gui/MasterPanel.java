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
import net.charabia.jsmoothgen.skeleton.*;
import net.charabia.jsmoothgen.application.gui.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import com.l2fprod.common.swing.*;

public class MasterPanel extends JPanel
{
    private JButtonBar m_leftBar = new JButtonBar(JButtonBar.VERTICAL);
    private ButtonGroup m_leftGroup = new ButtonGroup();
    private JPanel m_mainpanel = new JPanel();
    private JScrollBar m_mainpanelVBar;

    //    private ResourceBundle m_texts;
    
    private Vector m_displayedElements = new Vector();

    private JSmoothModelBean m_model = new JSmoothModelBean();
    private java.io.File m_modelLocation = null;

    private EditorPool m_edPool = new EditorPool();

    private String m_currentPanelName = "";

    private Class[] m_skelElements = { net.charabia.jsmoothgen.application.gui.editors.SkeletonChooser.class, net.charabia.jsmoothgen.application.gui.editors.SkeletonProperties.class };

    private Class[] m_execElements = { 
	net.charabia.jsmoothgen.application.gui.editors.ExecutableName.class ,
	net.charabia.jsmoothgen.application.gui.editors.ExecutableIcon.class ,
	net.charabia.jsmoothgen.application.gui.editors.CurrentDirectory.class
    };

    private Class[] m_appElements = {
	net.charabia.jsmoothgen.application.gui.editors.MainClass.class,
	net.charabia.jsmoothgen.application.gui.editors.ApplicationArguments.class,
	net.charabia.jsmoothgen.application.gui.editors.EmbeddedJar.class,
	net.charabia.jsmoothgen.application.gui.editors.ClassPath.class
    };

    private Class[] m_jvmSelElements = {
	net.charabia.jsmoothgen.application.gui.editors.MinVersion.class,
	net.charabia.jsmoothgen.application.gui.editors.MaxVersion.class,
	net.charabia.jsmoothgen.application.gui.editors.JVMBundle.class,
	net.charabia.jsmoothgen.application.gui.editors.JVMSearchSequence.class
    };

    private Class[] m_jvmCfgElements = {
	net.charabia.jsmoothgen.application.gui.editors.MaxMemoryHeap.class,
	net.charabia.jsmoothgen.application.gui.editors.InitialMemoryHeap.class,
	net.charabia.jsmoothgen.application.gui.editors.JavaProperties.class
    };

    public MasterPanel()
    {
	setLayout(new BorderLayout());
	add(BorderLayout.WEST, new JScrollPane(m_leftBar));
	JScrollPane scp = new JScrollPane(m_mainpanel);
	m_mainpanelVBar = scp.getVerticalScrollBar();
	add(BorderLayout.CENTER, scp);
	scp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	PanelLayout pl = new PanelLayout();
	m_mainpanel.setLayout(pl);

	addAction("Skeleton", "/icons/stock_new-template.png", m_skelElements);
	addAction("Executable", "/icons/stock_autopilot-24.png", m_execElements);
	addAction("Application", "/icons/stock_form-image-control.png", m_appElements);
	addAction("JVM Selection", "/icons/stock_search.png", m_jvmSelElements);
	addAction("JVM Configuration", "/icons/stock_form-properties.png", m_jvmCfgElements);
	
	//	m_texts = PropertyResourceBundle.getBundle("locale.Texts");
    }

    private String getLocaleText(String key)
    {
	try {
	    String value = Main.TEXTS.getString(key);
	    return value;
	} catch (Exception exc)
	    {
	    }
	return key;
    }

    private void addAction(final String name, String iconloc, final Class[] els)
    {
	final Action a = new AbstractAction( name, new ImageIcon(getClass().getResource(iconloc))) {
		public void actionPerformed(ActionEvent e) 
		{
		    if (m_currentPanelName.equals(name))
			return;
		    setupPanel(els);
		    m_currentPanelName = name;
		}
	    };
	
	JToggleButton jtb = new JToggleButton(a);
	m_leftGroup.add(jtb);
	m_leftBar.add(jtb);
    }

    public void setupPanel(Class[] els)
    {
	fireUpdateModel();
	detachAll();
	
	m_mainpanel.removeAll();
	m_displayedElements.removeAllElements();

	System.out.println("Adding " + els);

	if (els == null)
	    return;

	for (int i=0; i<els.length; i++)
	    {
		try {
		    //		    Editor ed = (Editor)els[i].newInstance();
		    Editor ed = m_edPool.getInstance(els[i]);

		    OptionalHelpPanel help = new OptionalHelpPanel();
		    help.getContentPane().setLayout(new BorderLayout());
		    help.getContentPane().add(BorderLayout.CENTER, ed);

		    help.setLabel(getLocaleText(ed.getLabel()));
		    help.setHelpText(getLocaleText(ed.getDescription()));

		    m_mainpanel.add(help);
		    m_displayedElements.add(ed);

		} catch (Exception exc)
		    {
			exc.printStackTrace();
		    }
	    }
	m_mainpanelVBar.setValue(0);
	attachAll();

	validate();
	repaint();
    }

    public void fireUpdateModel()
    {
	for (Iterator i=m_displayedElements.iterator(); i.hasNext(); )
	    {
		Editor ed = (Editor)i.next();
		ed.updateModel();
	    }	
    }

    public void fireModelChanged()
    {
	for (Iterator i=m_displayedElements.iterator(); i.hasNext(); )
	    {
		Editor ed = (Editor)i.next();
		ed.dataChanged();
	    }	
    }

    private void detachAll()
    {
	for (Iterator i=m_displayedElements.iterator(); i.hasNext(); )
	    {
		Editor ed = (Editor)i.next();

		if (ed instanceof JSmoothModelBean.Listener)
		    m_model.removeListener((JSmoothModelBean.Listener)ed);
		if (ed instanceof JSmoothModelBean.SkeletonChangedListener)
		    m_model.removeSkeletonChangedListener((JSmoothModelBean.SkeletonChangedListener)ed);

		ed.detach();
	    }
    }

    private void attachAll()
    {
	System.out.println("Attaching all with " + m_modelLocation + ": " + m_model);
	for (Iterator i=m_displayedElements.iterator(); i.hasNext(); )
	    {
		Editor ed = (Editor)i.next();

		File basedir = null;
		if (m_modelLocation != null)
		    basedir = m_modelLocation.getParentFile();

		ed.attach(m_model, basedir);
		if (ed instanceof JSmoothModelBean.Listener)
		    m_model.addListener((JSmoothModelBean.Listener)ed);
		if (ed instanceof JSmoothModelBean.SkeletonChangedListener)
		    m_model.addSkeletonChangedListener((JSmoothModelBean.SkeletonChangedListener)ed);

		ed.dataChanged();
	    }	
    }

    public void newModel()
    {
	JSmoothModelBean bean = new JSmoothModelBean();
	newModel(bean, null);
    }

    public void newModel(JSmoothModelBean bean, java.io.File location)
    {
	detachAll();

	m_model = bean;
	m_modelLocation = location;

	attachAll();
    }

    public boolean openFile(java.io.File f)
    {
	m_modelLocation = f;

	try
	    {
		JSmoothModelBean model = JSmoothModelPersistency.load(m_modelLocation);
		newModel(model, f);
		return true;
	    } catch (java.io.IOException iox)
		{
		    iox.printStackTrace();
		    return false;
		}
    }

    public boolean save()
    {
	if (m_modelLocation == null)
	    return false;
	try {
	    fireUpdateModel();
	    m_model.normalizePaths(m_modelLocation.getParentFile(), true);
	    JSmoothModelPersistency.save(m_modelLocation, m_model);

	    fireModelChanged();
	    return true;
	} catch (java.io.IOException iox)
	    {
		iox.printStackTrace();
	    }
	return false;
    }

    private Vector m_lastErrors = new Vector();

    public Vector getLastErrors()
    {
	return m_lastErrors;
    }

    public ExeCompiler.CompilerRunner getCompiler()
    {
	fireUpdateModel();
	m_model.normalizePaths(m_modelLocation.getParentFile());
	m_lastErrors.removeAllElements();

	SkeletonBean skel = Main.SKELETONS.getSkeleton(m_model.getSkeletonName());
	if (skel == null)
	    {
		m_lastErrors.add(Main.local("UNKNOWN_SKEL"));
		return null;
	    }

	File skelroot = Main.SKELETONS.getDirectory(skel);
	File basedir = m_modelLocation.getParentFile();
	File exedir = basedir;

	try {
	    File out = null;
	    if (new File(m_model.getExecutableName()).isAbsolute() == false)
		out = new File(exedir, m_model.getExecutableName());
	    else
		out = new File(m_model.getExecutableName());

	    System.out.println("out = "+ out.getAbsolutePath());
	    ExeCompiler compiler = new ExeCompiler();
	    ExeCompiler.CompilerRunner runner = compiler.getRunnable(skelroot, skel, basedir, m_model, out);
	    return runner;
	} catch (Exception exc)
	    {
		exc.printStackTrace();
		m_lastErrors.add(exc.getMessage());
		return null;
	    }
    }
    
    public void runexe()
    {
	fireUpdateModel();
		
	try {
	    File basedir = m_modelLocation.getParentFile();
	    File f = new File(basedir, m_model.getExecutableName());
	    String[] cmd = new String[]{ f.getAbsolutePath() };
	    
	    System.out.println("RUNNING " + cmd[0] + " @ " + basedir);
	    CommandRunner.run(cmd, f.getParentFile());
	} catch (Exception exc)
	    {
		exc.printStackTrace();
	    }
    }

    public java.io.File getProjectFile()
    {
	return m_modelLocation;
    }

    public void setProjectFile(java.io.File prjfile)
    {
	m_modelLocation = prjfile;
    }
    

    public JSmoothModelBean getModel()
    {
	return m_model;
    }

    public static void main(String args[])
    {
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) { }
	JFrame test = new JFrame("test");
	test.getContentPane().add(new MasterPanel());
	test.pack();
	test.setVisible(true);
    }
}
