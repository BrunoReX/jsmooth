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
	private static final int BUTTON_EXTRA_WIDTH = 6;
	private static final int CLASSPATH_HEIGHT = 10;
	private static final String JAVA_APP_WIZBAN = JSmoothResources.JAVA_APP_WIZBAN;
	private static final String ADD_ITEM = JSmoothResources.ADD_ITEM;
	private static final String REMOVE_ITEM = JSmoothResources.REMOVE_ITEM;
	private static final String EDIT_ITEM = JSmoothResources.EDIT_ITEM;
	private static final String MOVE_UP = JSmoothResources.MOVE_UP;
	private static final String MOVE_DOWN = JSmoothResources.MOVE_DOWN;
	
	private Text jarFile;
	private Button jarBrowse;
	
	public MainClassWizardPage() {
		super("wizard.main_class");
		setTitle("Main Class");
		setMessage("The Java application main class.");
		setImageDescriptor(JSmoothResources.getDescriptor(JAVA_APP_WIZBAN));
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
		
		createJARFile(comp);
		createMainClass(comp);
		createArguments(comp);
		
		setControl(comp);
	}
	
	private void createJARFile(Composite parent) {
		GridData layoutData = null;
		
		Button check = new Button(
			parent,
			SWT.CHECK | SWT.LEFT_TO_RIGHT);
		layoutData = new GridData(GridData.FILL);
		layoutData.horizontalSpan = 3;
		check.setLayoutData(layoutData);
		check.setText("Use JAR file");
		check.addSelectionListener(
			new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					toggleJARFileEnable(e);
				}

			});
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("JAR location:");
		
		jarFile = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		jarFile.setLayoutData(layoutData);
		jarFile.setEnabled(false);
		
		jarBrowse = new Button(parent, SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		String buttonText = "Browse...";
		layoutData.widthHint =
			computeButtonWidth(jarBrowse, buttonText);
		jarBrowse.setLayoutData(layoutData);
		jarBrowse.setText(buttonText);
		jarBrowse.setEnabled(false);
	}
	
	/**
	 * 
	 */
	protected void toggleJARFileEnable(SelectionEvent e) {
		Button check = (Button) e.widget;
		jarFile.setEnabled(check.getSelection());
		jarBrowse.setEnabled(check.getSelection());
	}

	private void createMainClass(Composite parent) {
		GridData layoutData = null;

		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("Main class:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
		
		Button button = new Button(parent, SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		String buttonText = "Browse...";
		layoutData.widthHint = computeButtonWidth(button, buttonText);
		button.setLayoutData(layoutData);
		button.setText(buttonText);
	}
	
	private int computeButtonWidth(Button button, String text) {
		initializeDialogUnits(button);
		return convertWidthInCharsToPixels(
			text.toCharArray().length + BUTTON_EXTRA_WIDTH);
	}
	
	private void createArguments(Composite parent) {
		GridData layoutData = null;
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("Arguments:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
	}
	
}
