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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * @author Dumon
 */
public class JavaAppWizardPage extends WizardPage {
	private static final String JAVA_APP_WIZBAN =
		JSmoothResources.JAVA_APP_WIZBAN;
	private static final int BUTTON_EXTRA_WIDTH = 6;
	private static final String ADD_ITEM =
		JSmoothResources.ADD_ITEM;
	private static final String REMOVE_ITEM =
		JSmoothResources.REMOVE_ITEM;
	private static final String EDIT_ITEM =
		JSmoothResources.EDIT_ITEM;
	private static final String MOVE_UP =
		JSmoothResources.MOVE_UP;
	private static final String MOVE_DOWN =
		JSmoothResources.MOVE_DOWN;
	
	public JavaAppWizardPage() {
		super("wizard.java_app");
		setTitle("Java Application");
		setMessage("Parameters for the Java application.");
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
		layout = new GridLayout();
		comp.setLayout(layout);
//		comp.setBackground(
//			new Color(Display.getCurrent(), 250, 50, 50));
		
		Group classpathGroup = createClasspathGroup(comp);
		createClasspath(classpathGroup);
		
		Group jarFileGroup = createJARFileGroup(comp);
		createJARFile(jarFileGroup);
		
		createSeparator(comp);
		
		Composite mainClassComposite = createMainClassComposite(comp);
		createMainClass(mainClassComposite);
		createArguments(mainClassComposite);
		
		setControl(comp);
	}
	
	private Group createClasspathGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		group.setLayoutData(layoutData);
		GridLayout layout = new GridLayout(2, false);
		group.setLayout(layout);
		group.setText("Classpath");
		
		return group;
	}
	
	private void createSeparator(Composite parent) {
		Label separatorLabel = new Label(
			parent,
			SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		separatorLabel.setLayoutData(layoutData);
		separatorLabel.setVisible(false);
	}
	
	private Group createJARFileGroup(Composite parent) {
		Group group =
			new Group(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		group.setLayout(layout);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		group.setLayoutData(layoutData);
		group.setText("JAR file");
		
		return group;
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
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("JAR location:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
		
		Button button = new Button(parent, SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		String buttonText = "Browse...";
		layoutData.widthHint =
			computeButtonWidth(button, buttonText);
		button.setLayoutData(layoutData);
		button.setText(buttonText);
	}
	
	private Composite createMainClassComposite(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		comp.setLayoutData(layoutData);
		GridLayout layout = new GridLayout(3, false);
		comp.setLayout(layout);
		
		return comp;
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
		
		Button button =
			new Button(parent, SWT.PUSH);
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
	
	private void createClasspath(Composite parent) {
		GridData layoutData = null;
		
		List list = new List(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_BOTH);
		list.setLayoutData(layoutData);
		
		Composite classpathButtonBar = new Composite(parent, SWT.NONE);
		classpathButtonBar.setLayout(new GridLayout());
		layoutData = new GridData(GridData.FILL);
		classpathButtonBar.setLayoutData(layoutData);
		
		createButtonForClasspath(classpathButtonBar, "Add...");
		createButtonForClasspath(classpathButtonBar, "Remove...");
		createButtonForClasspath(classpathButtonBar, "Edit...");
		
		Label separatorLabel = new Label(
			classpathButtonBar,
			SWT.SEPARATOR | SWT.HORIZONTAL);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		separatorLabel.setLayoutData(layoutData);
		
		createButtonForClasspath(
			classpathButtonBar,
			JSmoothResources.getImage(MOVE_UP));
		createButtonForClasspath(
			classpathButtonBar,
			JSmoothResources.getImage(MOVE_DOWN));
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
