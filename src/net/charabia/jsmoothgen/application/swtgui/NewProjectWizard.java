/*
 * Created on Nov 21, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import net.charabia.jsmoothgen.application.JSmoothModelBean;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Dumon
 */
public class NewProjectWizard extends Wizard {
	
	private JSmoothModelBean model;
	
	public NewProjectWizard() {
		setWindowTitle("New JSmooth Project");
		model = new JSmoothModelBean();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		addPage(new ProjectFileWizardPage());
		addPage(new ClasspathWizardPage());
		addPage(new MainClassWizardPage());
		addPage(new JRESearchWizardPage());
		addPage(new JREBundleWizardPage());
		addPage(new ExecutableWizardPage());
	}

}
