/*
 * Created on May 13, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Observer;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public abstract class JSmoothPage implements Observer {

    private Composite cmpControl;

    private JSmoothWindow jsWnd;
    
    private Set modifyListeners;
    
    public JSmoothPage(JSmoothWindow jsWnd) {
        this.jsWnd = jsWnd;
    }

    public abstract void createControl(Composite parent);

    public Composite getControl() {
        return cmpControl;
    }

    protected void setControl(Composite cmp) {
        cmpControl = cmp;
    }

    protected JSmoothWindow getJSmoothWindow() {
        return jsWnd;
    }

    protected Shell getShell() {
        return jsWnd.getShell();
    }
    
    public void addPageModifyListener(PageModifyListener modifyListener) {
        if (modifyListeners == null) {
            modifyListeners = new HashSet();
        }
        modifyListeners.add(modifyListener);
    }
    
    protected void firePageModified() {
        for (Iterator it = modifyListeners.iterator(); it.hasNext(); ) {
            ((PageModifyListener) it.next()).pageModified();
        }
    }
    
}