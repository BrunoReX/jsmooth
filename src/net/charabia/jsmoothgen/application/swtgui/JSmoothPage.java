/*
 * Created on May 13, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public abstract class JSmoothPage {
    
    private Control control;

    private JSmoothWindow jsmoothWnd;
    
    private Set modifyListeners;

    private JSmoothApplication jsmoothApp;
    
    private String toolTip;

    private ImageDescriptor image;
    
    public JSmoothPage(JSmoothWindow jsmoothWnd, JSmoothApplication jsmoothApp) {
        this.jsmoothWnd = jsmoothWnd;
        this.jsmoothApp = jsmoothApp;
    }
    
    public Control createControl(Composite parent) {
        return control= createPageArea(parent);
    }
    
    public abstract Control createPageArea(Composite parent);

    public Control getControl() {
        return control;
    }

    protected void setControl(Control cmp) {
        control = cmp;
    }

    protected JSmoothWindow getJSmoothWindow() {
        return jsmoothWnd;
    }

    protected Shell getShell() {
        return jsmoothWnd.getShell();
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
    
    protected JSmoothApplication getApplication() {
        return jsmoothApp;
    }
    
    public abstract boolean apply();
    
    public void setImageDescriptor(ImageDescriptor image) {
        this.image = image;
    }
    
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }
    
    public ImageDescriptor getImageDescriptor() {
        return image;
    }
    
    public String getToolTip() {
        return toolTip;
    }
    
    public String getId() {
        return getClass().toString();
    }
    
}