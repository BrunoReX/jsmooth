/*
 * Created on Nov 22, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
	
	private Button chkUseJarFile;
	private Text txtJarLocation;
	private Button btnJarLocation;
	private static final int DIM_CLASSPATH_HEIGHT = 10;
	private static final String STR_BROWSE = "Browse...";

	public MainClassWizardPage() {
		super("wizard.main_class");
		setTitle("Main Class");
		setMessage("The Java application main class.");
		String key = JSmoothResources.IMG_MAIN_CLASS_WIZBAN;
		setImageDescriptor(JSmoothResources.getDescriptor(key));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		composite.setLayout(layout);
		
		createWidgets(composite);
		
		useJarFileSelected();
		
		setControl(composite);
	}
	
	private void createWidgets(Composite parent) {
		chkUseJarFile = new Button(parent, SWT.CHECK | SWT.LEFT_TO_RIGHT);
		chkUseJarFile.setText("Use JAR file");
		chkUseJarFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				useJarFileSelected();
			}

		});
		GridData layoutData = new GridData(GridData.FILL);
		layoutData.horizontalSpan = 3;
		chkUseJarFile.setLayoutData(layoutData);
		
		Label label = new Label(parent, SWT.NONE);
		label.setText("JAR location:");
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		
		txtJarLocation = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		txtJarLocation.setLayoutData(layoutData);
		
		btnJarLocation = new Button(parent, SWT.NONE);
		btnJarLocation.setSize(Util.computeWidth(btnJarLocation, STR_BROWSE), SWT.DEFAULT);
		btnJarLocation.setText(STR_BROWSE);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = btnJarLocation.getSize().x;
		btnJarLocation.setLayoutData(layoutData);
		
		label = new Label(parent, SWT.NONE);
		label.setText("Main class:");
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text .setLayoutData(layoutData);
		
		Button button = new Button(parent, SWT.NONE);
		button.setSize(Util.computeWidth(button, STR_BROWSE), SWT.DEFAULT);
		button.setText(STR_BROWSE);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = button.getSize().x;
		button.setLayoutData(layoutData);
		
		label = new Label(parent, SWT.NONE);
		label.setText("Arguments:");
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		
		text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
	}

	private void useJarFileSelected() {
		txtJarLocation.setEnabled(chkUseJarFile.getSelection());
		btnJarLocation.setEnabled(chkUseJarFile.getSelection());
	}
	
}
