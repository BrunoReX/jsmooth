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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class WelcomePage extends JSmoothPage {

    public WelcomePage(JSmoothWindow jsmoothWnd, JSmoothApplication jsmoothApp) {
        super(jsmoothWnd, jsmoothApp);
    }

    public Control createPageArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());

        Label label = new Label(composite, SWT.NONE);
        GridData layData = new GridData(GridData.FILL_HORIZONTAL);
        label.setLayoutData(layData);

        label = new Label(composite, SWT.NONE);
        label.setFont(JFaceResources.getHeaderFont());
        label.setText("Welcome to JSmooth !");
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

        label = new Label(composite, SWT.NONE);
        label.setText("The Java .EXE wrapper");
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        
        return composite;
    }

    public void update(Observable o, Object arg) {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see net.charabia.jsmoothgen.application.swtgui.JSmoothPage#apply()
     */
    public boolean apply() {
        return false;
    }

}