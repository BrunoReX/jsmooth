/*
 * Created on Jun 19, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;


public class CompileAction extends JSmoothAction {
    private JSmoothApplication js;
    
    public CompileAction(JSmoothApplication js) {
        super(js);
    }
    
    public boolean run() {
        System.out.println("[DEBUG] Compiling, stand by...");
        JSmoothApplication app = getApplication();
        boolean hasproject = app.hasProjectFile();
        if (!hasproject) {
            app.showConsoleMessage("Cannot compile without a project file. Please save the project or load a new one, then try again.");
            return false;
        }
        app.showConsoleMessage("Compiling...");
        return app.compileProject();
    }
}