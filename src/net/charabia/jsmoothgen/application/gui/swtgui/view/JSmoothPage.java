/*
 * Created on May 13, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.view;

import java.util.Observer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;


public abstract class JSmoothPage implements Observer {
	
        private Composite cmpControl;
        private JSmoothWindow jsWnd;
        
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
        
        public abstract void commit();
        
}
