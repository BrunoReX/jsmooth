/*
 * Created on Nov 19, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.io.File;

import net.charabia.jsmoothgen.application.JSmoothModelBean;
import net.charabia.jsmoothgen.skeleton.SkeletonList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;

/**
 * @author Dumon
 */
public class MainWindow extends ApplicationWindow {
	private static final String STR_FILE_ITEM = "File";
	private static final String STR_PROJECT_ITEM = "Project";
	private static final String STR_NEW_PROJECT = "New Project...";
	private static final String STR_TOOLTIP_NEW_PROJECT = "New Project";
	
	private static final String DIR_SKELETON = "skeletons";
	
	private Action actionNewProject = new NewProjectAction();
	
	private SkeletonList skeletonList;
	private JSmoothModelBean model;
	private File projectFile;
	
	public MainWindow() {
		super(null);
		setBlockOnOpen(true);
		addMenuBar();
		addToolBar(SWT.FLAT | SWT.NO_FOCUS);
		
		skeletonList = new SkeletonList(new File(DIR_SKELETON));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.ApplicationWindow#createMenuManager()
	 */
	protected MenuManager createMenuManager() {
		MenuManager mainMenu = super.createMenuManager();

		MenuManager fileMenu = new MenuManager(STR_FILE_ITEM);
		fileMenu.add(actionNewProject);
		mainMenu.add(fileMenu);

		return mainMenu;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.ApplicationWindow#createToolBarManager(int)
	 */
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolbar = super.createToolBarManager(style);
		toolbar.add(actionNewProject);
		return toolbar;
	}

	class NewProjectAction extends Action {
		public NewProjectAction() {
			super(STR_NEW_PROJECT);
			String key = JSmoothResources.IMG_NEW_PROJECT;
			setImageDescriptor(JSmoothResources.getDescriptor(key));
			setToolTipText(STR_TOOLTIP_NEW_PROJECT);
		}

		public void run() {
			WizardDialog wizard = new WizardDialog(getShell(), new NewProjectWizard());
			wizard.open();
		}
	}

}
