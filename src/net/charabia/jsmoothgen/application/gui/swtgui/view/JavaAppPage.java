/*
 * Created on May 22, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.view;

import java.util.Observable;

import net.charabia.jsmoothgen.application.swtgui.model.JSmoothApplication;
import net.charabia.jsmoothgen.application.swtgui.model.UpdateMessage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class JavaAppPage extends JSmoothPage {
	
        private JSmoothApplication jsApp;
        private Text txtJarLoc;
	private Text txtMainCls;
	private Text txtArgs;
	private List lstClspath;
	private Button btnAddJar;
	private Button btnAddFolder;
	private Button btnRemove;
	
        public JavaAppPage(JSmoothWindow jsWnd, JSmoothApplication jsApp) {
                super(jsWnd);
                this.jsApp = jsApp;
        }

	public void createControl(Composite parent) {
		Composite cmpContents = new Composite(parent, SWT.NONE);
		cmpContents.setLayout(new GridLayout());
		setControl(cmpContents);
		
		Composite cmpJavaCmd = new Composite(cmpContents, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		cmpJavaCmd.setLayout(layout);
		cmpJavaCmd.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Jar location
		Label label = new Label(cmpJavaCmd, SWT.NONE);
		label.setText("Jar location:");		
		GridData layData = new GridData(GridData.FILL);
		label.setLayoutData(layData);
		
		txtJarLoc = new Text(cmpJavaCmd, SWT.BORDER);
		layData = new GridData(GridData.FILL_HORIZONTAL);
		layData.widthHint = 250;
		txtJarLoc.setLayoutData(layData);
                txtJarLoc.addFocusListener(new FocusAdapter() {
                        
                });
                
		Button button = new Button(cmpJavaCmd, SWT.NONE);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
				dialog.setText("Jar Location");
				String file = dialog.open();
				if(file != null) {
					txtJarLoc.setText(file);
				}
			}
		});
		layData = new GridData(GridData.FILL);
		layData.widthHint = JSmoothUtils.computeWidth(button, "Browse...");
		button.setLayoutData(layData);
		//
		
		// Main class
		label = new Label(cmpJavaCmd, SWT.NONE);
		label.setText("Main class:");		
		layData = new GridData(GridData.FILL);
		label.setLayoutData(layData);
		
		txtMainCls = new Text(cmpJavaCmd, SWT.BORDER);
		layData = new GridData(GridData.FILL_HORIZONTAL);
		layData.widthHint = 250;
		txtMainCls.setLayoutData(layData);
		txtMainCls.addModifyListener(new ModifyListener() {
                        public void modifyText(ModifyEvent e) {
                                jsApp.setMainClass(txtMainCls.getText());
                        }
                });
                
		new Label(cmpJavaCmd, SWT.NONE); // empty cell
		//
		
		// Arguments
		label = new Label(cmpJavaCmd, SWT.NONE);
		label.setText("Arguments:");		
		layData = new GridData(GridData.FILL);
		label.setLayoutData(layData);
		
		txtArgs = new Text(cmpJavaCmd, SWT.BORDER);
		layData = new GridData(GridData.FILL_HORIZONTAL);
		layData.widthHint = 250;
		txtArgs.setLayoutData(layData);
		
		new Label(cmpJavaCmd, SWT.NONE); // empty cell
		//
		
		// Classpath list
		Group grpClspath = new Group(cmpContents, SWT.NONE);
		grpClspath.setText("Classpath");
		grpClspath.setLayout(new GridLayout());
		layData = new GridData(GridData.FILL_HORIZONTAL);
		layData.horizontalSpan = 3;
		grpClspath.setLayoutData(layData);
		grpClspath.setLayout(new GridLayout(2, false));
		
		lstClspath = new List(grpClspath, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		layData = new GridData(GridData.FILL_BOTH);
		layData.widthHint = 300; // TODO hard coded
		layData.heightHint = lstClspath.getItemHeight() * 10; // TODO hard coded
		lstClspath.setLayoutData(layData);
		lstClspath.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateRemoveButton();
			}
		});
		//
		
		// The classpath Button bar
		Composite cmpButtonBar = new Composite(grpClspath, SWT.NONE);
		cmpButtonBar.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 2;
		cmpButtonBar.setLayout(layout);
		
		btnAddJar = new Button(cmpButtonBar, SWT.NONE);
		btnAddJar.setText("Add JAR File...");
		btnAddJar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				dialog.setText("JAR File");
				String file = dialog.open();
				if(file != null) {
					addClasspathItem(file);
				}
			}
		});
		layData = new GridData(GridData.FILL_HORIZONTAL);
		layData.widthHint = JSmoothUtils.computeWidth(btnAddJar, "Add JAR File...");
		btnAddJar.setLayoutData(layData);
		
		btnAddFolder = new Button(cmpButtonBar, SWT.NONE);
		btnAddFolder.setText("Add Class Folder...");
		btnAddFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.SAVE);
				dialog.setText("Current Directory");
				String file = dialog.open();
				if(file != null) {
					addClasspathItem(file);
				}
			}
		});
		layData = new GridData(GridData.FILL_HORIZONTAL);
		layData.widthHint = JSmoothUtils.computeWidth(btnAddFolder, "Add Class Folder...");
		btnAddFolder.setLayoutData(layData);

		btnRemove = new Button(cmpButtonBar, SWT.NONE);
		btnRemove.setText("Remove");
		btnRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
                                jsApp.removeClasspathItems(lstClspath.getSelectionIndices());
			}
		});
		layData = new GridData(GridData.FILL_HORIZONTAL);
		layData.widthHint = JSmoothUtils.computeWidth(btnRemove, "Remove");
		btnRemove.setLayoutData(layData);
		//
	}
	
	private void updateRemoveButton() {
		int i = lstClspath.getItemCount();
		boolean enable = true;
		if(i == 0) {
			enable = false;
		} else {
			int s = lstClspath.getSelectionCount();
			if(s == 0) enable = false;
		}
		btnRemove.setEnabled(enable);
	}
	
	private void addClasspathItem(String item) {
                jsApp.addClasspathItem(item);
	}

        public void update(Observable o, Object arg) {
                txtJarLoc.setText(jsApp.getJarLocation());
                txtArgs.setText(jsApp.getArguments());
                
                if(arg.equals(UpdateMessage.MSGUPD_MAINCLASS_CHANGED)) {
                        String mainCls = jsApp.getMainClass();
                        if(!txtMainCls.getText().equals(mainCls)) {
                                txtMainCls.setText(mainCls);
                        }
                }
                
                if(arg.equals(UpdateMessage.MSGUPD_CLASSPATH_CHANGED)) {
                        lstClspath.setItems(jsApp.getClasspath());
                }
                
                updateRemoveButton();
        }

        public void commit() {
                
        }

}
