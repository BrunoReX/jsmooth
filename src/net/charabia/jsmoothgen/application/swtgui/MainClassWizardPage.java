/*
 * Created on Nov 22, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

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
public class MainClassWizardPage extends WizardPage {
	private static final String MAIN_CLASS_WIZBAN =
		JSmoothResources.MAIN_CLASS_WIZBAN;
	
	public MainClassWizardPage() {
		super("wizard.main_class");
		setTitle("Main Class");
		setMessage(
			"Specify the main class and the optional arguments.");
		setImageDescriptor(
			JSmoothResources.getDescriptor(MAIN_CLASS_WIZBAN));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		GridLayout layout = null;
		GridData layoutData = null;
		
		Composite comp = new Composite(parent, SWT.NONE);
		layout = new GridLayout(3, false);
		comp.setLayout(layout);
//		comp.setBackground(
//			new Color(Display.getCurrent(), 250, 50, 50));
		
		createMainClass(comp);
		createArguments(comp);
		
		setControl(comp);
	}
	
	private void createJarLocation(Composite parent) {
		GridData layoutData = null;
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("Jar location:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text .setLayoutData(layoutData);
		
		Button browseButton = new Button(parent, SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		browseButton.setLayoutData(layoutData);
		browseButton.setText("Browse...");
	}
	
	private void createMainClass(Composite parent) {
		GridData layoutData = null;

		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label .setLayoutData(layoutData);
		label .setText("Main Class:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text .setLayoutData(layoutData);
		
		Button browseButton =
			new Button(parent, SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		browseButton .setLayoutData(layoutData);
		browseButton .setText("Browse...");
	}
	
	private void createArguments(Composite parent) {
		GridData layoutData = null;
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label .setLayoutData(layoutData);
		label .setText("Arguments:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text .setLayoutData(layoutData);
	}
}
