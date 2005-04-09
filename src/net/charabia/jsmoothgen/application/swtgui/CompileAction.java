/*
 * Created on Jun 19, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.util.Observable;

public class CompileAction implements JSmoothAction {
    private JSmoothApplication js;
    
    public CompileAction(JSmoothApplication js) {
        this.js = js;
    }
    
    public void update(Observable o, Object arg) {
//        setEnabled(!getApplication().isDirty()
//                && getApplication().hasProjectFile());
    }
    
    public void run() {
        System.out.println("Run Compile !");
//        try {
//            new ProgressMonitorDialog(getWindow().getShell()).run(true, false, new CompileOperation());
//        } catch (InvocationTargetException ex) {
//            if (ex.getCause() instanceof CompilationException) {
//                CompilationException compilationEx = (CompilationException) ex.getCause();
//                new CompilationErrorsDialog(getWindow().getShell(), compilationEx.getMessages());
//            } else {
//                throw new RuntimeException(ex.getCause());
//            }
//        } catch (InterruptedException ex) {
//            System.out.println("The compile process interrupted");
//            // Well, maybe we should be a little bit softer about this...
//            throw new RuntimeException(ex);
//        }
    }
    
//    class CompileOperation implements IRunnableWithProgress {
//        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
//            monitor.beginTask("Compiling...", IProgressMonitor.UNKNOWN);
//            try {
//                getApplication().compile();
//            } catch (CompilationException ex) {
//                monitor.done();
//                throw new InvocationTargetException(ex);
//            }
//            monitor.done();
//        }
//    }
    
//    class CompilationErrorsDialog extends Dialog {
//        
//        private String[] errors;
//        
//        protected CompilationErrorsDialog(Shell parentShell, String[] errors) {
//            super(parentShell);
//            this.errors = errors;
//        }
//        
//        protected void configureShell(Shell newShell) {
//            super.configureShell(newShell);
//            newShell.setText("Compilation Errors");
//        }
//        
//        protected Control createDialogArea(Composite parent) {
//            Composite composite = (Composite) super.createDialogArea(parent);
//            new Label(composite, SWT.NONE).setText("The compilation failed.");
//            new Label(composite, SWT.NONE).setText("Errors encountered:");
//            Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
//            separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//            List errorList = new List(composite, SWT.READ_ONLY);
//            GridData errorListData = new GridData(GridData.FILL_BOTH);
//            errorListData.widthHint = 200;
//            errorListData.heightHint = errorList.getItemHeight() * 3;
//            errorList.setLayoutData(errorListData);
//            errorList.setItems(errors);
//            return composite;
//        }
//        
//    }
}