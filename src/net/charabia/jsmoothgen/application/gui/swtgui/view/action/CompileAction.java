/*
 * Created on Jun 19, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.view.action;

import java.util.Observable;

import net.charabia.jsmoothgen.application.swtgui.model.JSmoothApplication;
import net.charabia.jsmoothgen.application.swtgui.view.JSmoothWindow;
import net.charabia.jsmoothgen.application.swtgui.view.resources.JSmoothResources;


public class CompileAction extends JSmoothAction {

        public CompileAction(JSmoothWindow jsWnd, JSmoothApplication jsApp) {
                super(jsWnd, jsApp);
                
                setId(ACTION_COMPILE);
                setText("Compile...");
                setImageDescriptor(getDescriptor(JSmoothResources.IMG_ECLIPSE_16));
        }

        public void update(Observable o, Object arg) {
                setEnabled(getJSmoothApplication().hasProjectFile());
        }

}
