/*
 * Created on Nov 21, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.io.File;

import net.charabia.jsmoothgen.application.JSmoothModelBean;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
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
public class JVMSelectionWizardPage extends WizardPage {
	private static final int VERSION_TEXT_WIDTH = 30;
	private static final int VERSION_TEXT_LIMIT = 5;
	
	public JVMSelectionWizardPage() {
		super("wizard.jvm_selection");
		setTitle("JVM Selection");
		setMessage("Specify parameters for the Java Virtual Machine.");
	}
	
	/* (non-Javadoc)
	 * @see net.charabia.jsmoothgen.application.gui.ModelUpdater#setModel(java.io.File, net.charabia.jsmoothgen.application.JSmoothModelBean)
	 */
	public void setModel(File basedir, JSmoothModelBean model) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.charabia.jsmoothgen.application.gui.ModelUpdater#updateModel()
	 */
	public void updateModel() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		GridLayout layout = null;
		GridData layoutData = null;
//		Label label = null;
//		Text text = null;
//		Button button = null;
		
		Composite comp = new Composite(parent, SWT.NONE);
		layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		comp.setLayout(layout);
//		comp.setBackground(
//			new Color(Display.getCurrent(), 250, 50, 50));
		
		Group jvmGroup = new Group(comp, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		jvmGroup.setLayoutData(layoutData);
		layout = new GridLayout(2, false);
		jvmGroup.setLayout(layout);
		jvmGroup.setText("JVM Version");
		
		createMinimumVersion(jvmGroup);
		createMaximumVersion(jvmGroup);
		
		Group jvmBundleGroup = new Group(comp, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		jvmBundleGroup.setLayoutData(layoutData);
		layout = new GridLayout(3, false);
		jvmBundleGroup.setLayout(layout);
		jvmBundleGroup.setText("JVM Bundle");
		
		createUseJvmBundle(jvmBundleGroup);
		
		Group jvmSearchGroup = new Group(comp, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 2;
		jvmSearchGroup.setLayoutData(layoutData);
		layout = new GridLayout(2, false);
		jvmSearchGroup.setLayout(layout);
		jvmSearchGroup.setText("JVM Search");
		
		createJVMSearch(jvmSearchGroup);
		
		setControl(comp);
	}
	
	private void createMinimumVersion(Composite parent) {
		GridData layoutData = null;
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("Min:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = VERSION_TEXT_WIDTH;
		text.setLayoutData(layoutData);
		text.setTextLimit(VERSION_TEXT_LIMIT);
	}
	
	private void createMaximumVersion(Composite parent) {
		GridData layoutData = null;
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setLayoutData(layoutData);
		label.setText("Max:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL);
		layoutData.widthHint = VERSION_TEXT_WIDTH;
		text.setLayoutData(layoutData);
		text.setTextLimit(VERSION_TEXT_LIMIT);
	}
	
	private void createUseJvmBundle(Composite parent) {
		GridData layoutData = null;
		
		Button check = new Button(
			parent,
			SWT.CHECK | SWT.LEFT_TO_RIGHT);
		layoutData = new GridData(GridData.FILL);
		layoutData.horizontalSpan = 3;
		check.setLayoutData(layoutData);
		check.setText("Use JVM Bundle");
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setText("Directory:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
		
		Button browseButton = new Button(parent, SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		browseButton.setLayoutData(layoutData);
		browseButton.setText("Browse...");
	}
	
	private void createJVMSearch(Composite parent) {
		GridData layoutData = null;
		
		List list = new List(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_BOTH);
		list.setLayoutData(layoutData);
		
		Composite jvmSearchButtonBar = new Composite(parent, SWT.NONE);
		jvmSearchButtonBar.setLayout(new GridLayout());
		layoutData = new GridData(GridData.FILL);
		jvmSearchButtonBar.setLayoutData(layoutData);
		
		Button addButton = new Button(jvmSearchButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		addButton .setLayoutData(layoutData);
		addButton .setText("Add...");
		
		Button removeButton = new Button(jvmSearchButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		removeButton .setLayoutData(layoutData);
		removeButton .setText("Remove...");
		
		Button editButton = new Button(jvmSearchButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		editButton .setLayoutData(layoutData);
		editButton .setText("Edit...");
		
		Label separatorLabel = new Label(jvmSearchButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		separatorLabel .setLayoutData(layoutData);
		
		Button upButton = new Button(jvmSearchButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		upButton.setLayoutData(layoutData);
		upButton.setText("Up");
		
		Button downButton = new Button(jvmSearchButtonBar, SWT.NONE);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		downButton.setLayoutData(layoutData);
		downButton.setText("Down");
	}
}
