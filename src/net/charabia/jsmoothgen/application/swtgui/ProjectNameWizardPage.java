/*
 * Created on Nov 28, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Dumon
 */
public class ProjectNameWizardPage extends WizardPage {
	private static final String NEW_PROJECT_WIZBAN =
		JSmoothResources.NEW_PROJECT_WIZBAN;
	private static final int BUTTON_EXTRA_WIDTH = 6;
	
	public ProjectNameWizardPage() {
		super("wizard.project_name");
		setTitle("JSmooth Project");
		setMessage("Create a new JSmooth project.");
		setImageDescriptor(
			JSmoothResources.getDescriptor(NEW_PROJECT_WIZBAN));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		comp.setLayout(layout);
		
		createName(comp);
		createSeparator(comp);
		createLocation(comp);
		
		setControl(comp);
	}
	
	private void createName(Composite parent) {
		GridData layoutData = null;
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("Project name:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
	}
	
	private void createSeparator(Composite parent) {
		Label separator = new Label(parent,
			SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 2;
		separator.setLayoutData(layoutData);
		separator.setVisible(false);
	}
	
	private void createLocation(Composite parent) {
		GridData layoutData = null;
		
		Group group = new Group(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 2;
		group.setLayoutData(layoutData);
		GridLayout layout = new GridLayout(3, false);
		group.setLayout(layout);
		group.setText("Project location");
		
		Label label = new Label(group, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("Directory:");
		
		Text text = new Text(group, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
		
		Button button = new Button(group, SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		String buttonText = "Browse...";
		layoutData.widthHint =
			computeButtonWidth(button, buttonText);
		button.setLayoutData(layoutData);
		button.setText(buttonText);
	}
	
	private int computeButtonWidth(Button button, String text) {
		initializeDialogUnits(button);
		return convertWidthInCharsToPixels(
			text.toCharArray().length + BUTTON_EXTRA_WIDTH);
	}
}
