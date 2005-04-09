/*
 * Created on May 13, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public abstract class JSmoothPage {
    private Control control;
    private JSmoothApplication js;
    private Set modifyListeners;
    private String toolTip;
    private Image image;
    
    public JSmoothPage(JSmoothApplication js) {
        this.js = js;
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

    protected JSmoothApplication getJSmoothWindow() {
        return js;
    }

    protected Shell getShell() {
        return js.getShell();
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
    
    public abstract boolean apply();
    
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }
    
    public String getToolTip() {
        return toolTip;
    }
    
    public String getId() {
        return getClass().toString();
    }
    
    public Image getImage() {
        return image;
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    protected JSmoothApplication getApplication() {
        return js;
    }
}