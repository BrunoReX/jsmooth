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
import org.eclipse.swt.widgets.Text;

/**
 * @author Dumon
 */
public class JavaAppWizardPage extends WizardPage {

	public JavaAppWizardPage() {
		super("wizard.java_app");
		setTitle("Java Application");
		setMessage("Specify parameters for the Java application.");
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
		
		Composite argumentsComposite =
			new Composite(comp, SWT.NONE);
		layout = new GridLayout(3, false);
		argumentsComposite.setLayout(layout);
		layoutData = new GridData(GridData.FILL_BOTH);
		argumentsComposite.setLayoutData(layoutData);
		
		createJarLocation(argumentsComposite);
		createMainClass(argumentsComposite);
		createArguments(argumentsComposite);
		
		Group classpathGroup = new Group(comp, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		classpathGroup.setLayoutData(layoutData);
		layout = new GridLayout(2, false);
		classpathGroup.setLayout(layout);
		classpathGroup.setText("Classpath");

		createClasspath(classpathGroup);
		
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
		
		Label separatorLabel = new Label(classpathButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		separatorLabel .setLayoutData(layoutData);
		
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
