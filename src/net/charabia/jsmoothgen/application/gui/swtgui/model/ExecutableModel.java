/*
 * Created on Jun 11, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.model;

import java.util.Observable;


public class ExecutableModel extends Observable {
        
        private JSmoothApplication jsthMdl;
        
	public ExecutableModel(JSmoothApplication jsthMdl) {
                this.jsthMdl = jsthMdl;
	}

	public String getExecutableName() {
		return "[Exec Name]";
	}

	public String getCurrentDirectory() {
		return "[Curr. Dir]";
	}

	public String getIconFile() {
		return null;
	}
        
}
