/*
 * Created on May 30, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.util.Observable;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class WelcomePage extends JSmoothPage {

    public WelcomePage(JSmoothWindow jsWnd) {
        super(jsWnd);
    }

    public void createControl(Composite parent) {
        Composite cmpTop = new Composite(parent, SWT.NONE);
        cmpTop.setLayout(new GridLayout());
        setControl(cmpTop);

        Label label = new Label(getControl(), SWT.NONE);
        GridData layData = new GridData(GridData.FILL_HORIZONTAL);
        label.setLayoutData(layData);

        label = new Label(getControl(), SWT.NONE);
        label.setFont(JFaceResources.getHeaderFont());
        label.setText("Welcome to JSmooth !");
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

        label = new Label(getControl(), SWT.NONE);
        label.setText("The Java .EXE wrapper");
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
    }

    public void update(Observable o, Object arg) {
        // Do nothing
    }

}