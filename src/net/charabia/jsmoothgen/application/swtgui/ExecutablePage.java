/*
 * Created on May 19, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.util.Observable;
import java.util.Observer;

import net.charabia.jsmoothgen.application.swtgui.model.ExecutableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ExecutablePage extends JSmoothPage implements Observer {

    private ExecutableModel execMdl;

    private Text txtExecName;

    private Text txtCurrDir;

    private Label lblIcon;

    public ExecutablePage(JSmoothWindow jsWnd) {
        super(jsWnd);
    }

    public void createControl(Composite parent) {
        Composite cmpContents = new Composite(parent, SWT.NONE);
        cmpContents.setLayout(new GridLayout(3, false));
        setControl(cmpContents);

        // Executable name
        //{{
        Label label = new Label(cmpContents, SWT.NONE);
        label.setText("Executable name:");
        GridData layoutData = new GridData(GridData.FILL);
        label.setLayoutData(layoutData);

        txtExecName = new Text(cmpContents, SWT.BORDER);
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.widthHint = 250;
        txtExecName.setLayoutData(layoutData);

        Button button = new Button(cmpContents, SWT.NONE);
        button.setSize(JSmoothUtils.computeWidth(button, "Browse..."),
                SWT.DEFAULT);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
                dialog.setText("Executable Name");
                String file = dialog.open();
                if (file != null) {
                    // txtExecName.setText(file);
                    // adapter.setExecutableFile(file);
                }
            }
        });
        layoutData = new GridData(GridData.FILL);
        layoutData.widthHint = button.getSize().x;
        button.setLayoutData(layoutData);
        //}}

        // Current directory
        //{{
        label = new Label(cmpContents, SWT.NONE);
        label.setText("Current directory:");
        layoutData = new GridData(GridData.FILL);
        label.setLayoutData(layoutData);

        txtCurrDir = new Text(cmpContents, SWT.BORDER);
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.widthHint = 250;
        txtCurrDir.setLayoutData(layoutData);

        button = new Button(cmpContents, SWT.NONE);
        button.setSize(JSmoothUtils.computeWidth(button, "Browse..."),
                SWT.DEFAULT);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(getShell(),
                        SWT.SAVE);
                dialog.setText("Current Directory");
                String dir = dialog.open();
                if (dir != null) {
                    txtCurrDir.setText(dir);
                }
            }
        });
        layoutData = new GridData(GridData.FILL);
        layoutData.widthHint = button.getSize().x;
        button.setLayoutData(layoutData);
        //}}

        label = new Label(cmpContents, SWT.NONE);
        label.setText("Executable icon:");
        layoutData = new GridData(GridData.FILL);
        label.setLayoutData(layoutData);

        Composite cmpIcon = new Composite(cmpContents, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        cmpIcon.setLayout(layout);
        layoutData = new GridData(GridData.FILL);
        cmpIcon.setLayoutData(layoutData);

        lblIcon = new Label(cmpIcon, SWT.BORDER | SWT.FLAT);
        layoutData = new GridData(GridData.FILL);
        layoutData.widthHint = 32;
        layoutData.heightHint = 32;
        lblIcon.setLayoutData(layoutData);

        button = new Button(cmpIcon, SWT.NONE);
        button.setSize(JSmoothUtils.computeWidth(button, "Browse..."),
                SWT.DEFAULT);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                browseIconPressed();
            }
        });
        layoutData = new GridData(GridData.FILL);
        layoutData.widthHint = button.getSize().x;
        button.setLayoutData(layoutData);
    }

    private void browseIconPressed() {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setText("Icon File");
        String file = dialog.open();

        // Means "CANCEL"
        if (file == null)
            return;

        if (setIconImage(file)) {
            // adapter.setExecutableFile(file); TODO
        }
    }

    private boolean setIconImage(String file) {
        if (file == null) {
            // Clear the label's image
            lblIcon.setImage(null);
            return true;
        }

        Image img = null;
        try {
            img = new Image(getControl().getDisplay(), file);
        } catch (SWTException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        }

        getJSmoothWindow().getResources().doRegister(img); // Disposed of
                                                           // automagically
        lblIcon.setImage(img);

        return true;
    }

    public void update(Observable o, Object arg) {
        txtExecName.setText(execMdl.getExecutableName());
        txtCurrDir.setText(execMdl.getCurrentDirectory());
        setIconImage(execMdl.getIconFile());
    }

}