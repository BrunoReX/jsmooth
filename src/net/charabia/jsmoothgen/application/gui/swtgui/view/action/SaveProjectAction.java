/*
 * Created on Jun 19, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.view.action;

import java.util.Observable;

import net.charabia.jsmoothgen.application.swtgui.model.JSmoothApplication;
import net.charabia.jsmoothgen.application.swtgui.view.JSmoothWindow;
import net.charabia.jsmoothgen.application.swtgui.view.resources.JSmoothResources;


public class SaveProjectAction extends JSmoothAction {

        public SaveProjectAction(JSmoothWindow jsWnd, JSmoothApplication jsApp) {
                super(jsWnd, jsApp);
                
                setId(JSmoothAction.ACTION_SAVE_PROJECT);
                setText("Save");
                setImageDescriptor(getDescriptor(JSmoothResources.IMG_ECLIPSE_16));
        }
        
        public void run() {
                getJSmoothApplication().saveProject();
        }

        public void update(Observable o, Object arg) {
                setEnabled(getJSmoothApplication().isDirty() &&
                           getJSmoothApplication().hasProjectFile());
        }

}
