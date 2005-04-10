/*
 * Created on Jun 19, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;



public class SaveProjectAction extends JSmoothAction {
    private JSmoothApplication js;
    
    public SaveProjectAction(JSmoothApplication js) {
        super(js);
    }

    public void run() {
        if (getApplication().hasProjectFile()) {
            getApplication().saveProject();
        }
        else {
            getApplication().ACTION_SAVE_AS.run();
        }
    }
}