/*
 * Created on Nov 22, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

/**
 * @author Dumon
 */
public class ClasspathWizardPage extends WizardPage {
	private static final int BUTTON_EXTRA_WIDTH = 6;
	private static final int CLASSPATH_HEIGHT = 10;
	private static final String JAVA_APP_WIZBAN = JSmoothResources.JAVA_APP_WIZBAN;
	private static final String ADD_ITEM = JSmoothResources.ADD_ITEM;
	private static final String REMOVE_ITEM = JSmoothResources.REMOVE_ITEM;
	private static final String EDIT_ITEM = JSmoothResources.EDIT_ITEM;
	private static final String MOVE_UP = JSmoothResources.MOVE_UP;
	private static final String MOVE_DOWN = JSmoothResources.MOVE_DOWN;
	
	public ClasspathWizardPage() {
		super("wizard.classpath");
		setTitle("Classpath");
		setMessage("The Java application classpath.");
		setImageDescriptor(
			JSmoothResources.getDescriptor(JAVA_APP_WIZBAN));
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
		
		createClasspath(comp);
		
		setControl(comp);
	}
	
	private int computeButtonWidth(Button button, String text) {
		initializeDialogUnits(button);
		return convertWidthInCharsToPixels(
			text.toCharArray().length + BUTTON_EXTRA_WIDTH);
	}
	
	private void createClasspath(Composite parent) {
		GridData layoutData = null;
		
		List list = new List(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_BOTH);
		list.setLayoutData(layoutData);
		
		Composite classpathButtonBar = new Composite(parent, SWT.NONE);
		classpathButtonBar.setLayout(new GridLayout());
		layoutData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		layoutData.heightHint =
			list.getItemHeight() * CLASSPATH_HEIGHT;
		classpathButtonBar.setLayoutData(layoutData);
		
		createButtonForClasspath(classpathButtonBar, "Add...");
		createButtonForClasspath(classpathButtonBar, "Remove...");
		createButtonForClasspath(classpathButtonBar, "Edit...");
	}
	
	private Button createButtonForClasspath(
		Composite parent,
		String text,
		Image image) {
			
		Button button = new Button(parent, SWT.PUSH);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.widthHint = computeButtonWidth(button, text);
		button.setLayoutData(layoutData);
		button.setText(text);
		button.setImage(image);
		
		return button;
	}
	
	private Button createButtonForClasspath(Composite parent, String text) {
		return createButtonForClasspath(parent, text, null);
	}
	
	private Button createButtonForClasspath(Composite parent, Image image) {
		return createButtonForClasspath(parent, "", image);
	}
	
}
