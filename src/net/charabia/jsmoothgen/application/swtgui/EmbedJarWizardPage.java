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
public class EmbedJarWizardPage extends WizardPage {
	private static final String JAR_WIZBAN =
		JSmoothResources.JAR_WIZBAN;
	
	public EmbedJarWizardPage() {
		super("wizard.embed_jar");
		setTitle("JAR File");
		setMessage("Embed a JAR file into the application.");
		setImageDescriptor(
			JSmoothResources.getDescriptor(JAR_WIZBAN));
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
//		comp.setBackground(
//			new Color(Display.getCurrent(), 250, 50, 50));
		
		createJarLocation(comp);
		
		setControl(comp);
	}
	
	private void createJarLocation(Composite parent) {
		GridData layoutData = null;
		
		Button check = new Button(
			parent,
			SWT.CHECK | SWT.LEFT_TO_RIGHT);
		layoutData = new GridData(GridData.FILL);
		layoutData.horizontalSpan = 3;
		check.setLayoutData(layoutData);
		check.setText("Embed JAR");
		
		Label label = new Label(parent, SWT.NONE);
		layoutData = new GridData(GridData.FILL);
		label.setText("Location:");
		
		Text text = new Text(parent, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(layoutData);
		
		Button browseButton = new Button(parent, SWT.PUSH);
		layoutData = new GridData(GridData.FILL);
		browseButton.setLayoutData(layoutData);
		browseButton.setText("Browse...");
	}
}
