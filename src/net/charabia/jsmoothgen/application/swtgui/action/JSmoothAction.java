/*
 * Created on Jun 12, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.action;

import java.util.Observer;

import net.charabia.jsmoothgen.application.swtgui.JSmoothApplication;
import net.charabia.jsmoothgen.application.swtgui.JSmoothWindow;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public abstract class JSmoothAction extends Action implements Observer {

    public static final String ACTION_SHOWPAGE_JAVA_APP = "Action.ShowPage.Java_Application";

    public static final String ACTION_SHOWPAGE_WELCOME = "Action.ShowPage.Welcome";

    public static final String ID_ACTION_SHOWPAGE_EXE = "Action.ShowPage.Executable";

    public static final String ACTION_SHOWPAGE_SKELETON = "Action.ShowPage.Skeleton";

    public static final String ACTION_NEW_PROJECT = "Action.NewProject";

    public static final String ACTION_COMPILE = "Action.Compile_Project";

    public static final String ACTION_SAVE_PROJECT = "Action.Save_Project";

    private JSmoothWindow jsWnd;

    private JSmoothApplication jsApp;

    public JSmoothAction(JSmoothWindow jsWnd, JSmoothApplication jsApp) {
        this.jsWnd = jsWnd;
        (this.jsApp = jsApp).addObserver(this);
    }

    protected JSmoothApplication getApplication() {
        return jsApp;
    }

    protected JSmoothWindow getWindow() {
        return jsWnd;
    }

    protected ImageDescriptor getDescriptor(String key) {
        return jsWnd.getResources().getDescriptor(key);
    }

}