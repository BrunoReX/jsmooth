/*
 * Created on Jun 12, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.action;

import net.charabia.jsmoothgen.application.swtgui.JSmoothApplication;
import net.charabia.jsmoothgen.application.swtgui.JSmoothWindow;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public abstract class JSmoothAction extends Action {

    public static final String ACTION_SHOWPAGE_JAVA_APP = "Action.ShowPage.Java_Application";

    public static final String ACTION_SHOWPAGE_WELCOME = "Action.ShowPage.Welcome";

    public static final String ID_ACTION_SHOWPAGE_EXE = "Action.ShowPage.Executable";

    public static final String ACTION_SHOWPAGE_SKELETON = "Action.ShowPage.Skeleton";

    public static final String ACTION_NEW_PROJECT = "Action.NewProject";

    public static final String ACTION_COMPILE = "Action.Compile_Project";

    public static final String ACTION_SAVE_PROJECT = "Action.Save_Project";

    private JSmoothWindow jsmoothWnd;

    private JSmoothApplication jsmoothApp;

    public JSmoothAction(JSmoothWindow jsmoothWnd, JSmoothApplication jsmoothApp) {
        this.jsmoothWnd = jsmoothWnd;
        this.jsmoothApp = jsmoothApp;
    }

    protected JSmoothApplication getApplication() {
        return jsmoothApp;
    }

    protected JSmoothWindow getWindow() {
        return jsmoothWnd;
    }

    protected ImageDescriptor getDescriptor(String key) {
        return jsmoothWnd.getResources().getDescriptor(key);
    }

}