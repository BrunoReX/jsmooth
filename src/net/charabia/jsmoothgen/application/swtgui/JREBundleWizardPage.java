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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Dumon
 */
public class JREBundleWizardPage extends WizardPage {
	private static final int BUTTON_EXTRA_WIDTH = 6;
	private static final int VERSION_TEXT_WIDTH = 40;
	private static final int VERSION_TEXT_LIMIT = 5;
	private static final int JRE_SEARCH_HEIGHT = 10;
	private static final String JRE_WIZBAN =
		JSmoothResources.JRE_WIZBAN;
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
	 
	public JREBundleWizardPage() {
		super("wizard.jre_selection");
		setTitle("JRE Selection");
		setMessage("Parameters for the Java Runtime Environment.");
		setImageDescriptor(
			JSmoothResources.getDescriptor(JRE_WIZBAN));
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
		
		Group jreBundleGroup = createJREBundleGroup(comp);
		createJREBundle(jreBundleGroup);
		
		Group jreVersionGroup = createJREVersionGroup(comp);
		createJREVersion(jreVersionGroup);
		
		setControl(comp);
	}
	
	private Group createJREVersionGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(layoutData);
		GridLayout layout = new GridLayout(3, false);
		group.setLayout(layout);
		group.setText("JRE Version");
		
		return group;
	}
	
	private Group createJREBundleGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(layoutData);
		GridLayout layout = new GridLayout(3, false);
		group.setLayout(layout);
		group.setText("JRE Bundle");
		
		return group;
	}
	
	private void createJREVersion(Composite parent) {
		Button check = new Button(
			parent,
			SWT.CHECK | SWT.LEFT_TO_RIGHT);
		GridData layoutData = new GridData(GridData.FILL);
		check.setLayoutData(layoutData);
		check.setText("Need JRE version");
		
		createMinimumVersion(parent);
		createMaximumVersion(parent);
	}
	
	private void createMinimumVersion(Composite parent) {
		GridData layoutData = null;
		
		String title = "Minimum";
		Group group = new Group(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		group.setLayoutData(layoutData);
		GridLayout layout = new GridLayout();
		group.setLayout(layout);
		group.setText(title);
		
		Text text = new Text(group, SWT.BORDER);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = VERSION_TEXT_WIDTH;
		text.setLayoutData(layoutData);
		text.setTextLimit(VERSION_TEXT_LIMIT);
	}
	
	private void createMaximumVersion(Composite parent) {
		GridData layoutData = null;
		
		Group group = new Group(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		group.setLayoutData(layoutData);
		GridLayout layout = new GridLayout();
		group.setLayout(layout);
		group.setText("Maximum");
		
		Text text = new Text(group, SWT.BORDER);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = VERSION_TEXT_WIDTH;
		text.setLayoutData(layoutData);
		text.setTextLimit(VERSION_TEXT_LIMIT);
	}
	
	private void createJREBundle(Composite parent) {
		GridData layoutData = null;
		
		Button check = new Button(
			parent,
			SWT.CHECK | SWT.LEFT_TO_RIGHT);
		layoutData = new GridData(GridData.FILL);
		layoutData.horizontalSpan = 3;
		check.setLayoutData(layoutData);
		check.setText("Use JRE bundle");
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setText("Directory:");
		
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
	
	private int computeButtonWidth(Button button, String text) {
		initializeDialogUnits(button);
		return convertWidthInCharsToPixels(
			text.toCharArray().length + BUTTON_EXTRA_WIDTH);
	}
	
}
