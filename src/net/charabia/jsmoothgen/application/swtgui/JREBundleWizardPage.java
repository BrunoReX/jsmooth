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
	
	private Button chkUseJREBundle;
	private Text txtJREBundle;
	private Button btnJREBundle;
	
	private static final int DIM_VERSION_TEXT_WIDTH = 40;
	private static final int DIM_VERSION_TEXT_LIMIT = 5;
	private static final int DIM_JRE_SEARCH_HEIGHT = 10;
	private static final String STR_BROWSE = "Browse...";

	public JREBundleWizardPage() {
		super("wizard.jre_bundle");
		setTitle("JRE Bundle");
		setMessage("Java Runtime Environment bundle.");
		String key = JSmoothResources.IMG_JRE_WIZBAN;
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
		
		useJREBundleSelected();
		
		setControl(composite);
	}

	private void createWidgets(Composite parent) {
		chkUseJREBundle = new Button(parent, SWT.CHECK | SWT.LEFT_TO_RIGHT);
		chkUseJREBundle.setText("Use JRE bundle");
		chkUseJREBundle.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				useJREBundleSelected();
			}

		});
		GridData layoutData = new GridData(GridData.FILL);
		layoutData.horizontalSpan = 3;
		chkUseJREBundle.setLayoutData(layoutData);
		
		Label label = new Label(parent, SWT.NONE);
		label.setText("Directory:");
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		
		txtJREBundle = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		txtJREBundle.setLayoutData(layoutData);
		
		btnJREBundle = new Button(parent, SWT.NONE);
		btnJREBundle.setSize(Util.computeWidth(btnJREBundle, STR_BROWSE), SWT.DEFAULT);
		btnJREBundle.setText(STR_BROWSE);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = btnJREBundle.getSize().x;
		btnJREBundle.setLayoutData(layoutData);
		
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		composite.setLayout(layout);
		layoutData = new GridData(GridData.FILL);
		layoutData.horizontalSpan = 3;
		composite.setLayoutData(layoutData);
	}

	private void useJREBundleSelected() {
		txtJREBundle.setEnabled(chkUseJREBundle.getSelection());
		btnJREBundle.setEnabled(chkUseJREBundle.getSelection());
	}

}
