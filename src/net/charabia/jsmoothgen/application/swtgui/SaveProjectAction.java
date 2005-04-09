/*
 * Created on Jun 19, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.util.Observable;

public class SaveProjectAction implements JSmoothAction {
    private JSmoothApplication js;
    
    public SaveProjectAction(JSmoothApplication js) {
        this.js = js;
    }

    public void run() {
//        getApplication().saveProject();
    }

    public void update(Observable o, Object arg) {
//        setEnabled(getApplication().isDirty()
//                && getApplication().hasProjectFile());
    }

}