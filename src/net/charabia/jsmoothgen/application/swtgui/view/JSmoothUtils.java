/*
 * Created on Jan 26, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.view;

import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;

/**
 * @author Dumon
 */
public class JSmoothUtils {

	private static final int BUTTON_EXTRA_WIDTH = 6;
	
	private static int convertWidth(Control control, int chars) {
		GC gc = new GC(control);
		gc.setFont(control.getFont());
		FontMetrics fontMetrics = gc.getFontMetrics();
		gc.dispose();
		return fontMetrics.getAverageCharWidth() * chars;
	}
	
	public static int computeWidth(Control control, String string) {
		int stringWidth = convertWidth(control, string.length());
		int extraWidth = convertWidth(control, BUTTON_EXTRA_WIDTH);
		return stringWidth + extraWidth;
	}
}
