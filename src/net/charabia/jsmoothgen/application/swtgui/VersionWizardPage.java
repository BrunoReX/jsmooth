/*
 * Created on Nov 21, 2003
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
public class VersionWizardPage extends WizardPage {
	private static final int VERSION_TEXT_WIDTH = 30;
	private static final int VERSION_TEXT_LIMIT = 5;
	
	public VersionWizardPage() {
		super("wizard.jvm_version");
		setTitle("JVM Version");
		setMessage("Specify the minimum and maximum JVM versions.");
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		GridLayout layout = null;
		GridData layoutData = null;
		
		Composite comp = new Composite(parent, SWT.NONE);
		layout = new GridLayout(2, false);
		comp.setLayout(layout);
//		comp.setBackground(
//			new Color(Display.getCurrent(), 250, 50, 50));
		
		createNeedVersion(comp);
		createMinimumVersion(comp);
		createMaximumVersion(comp);
		
		setControl(comp);
	}
	
	private void createNeedVersion(Composite parent) {
		Button check = new Button(
			parent,
			SWT.CHECK | SWT.LEFT_TO_RIGHT);
		GridData layoutData = new GridData(GridData.FILL);
		layoutData.horizontalSpan = 2;
		check.setLayoutData(layoutData);
		check.setText("Need version");
	}
	
	private void createMinimumVersion(Composite parent) {
		GridData layoutData = null;
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("Minimum:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = VERSION_TEXT_WIDTH;
		text.setLayoutData(layoutData);
		text.setTextLimit(VERSION_TEXT_LIMIT);
	}
	
	private void createMaximumVersion(Composite parent) {
		GridData layoutData = null;
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("Maximum:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = VERSION_TEXT_WIDTH;
		text.setLayoutData(layoutData);
		text.setTextLimit(VERSION_TEXT_LIMIT);
	}
}
