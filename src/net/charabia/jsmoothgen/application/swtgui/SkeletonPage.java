/*
 * Created on May 13, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.util.Observable;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public final class SkeletonPage extends JSmoothPage {

    // Widgets
    private Combo cmboSkelSelec;

    private Button chkDebugWrapper;

    private Dialog skelProps;

    private JSmoothApplication jsApp;

    private SkeletonModel skelMdl;

    public SkeletonPage(JSmoothWindow jsWnd, JSmoothApplication jsApp) {
        super(jsWnd, jsApp);
        (this.jsApp = jsApp).addObserver(this);
        (this.skelMdl) = jsApp.getSkeletonModel();
    }

    private int getItemIndex(String[] array, String str) {
        int i = array.length - 1;
        for (; i > 0; i--) {
            if (array[i].equals(str))
                break;
        }
        return i;
    }

    public Control createPageArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(3, false));

        Label label = new Label(composite, SWT.NONE);
        label.setText("Skeleton:");

        // The Skeleton selection combo
        cmboSkelSelec = new Combo(composite, SWT.READ_ONLY);
        cmboSkelSelec.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                jsApp.setSkeletonName(cmboSkelSelec.getText());
            }
        });

        Button btnProps = new Button(composite, SWT.PUSH);
        GridData layData = new GridData(GridData.FILL);
        layData.widthHint = JSmoothUtils
                .computeButtonWidth(btnProps, "Properties...");
        btnProps.setLayoutData(layData);
        btnProps.setText("Properties...");
        btnProps.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                (new SkeletonPropertiesDialog(SkeletonPage.this, jsApp
                        .getSkeletonModel())).open();
            }
        });

        /*
         * TODO
         * 
         * chkDebugWrapper = new Button(cmpContents, SWT.CHECK); layData = new
         * GridData(GridData.FILL); layData.horizontalSpan = 3;
         * chkDebugWrapper.setLayoutData(layData);
         * chkDebugWrapper.setText("Display debug wrapper");
         */
        
        return composite;
    }

    public void update(Observable o, Object arg) {
        String[] skelNames = skelMdl.getSkeletonNames();

        cmboSkelSelec.setItems(skelNames);
        getJSmoothWindow().getShell().layout();

        // Select the right skeleton name
        if (!arg.equals(UpdateMessage.MSGUPD_NULL_PROJECT)) {
            cmboSkelSelec.select(getItemIndex(skelNames, jsApp
                    .getSkeletonName()));
        }
    }

    /* (non-Javadoc)
     * @see net.charabia.jsmoothgen.application.swtgui.JSmoothPage#apply()
     */
    public boolean apply() {
        return false;
    }

}