/*
 * Created on Jun 19, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Observable;

import net.charabia.jsmoothgen.application.swtgui.CompilationException;
import net.charabia.jsmoothgen.application.swtgui.JSmoothApplication;
import net.charabia.jsmoothgen.application.swtgui.JSmoothWindow;
import net.charabia.jsmoothgen.application.swtgui.resources.JSmoothResources;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class CompileAction extends JSmoothAction {
    
    class CompileOperation implements IRunnableWithProgress {

        /* (non-Javadoc)
         * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
         */
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
            monitor.beginTask("Compiling...", IProgressMonitor.UNKNOWN);
            try {
                getApplication().compile();
            } catch (CompilationException ex) {
                monitor.done();
                throw new InvocationTargetException(ex);
            }
            monitor.done();
        }
        
    }
    
    class CompilationErrorsDialog extends Dialog {
        
        private String[] errors;
        
        /**
         * @param parentShell
         */
        protected CompilationErrorsDialog(Shell parentShell, String[] errors) {
            super(parentShell);
            this.errors = errors;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
         */
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setText("Compilation Errors");
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
         */
        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite) super.createDialogArea(parent);
            new Label(composite, SWT.NONE).setText("The compilation failed.");
            new Label(composite, SWT.NONE).setText("Errors encountered:");
            Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
            separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            List errorList = new List(composite, SWT.READ_ONLY);
            GridData errorListData = new GridData(GridData.FILL_BOTH);
            errorListData.widthHint = 200;
            errorListData.heightHint = errorList.getItemHeight() * 3;
            errorList.setLayoutData(errorListData);
            errorList.setItems(errors);
            return composite;
        }
        
    }
    
    public CompileAction(JSmoothWindow jsmoothWnd, JSmoothApplication jsmoothApp) {
        super(jsmoothWnd, jsmoothApp);
        setId(ACTION_COMPILE);
        setText("Compile...");
        setImageDescriptor(getDescriptor(JSmoothResources.IMG_ECLIPSE_16));
    }

    public void update(Observable o, Object arg) {
        setEnabled(!getApplication().isDirty()
                && getApplication().hasProjectFile());
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        try {
            new ProgressMonitorDialog(getWindow().getShell()).run(true, false, new CompileOperation());
        } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof CompilationException) {
                CompilationException compilationEx = (CompilationException) ex.getCause();
                new CompilationErrorsDialog(getWindow().getShell(), compilationEx.getMessages());
            } else {
                throw new RuntimeException(ex.getCause());
            }
        } catch (InterruptedException ex) {
            System.out.println("The compile process interrupted");
            // Well, maybe we should be a little bit softer on that...
            throw new RuntimeException(ex);
        }
    }
    
}