/*
 * Created on Nov 21, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.io.File;

import net.charabia.jsmoothgen.application.JSmoothModelBean;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Dumon
 */
public class ExecutableWizardPage extends WizardPage {
	
	public ExecutableWizardPage() {
		super("wizard.executable");
		setTitle("Executable");
		setMessage("Specify parameters for the Windows executable.");
	}
	
	/* (non-Javadoc)
	 * @see net.charabia.jsmoothgen.application.gui.ModelUpdater#setModel(java.io.File, net.charabia.jsmoothgen.application.JSmoothModelBean)
	 */
	public void setModel(File basedir, JSmoothModelBean model) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.charabia.jsmoothgen.application.gui.ModelUpdater#updateModel()
	 */
	public void updateModel() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		GridLayout layout = null;
		GridData layoutData = null;
		
		Composite comp = new Composite(parent, SWT.NONE);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		comp.setLayout(layout);
//		comp.setBackground(
//			new Color(Display.getCurrent(), 250, 50, 50));
		
		Composite executableComposite =
			new Composite(comp, SWT.NONE);
		layout = new GridLayout(3, false);
		executableComposite.setLayout(layout);
		layoutData = new GridData(GridData.FILL_BOTH);
		executableComposite.setLayoutData(layoutData);
		
		createExecutableName(executableComposite);
		createCurrentDirectory(executableComposite);
		createExecutableIcon(executableComposite);
		
		setControl(comp);
	}
	
	private void createExecutableName(Composite parent) {
		GridData layoutData = null;
		
		Label label = new Label(parent, SWT.NONE);
		label.setText("Executable Name:");
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
		
		Button browseButton = new Button(parent, SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		browseButton.setLayoutData(layoutData);
		browseButton.setText("Browse...");
	}
	
	private void createCurrentDirectory(Composite parent) {
		GridData layoutData = null;
		
		Label label = new Label(parent, SWT.NONE);
		label.setText("Current Directory:");
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		
		Text text = new Text(parent , SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
		
		Button browseButton = new Button(parent , SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		browseButton.setLayoutData(layoutData);
		browseButton.setText("Browse...");
	}
	
	private void createExecutableIcon(Composite parent) {
		GridData layoutData = null;
		
		Label label = new Label(parent , SWT.NONE);
		label.setText("Executable Icon:");
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		
		Label icon = new Label(parent , SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		icon.setLayoutData(layoutData);
		icon.setText("<Image>");
		
		Button browseButton = new Button(parent , SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		browseButton .setLayoutData(layoutData);
		browseButton .setText("Browse...");
	}
}
