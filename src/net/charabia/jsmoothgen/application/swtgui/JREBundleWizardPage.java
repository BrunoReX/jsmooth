/*
 * Created on Nov 21, 2003
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
public class JREBundleWizardPage extends WizardPage {
	private static final int BUTTON_EXTRA_WIDTH = 6;
	private static final int VERSION_TEXT_WIDTH = 40;
	private static final int VERSION_TEXT_LIMIT = 5;
	private static final int JRE_SEARCH_HEIGHT = 10;
	private static final String JRE_WIZBAN = JSmoothResources.JRE_WIZBAN;
	private static final String ADD_ITEM = JSmoothResources.ADD_ITEM;
	private static final String REMOVE_ITEM = JSmoothResources.REMOVE_ITEM;
	private static final String EDIT_ITEM = JSmoothResources.EDIT_ITEM;
	private static final String MOVE_UP = JSmoothResources.MOVE_UP;
	private static final String MOVE_DOWN = JSmoothResources.MOVE_DOWN;
	
	private Text versionText;
	private Text bundleText;
	private Button bundleBrowse;
	
	public JREBundleWizardPage() {
		super("wizard.jre_bundle");
		setTitle("JRE Bundle");
		setMessage("Java Runtime Environment bundle.");
		setImageDescriptor(JSmoothResources.getDescriptor(JRE_WIZBAN));
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
		
		createJREBundle(comp);
		createJREVersion(comp);
		
		setControl(comp);
	}
	
	private void createJREVersion(Composite parent) {
		GridData layoutData = null;
		
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		comp.setLayout(layout);
		layoutData = new GridData(GridData.FILL);
		layoutData.horizontalSpan = 3;
		comp.setLayoutData(layoutData);
		
		Button check = new Button(
			comp,
			SWT.CHECK | SWT.LEFT_TO_RIGHT);
		layoutData = new GridData(GridData.FILL);
		check.setLayoutData(layoutData);
		check.setText("Need minimum JRE version");
		check.addSelectionListener(
			new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					toggleJREVersionEnable(e);
				}

			});
		
		versionText = new Text(comp, SWT.BORDER);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = VERSION_TEXT_WIDTH;
		versionText.setLayoutData(layoutData);
		versionText.setTextLimit(VERSION_TEXT_LIMIT);
		versionText.setEnabled(false);
	}
	
	/**
	 * @param e
	 */
	protected void toggleJREVersionEnable(SelectionEvent e) {
		Button check = (Button) e.widget;
		versionText.setEnabled(check.getSelection());
	}

	/**
	 * @param e
	 */
	protected void toggleJREBundleEnable(SelectionEvent e) {
		Button check = (Button) e.widget;
		bundleText.setEnabled(check.getSelection());
		bundleBrowse.setEnabled(check.getSelection());
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
		check.addSelectionListener(
			new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					toggleJREBundleEnable(e);
				}

			});
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setText("Directory:");
		
		bundleText = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		bundleText.setLayoutData(layoutData);
		bundleText.setEnabled(false);
		
		bundleBrowse = new Button(parent, SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		String buttonText = "Browse...";
		layoutData.widthHint =
			computeButtonWidth(bundleBrowse, buttonText);
		bundleBrowse.setLayoutData(layoutData);
		bundleBrowse.setText(buttonText);
		bundleBrowse.setEnabled(false);
	}
	
	private int computeButtonWidth(Button button, String text) {
		initializeDialogUnits(button);
		return convertWidthInCharsToPixels(
			text.toCharArray().length + BUTTON_EXTRA_WIDTH);
	}
	
}
