/*
 * Created on Nov 19, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.io.File;
import java.io.IOException;

import net.charabia.jsmoothgen.application.JSmoothModelBean;
import net.charabia.jsmoothgen.application.JSmoothModelPersistency;
import net.charabia.jsmoothgen.application.gui.MainController;
import net.charabia.jsmoothgen.skeleton.SkeletonList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Dumon
 */
public class MainWindow extends ApplicationWindow implements MainController {
	private static final String FILE_ITEM = "File";
	private static final String PROJECT_ITEM = "Project";

	private File m_projectFile;
	private SkeletonList m_skeletonList;

	public MainWindow() {
		super(null);
		setBlockOnOpen(true);
		addMenuBar();
	}

	public static void main(String[] args) {
		new MainWindow().open();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.ApplicationWindow#createMenuManager()
	 */
	protected MenuManager createMenuManager() {
		MenuManager mainMenu = new MenuManager();

		MenuManager fileMenu = new MenuManager(FILE_ITEM);
		fileMenu.add(new NewProjectAction());
		fileMenu.add(new OpenProjectAction());
		mainMenu.add(fileMenu);

		MenuManager projectMenu = new MenuManager(PROJECT_ITEM);
		mainMenu.add(projectMenu);

		return mainMenu;
	}

	/* (non-Javadoc)
	 * @see net.charabia.jsmoothgen.application.gui.MainController#getSkeletonList()
	 */
	public SkeletonList getSkeletonList() {
		return m_skeletonList;
	}

	/* (non-Javadoc)
	 * @see net.charabia.jsmoothgen.application.gui.MainController#setStateText(java.lang.String)
	 */
	public void setStateText(String text) {
	}

	class OpenProjectAction extends Action {
		private static final String NAME = "Open...";

		public OpenProjectAction() {
			super(NAME);
		}

		public void run() {
			Shell wShell = Display.getCurrent().getActiveShell();
			FileDialog fileDialog =
				new FileDialog(wShell, SWT.OPEN);
			String[] extensions = new String[] { "*.jsmooth" };
			fileDialog.setFilterExtensions(extensions);
			String sFile = fileDialog.open();
			if (sFile == null) {
				return;
			}
			m_projectFile = new File(sFile);

			JSmoothModelBean model = null;
			try {
				model =
					JSmoothModelPersistency.load(
						m_projectFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class NewProjectAction extends Action {
		private static final String NAME = "New";

		public NewProjectAction() {
			super(NAME);
		}

		public void run() {
			m_projectFile = null;
		}
	}

}
