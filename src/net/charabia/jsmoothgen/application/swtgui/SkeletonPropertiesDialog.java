/*
 * Created on May 15, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import net.charabia.jsmoothgen.application.swtgui.model.SkeletonModel;
import net.charabia.jsmoothgen.skeleton.SkeletonProperty;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class SkeletonPropertiesDialog extends Dialog implements Observer {

    private Text txt;

    private Button chkPressKey;

    private List controls = new Vector();

    private JSmoothPage jsPage;

    private SkeletonModel skelMdl;

    public SkeletonPropertiesDialog(JSmoothPage jsPage, SkeletonModel skelMdl) {
        super(jsPage.getShell());
        this.skelMdl = skelMdl;
    }

    protected Control createDialogArea(Composite parent) {
        Composite cmpDlgArea = new Composite(parent, SWT.NONE);
        cmpDlgArea.setLayout(new GridLayout());

        SkeletonProperty[] props = skelMdl.getProperties();

        for (int i = 0; i < props.length; i++) {
            Control c = createPropertyControl(cmpDlgArea, props[i]);
            c.setData(props[i]);
            controls.add(c);
        }

        return cmpDlgArea;
    }

    private Control createPropertyControl(Composite wParent,
            SkeletonProperty prop) {

        switch (skelMdl.getPropertyType(prop)) {
        case SkeletonModel.TYPE_TEXT_SINGLE:
            // The group wrapping the text field
            //{{
            Group grp = new Group(wParent, SWT.NONE);
            GridData layData = new GridData(GridData.FILL);
            layData.widthHint = 400;
            grp.setLayoutData(layData);
            grp.setLayout(new GridLayout());
            grp.setText(skelMdl.getPropertyLabel(prop));
            //}}

            // The actual text field
            txt = new Text(grp, SWT.SINGLE | SWT.BORDER);
            layData = new GridData(GridData.FILL_BOTH);
            txt.setLayoutData(layData);
            txt.setText(skelMdl.getPropertyValue(prop));

            return txt;

        case SkeletonModel.TYPE_TEXT_MULTI:
            // The group wrapping the text field
            //{{
            grp = new Group(wParent, SWT.NONE);
            layData = new GridData(GridData.FILL);
            layData.widthHint = 400;
            layData.heightHint = 100;
            grp.setLayoutData(layData);
            grp.setLayout(new GridLayout());
            grp.setText(skelMdl.getPropertyLabel(prop));
            //}}

            // The actual text field
            txt = new Text(grp, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL
                    | SWT.V_SCROLL);
            layData = new GridData(GridData.FILL_BOTH);
            txt.setLayoutData(layData);
            txt.setText(skelMdl.getPropertyValue(prop));

            return txt;

        case SkeletonModel.TYPE_CHECK:
            Button chk = new Button(wParent, SWT.CHECK);
            chk.setText(skelMdl.getPropertyLabel(prop));
            chk.setSelection("1".equals(skelMdl.getPropertyValue(prop)));

            return chk;

        default:
            return null;
        }
    }

    protected void okPressed() {
        Iterator it = controls.iterator();

        while (it.hasNext()) {
            Control ctrl = (Control) it.next();
            SkeletonProperty prop = (SkeletonProperty) ctrl.getData();
            switch (skelMdl.getPropertyType(prop)) {
            case SkeletonModel.TYPE_TEXT_SINGLE:
                String value = ((Text) ctrl).getText();
                skelMdl.setPropertyValue(prop, value); // TODO

                break;

            case SkeletonModel.TYPE_TEXT_MULTI:
                value = ((Text) ctrl).getText();
                skelMdl.setPropertyValue(prop, value); // TODO

                break;

            case SkeletonModel.TYPE_CHECK:
                boolean b = ((Button) ctrl).getSelection();
                value = (b == true) ? "1" : "0";
                skelMdl.setPropertyValue(prop, value); // TODO

                break;
            }
        }

        super.okPressed();
    }

    public void update(Observable o, Object arg) {
        // TODO
    }

}