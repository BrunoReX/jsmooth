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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Dumon
 */
public class ExecutableWizardPage extends WizardPage {
	private static final String EXE_WIZBAN = JSmoothResources.IMG_EXE_WIZBAN;
	private static final String STRING_BROWSE = "Browse...";
	
	private static final String WIDGET_ICON = "Icon";
	private static final String WIDGET_ICON_BUTTON = "IconButton";
	private static final String WIDGET_ICON_COMPOSITE = "IconComposite";
	private static final String WIDGET_ICON_LABEL = "IconLabel";
	private static final String WIDGET_NAME_BUTTON = "NameButton";
	private static final String WIDGET_NAME_LABEL = "NameLabel";
	private static final String WIDGET_NAME_TEXT = "NameText";
	private static final String WIDGET_TOP = "Top";
	
	public ExecutableWizardPage() {
		super("wizard.executble");
		setTitle("Executable");
		setMessage("Windows executable.");
		setImageDescriptor(JSmoothResources.getDescriptor(EXE_WIZBAN));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		composite.setLayout(layout);
		
		createWidgets(composite);
		
		setControl(composite);
	}
	
	private void createWidgets(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Executable name:");		
		GridData layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
		
		Button button = new Button(parent, SWT.NONE);
		button.setSize(Util.computeWidth(button, STRING_BROWSE), SWT.DEFAULT);
		button.setText(STRING_BROWSE);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = button.getSize().x;
		button.setLayoutData(layoutData);
		
		label = new Label(parent, SWT.NONE);
		label.setText("Executable icon:");
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		layoutData = new GridData(GridData.FILL);
		composite.setLayoutData(layoutData);
		
		createIconWidgets(composite);
	}
	
	/**
	 * @param parent
	 */
	private void createIconWidgets(Composite parent) {
		Label label = new Label(parent, SWT.BORDER);
		GridData layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = 32;
		layoutData.heightHint = 32;
		label.setLayoutData(layoutData);
		
		Button button = new Button(parent, SWT.NONE);
		button.setSize(Util.computeWidth(button, STRING_BROWSE), SWT.DEFAULT);
		button.setText(STRING_BROWSE);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = button.getSize().x;
		button.setLayoutData(layoutData);
	}

}
