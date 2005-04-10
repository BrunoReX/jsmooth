/*
 * Created on Jun 19, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;


public class OpenAction extends JSmoothAction {
    
    public OpenAction(JSmoothApplication js) {
        super(js);
    }

    public void run() {
        FileDialog dialog = new FileDialog(getApplication().getShell(), SWT.OPEN);
        dialog.setText("Open Project");
        String file = dialog.open();
        if (file != null) getApplication().openProject(file);
    }
}