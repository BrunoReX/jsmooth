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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

/**
 * @author Dumon
 */
public class ClasspathWizardPage extends WizardPage {
	private static final String CLASSPATH_WIZBAN =
		JSmoothResources.CLASSPATH_WIZBAN;
	
	public ClasspathWizardPage() {
		super("wizard.classpath");
		setTitle("Classpath");
		setMessage("Specify the project classpath.");
		setImageDescriptor(
			JSmoothResources.getDescriptor(CLASSPATH_WIZBAN));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		GridLayout layout = null;
		GridData layoutData = null;
		
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
//		comp.setBackground(
//			new Color(Display.getCurrent(), 250, 50, 50));
		
		Group classpathGroup = new Group(comp, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		classpathGroup.setLayoutData(layoutData);
		layout = new GridLayout(2, false);
		classpathGroup.setLayout(layout);
		classpathGroup.setText("Classpath");

		createClasspath(classpathGroup);
		
		setControl(comp);
	}
	
	private void createClasspath(Composite parent) {
		GridData layoutData = null;
		
		List list = new List(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_BOTH);
		list.setLayoutData(layoutData);
		
		Composite classpathButtonBar = new Composite(parent, SWT.NONE);
		classpathButtonBar.setLayout(new GridLayout());
		layoutData = new GridData(GridData.FILL);
		classpathButtonBar.setLayoutData(layoutData);
		
		Button addButton = new Button(classpathButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		addButton .setLayoutData(layoutData);
		addButton .setText("Add...");
		
		Button removeButton = new Button(classpathButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		removeButton .setLayoutData(layoutData);
		removeButton .setText("Remove...");
		
		Button editButton = new Button(classpathButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		editButton .setLayoutData(layoutData);
		editButton .setText("Edit...");
		
		Label separatorLabel = new Label(
			classpathButtonBar,
			SWT.SEPARATOR | SWT.HORIZONTAL);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		separatorLabel .setLayoutData(layoutData);
		separatorLabel.setVisible(false);
		
		Button upButton = new Button(classpathButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		upButton.setLayoutData(layoutData);
		upButton.setText("Up");
		
		Button downButton = new Button(classpathButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		downButton.setLayoutData(layoutData);
		downButton.setText("Down");
	}
}
