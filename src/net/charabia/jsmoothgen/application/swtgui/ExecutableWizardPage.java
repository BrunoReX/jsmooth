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
public class ExecutableWizardPage extends WizardPage {
	private static final int BUTTON_EXTRA_WIDTH = 6;
	private static final String EXE_WIZBAN = JSmoothResources.EXE_WIZBAN;
	
	public ExecutableWizardPage() {
		super("wizard.executble");
		setTitle("Executable");
		setMessage("Windows executable.");
		setImageDescriptor(JSmoothResources.getDescriptor(EXE_WIZBAN));
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
		
		createExecutableName(comp);
		createExecutableIcon(comp);
		
		setControl(comp);
	}
	
	/**
	 * @param comp
	 */
	private void createExecutableIcon(Composite parent) {
		GridData layoutData = null;
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("Executable icon:");
		
		Composite comp = new Composite(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		comp.setLayoutData(layoutData);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		comp.setLayout(layout);
		
		Label icon = new Label(comp, SWT.BORDER);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = 32;
		layoutData.heightHint = 32;
		icon.setLayoutData(layoutData);
		
		Button button = new Button(comp, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = computeButtonWidth(button, "Browse...");
		button.setLayoutData(layoutData);
		button.setText("Browse...");
		
		Label blank = new Label(parent, SWT.NONE);
	}

	/**
	 * @param comp
	 */
	private void createExecutableName(Composite parent) {
		GridData layoutData = null;
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("Executable name:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
		
		Button button = new Button(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = computeButtonWidth(button, "Browse...");
		button.setLayoutData(layoutData);
		button.setText("Browse...");
	}

	private int computeButtonWidth(Button button, String text) {
		initializeDialogUnits(button);
		return convertWidthInCharsToPixels(
			text.toCharArray().length + BUTTON_EXTRA_WIDTH);
	}
	
}
