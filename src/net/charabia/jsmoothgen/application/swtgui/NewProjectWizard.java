/*
 * Created on Nov 21, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Dumon
 */
public class NewProjectWizard extends Wizard {
	
	public NewProjectWizard() {
		setWindowTitle("New Project");
		
		addPage(new JavaAppWizardPage());
		addPage(new JRESelectionWizardPage());
		
		// For Rodrigo.
//		addPage(new ProjectNameWizardPage());
//		addPage(new ClasspathWizardPage());
//		addPage(new EmbedJarWizardPage());
//		addPage(new MainClassWizardPage());
//		addPage(new VersionWizardPage());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
