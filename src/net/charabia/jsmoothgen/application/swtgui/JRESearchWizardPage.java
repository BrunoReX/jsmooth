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
import org.eclipse.swt.widgets.List;

/**
 * @author Dumon
 */
public class JRESearchWizardPage extends WizardPage {
	
	private static final int JRE_SEARCH_HEIGHT = 10;
	private static final String MOVE_DOWN = JSmoothResources.IMG_MOVE_DOWN;
	private static final String MOVE_UP = JSmoothResources.IMG_MOVE_UP;

	private static final String STRING_ADD = "Add...";
	private static final String STRING_EDIT = "Edit...";
	private static final String STRING_REMOVE = "Remove...";
	private static final int VERSION_TEXT_LIMIT = 5;
	private static final int VERSION_TEXT_WIDTH = 40;
	
	private static final String WIDGET_ADD_BUTTON = "AddButton";
	private static final String WIDGET_BUTTON_BAR = "ButtonBar";
	private static final String WIDGET_EDIT_BUTTON = "EditButton";
	private static final String WIDGET_LIST = "List";
	private static final String WIDGET_REMOVE_BUTTON = "RemoveButton";
	private static final String WIDGET_TOP = "Top";

	public JRESearchWizardPage() {
		super("wizard.jre_search");
		setTitle("JRE Search");
		setMessage("Locations to search the Java Runtime Environment.");
		String key = JSmoothResources.IMG_JRE_WIZBAN;
		setImageDescriptor(JSmoothResources.getDescriptor(key));
	}
	
	private void createButton(Composite parent, String label) {
		Button button = new Button(parent, SWT.PUSH);
		button.setSize(Util.computeWidth(button, label), SWT.DEFAULT);
		button.setText(label);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.widthHint = button.getSize().x;
		button.setLayoutData(layoutData);
	}

	private void createButtons(Composite parent) {
		createButton(parent, STRING_ADD);
		createButton(parent, STRING_EDIT);
		createButton(parent, STRING_REMOVE);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		
		createWidgets(composite);
		
		setControl(composite);
	}

	private void createWidgets(Composite parent) {
		List list = new List(parent, SWT.BORDER);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		list.setLayoutData(layoutData);

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		layoutData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		composite.setLayoutData(layoutData);

		createButtons(composite);
	}

}
