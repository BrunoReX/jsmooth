/*
 * Created on Nov 22, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.io.File;
import java.util.Arrays;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;

/**
 * @author Dumon
 */
public class ClasspathWizardPage extends WizardPage {
	
	private static final String STRING_ADD_JAR = "Add JAR...";
	private static final String STRING_ADD_CLASS_FOLDER = "Add Class Folder...";
	private static final String STRING_EDIT = "Edit...";
	private static final String STRING_REMOVE = "Remove";
	
	private static final String WIDGET_ADD_BUTTON = "AddButton";
	private static final String WIDGET_BUTTON_BAR = "ButtonBar";
	private static final String WIDGET_EDIT_BUTTON = "EditButton";
	private static final String WIDGET_REMOVE_BUTTON = "RemoveButton";
	private static final String WIDGET_LIST = "List";
	private static final String WIDGET_TOP = "Top";
	
	private Button btnAddJar;
	private Button btnAddClassFolder;
	private Button btnRemove;
	private Button btnEdit;
	private List lstClasspath;
	
	public ClasspathWizardPage() {
		super("wizard.classpath");
		setTitle("Classpath");
		setMessage("The Java application classpath.");
		String key = JSmoothResources.IMG_CLASSPATH_WIZBAN;
		setImageDescriptor(JSmoothResources.getDescriptor(key));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		
		lstClasspath = new List(composite,
		                        SWT.BORDER |
		                        SWT.H_SCROLL |
		                        SWT.V_SCROLL |
		                        SWT.SINGLE);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		lstClasspath.setLayoutData(layoutData);
		
		Composite buttonBar = new Composite(composite, SWT.NONE);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttonBar.setLayout(layout);
		layoutData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		buttonBar.setLayoutData(layoutData);
		
		btnAddJar = new Button(buttonBar, SWT.PUSH);
		btnAddJar.setSize(Util.computeWidth(btnAddJar, STRING_ADD_JAR), SWT.DEFAULT);
		btnAddJar.setText(STRING_ADD_JAR);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.widthHint = btnAddJar.getSize().x;
		btnAddJar.setLayoutData(layoutData);
		btnAddJar.addSelectionListener(new SelectionAdapter(){
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				addJarButtonPressed();
			}
		});
		
		btnAddClassFolder = new Button(buttonBar, SWT.PUSH);
		btnAddClassFolder.setSize(Util.computeWidth(btnAddClassFolder, STRING_ADD_CLASS_FOLDER), SWT.DEFAULT);
		btnAddClassFolder.setText(STRING_ADD_CLASS_FOLDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.widthHint = btnAddClassFolder.getSize().x;
		btnAddClassFolder.setLayoutData(layoutData);
		btnAddClassFolder.addSelectionListener(new SelectionAdapter(){
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				addClassFolderButtonPressed();
			}
		});
		
		btnEdit = new Button(buttonBar, SWT.PUSH);
		btnEdit.setSize(Util.computeWidth(btnEdit, STRING_EDIT), SWT.DEFAULT);
		btnEdit.setText(STRING_EDIT);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.widthHint = btnEdit.getSize().x;
		btnEdit.setLayoutData(layoutData);
		btnEdit.addSelectionListener(new SelectionAdapter(){
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				editButtonButtonPressed();
			}
		});
		
		btnRemove = new Button(buttonBar, SWT.PUSH);
		btnRemove.setSize(Util.computeWidth(btnRemove, STRING_REMOVE), SWT.DEFAULT);
		btnRemove.setText(STRING_REMOVE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.widthHint = btnRemove.getSize().x;
		btnRemove.setLayoutData(layoutData);
		btnRemove.addSelectionListener(new SelectionAdapter(){
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				removeButtonPressed();
			}
		});
		
		setControl(composite);
	}
	
	/**
	 * Called when the "Edit" btton is pressed. It checks whether the
	 * selected list item is a folder or a file. Then, based on
	 * the result, the event is delegated to addJarButtonPressed() or
	 * addClassFolderPressed().
	 * Within the dialogs that pop up, the respective folder or file is
	 * selected.
	 */
	private void editButtonButtonPressed() {
		String[] items = lstClasspath.getSelection();
		
		// No selection.
		if(items.length == 0) return;
		
		// We now there can be max one item.
		// Due to the SWT.SINGLE style of the list.
		File file = new File(items[0]);
		if(file.isFile()) {
			addJarButtonPressed();
		}
		if(file.isDirectory()) {
			addClassFolderButtonPressed();
		}
	}

	/**
	 * Called when the "Remove" button is pressed.
	 * Deletes all the selected lines from the list.
	 */
	private void removeButtonPressed() {
		for (int i = 0; i < lstClasspath.getItemCount(); i++) {
			if(lstClasspath.isSelected(i)) lstClasspath.remove(i);
		} 
	}

	private void addJarButtonPressed() {
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
		String file = dialog.open();
		
		// True if the dialog _wasn't_ cancelled.
		if(file != null) {
			String[] items = lstClasspath.getItems();
			java.util.List list = Arrays.asList(items);
			if(!list.contains(file)) {
				lstClasspath.add(file);
			}
		}
	}

	private void addClassFolderButtonPressed() {
		DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.OPEN);
		String folder = dialog.open();
		
		// True if the dialog _wasn't_ cancelled.
		if(folder != null) {
			String[] items = lstClasspath.getItems();
			java.util.List list = Arrays.asList(items);
			if(!list.contains(folder)) {
				lstClasspath.add(folder);
			}
		}
	}
	
}
