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
//		Incomplete.
//		addPage(new ExecutableWizardPage());

		addPage(new JavaAppWizardPage());
		addPage(new JVMSelectionWizardPage());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
