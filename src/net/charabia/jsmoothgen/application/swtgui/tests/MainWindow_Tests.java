/*
 * Created on Nov 21, 2003
 */
package net.charabia.jsmoothgen.application.swtgui.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.charabia.jsmoothgen.application.swtgui.JSmoothResources;
import net.charabia.jsmoothgen.application.swtgui.MainWindow;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Dumon
 */
public class MainWindow_Tests extends TestCase {
	private Shell shell;
	
	// The trailing "/" required !
	private String installPath = "C:/eclipse-2.1.2/workspace/jsmooth/";
	
	public void testVisual() {
		new MainWindow().open();
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		shell = Display.getDefault().getActiveShell();
		JSmoothResources.loadResources(installPath);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(MainWindow_Tests.class);
	}
}
