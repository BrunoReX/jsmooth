/*
 * Created on Nov 28, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Dumon
 */
public class ProjectFileWizardPage extends WizardPage {

	private static final String STR_BROWSE = "Browse...";
	private static final String STR_DESCRIPTION_MESSAGE = "Create a new JSmooth project.";

	private Text txtProjectFile;
	private Button btnBrowse;

	public ProjectFileWizardPage() {
		super("wizard.project_file");
		setTitle("JSmooth Project");
		setMessage(STR_DESCRIPTION_MESSAGE);
		String key = JSmoothResources.IMG_NEW_PROJECT_WIZBAN;
		setImageDescriptor(JSmoothResources.getDescriptor(key));
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		composite.setLayout(layout);

		Label label = new Label(composite, SWT.NONE);
		label.setText("Project file:");
		GridData layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);

		txtProjectFile = new Text(composite, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		txtProjectFile.setLayoutData(layoutData);
		txtProjectFile.addModifyListener(new ModifyListener() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
			 */
			public void modifyText(ModifyEvent e) {
				projectFileModified();
			}
		});

		btnBrowse = new Button(composite, SWT.NONE);
		btnBrowse.setSize(Util.computeWidth(btnBrowse, STR_BROWSE), SWT.DEFAULT);
		btnBrowse.setText(STR_BROWSE);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = btnBrowse.getSize().x;
		btnBrowse.setLayoutData(layoutData);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				browseSelected();
			}
		});
		
		setPageComplete(!isFileEmpty());
		
		setControl(composite);
	}

	private void projectFileModified() {
		setPageComplete(!isFileEmpty());
		setNewProjectWizardMessage();
	}

	private void browseSelected() {
		FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
		String file = dialog.open();
		if(file != null) {
			txtProjectFile.setText(file);
		}
	}
	
	private boolean isFileEmpty() {
		String fileText = txtProjectFile.getText();
		return fileText.trim().length() == 0;
	}
	
	private void setNewProjectWizardMessage() {
		if (isFileEmpty()) {
			setMessage("File name must be specified.");
		} else {
			setMessage(STR_DESCRIPTION_MESSAGE);
		}
	}
	
}
