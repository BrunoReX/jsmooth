/*
 * Created on Jun 10, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.action;

import java.util.Observable;
import java.util.Observer;

import net.charabia.jsmoothgen.application.swtgui.JSmoothPage;
import net.charabia.jsmoothgen.application.swtgui.JSmoothWindow;
import net.charabia.jsmoothgen.application.swtgui.model.JSmoothApplication;
import net.charabia.jsmoothgen.application.swtgui.resources.JSmoothResources;

import org.eclipse.core.internal.runtime.Assert;

public class ShowPageAction extends JSmoothAction implements Observer {

    private JSmoothPage jsPage;

    public ShowPageAction(JSmoothWindow jsthWnd, JSmoothApplication jsthApp) {
        super(jsthWnd, jsthApp);
        setImageDescriptor(getDescriptor(JSmoothResources.IMG_ECLIPSE_32));
    }

    public void setPage(JSmoothPage jsPage) {
        this.jsPage = jsPage;
    }

    public void run() {
        Assert.isNotNull(jsPage);
        getJSmoothWindow().showPage(jsPage);
    }

    public void update(Observable o, Object arg) {
        setEnabled(getJSmoothApplication().hasProjectFile());
    }

}