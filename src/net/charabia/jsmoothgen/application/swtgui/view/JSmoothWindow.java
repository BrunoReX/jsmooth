/*
 * Created on Nov 19, 2003
 */
package net.charabia.jsmoothgen.application.swtgui.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import net.charabia.jsmoothgen.application.swtgui.model.JSmoothApplication;
import net.charabia.jsmoothgen.application.swtgui.model.UpdateMessage;
import net.charabia.jsmoothgen.application.swtgui.view.action.CompileAction;
import net.charabia.jsmoothgen.application.swtgui.view.action.JSmoothAction;
import net.charabia.jsmoothgen.application.swtgui.view.action.NewProjectAction;
import net.charabia.jsmoothgen.application.swtgui.view.action.SaveProjectAction;
import net.charabia.jsmoothgen.application.swtgui.view.action.ShowPageAction;
import net.charabia.jsmoothgen.application.swtgui.view.resources.JSmoothResources;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;

/**
 * @author Dumon
 */
public final class JSmoothWindow implements Observer {
	
	/**
	 * Special layout for the JSmooth window.
	 */
	class JSmoothLayout extends Layout {
		
		protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			
			Point result = new Point(0, 0);
			Control[] ctrls = composite.getChildren();
			
			Point p = null;
			for (int i = 0; i < ctrls.length; i++) {
				Control w = ctrls[i];
				
				if(menuSepar == w) {
					p = w.computeSize(wHint, hHint, flushCache);
					result.y +=  p.y;
				} else if(pageSwitcher == w) {
					p = w.computeSize(wHint, hHint, flushCache);
					result.x += p.x;
					result.y += p.y;
				} else if(switcherSepar == w) {
					p = w.computeSize(wHint, hHint, flushCache);
					result.x += p.x;
				} else if(cmpPageArea == w) {
					p = w.computeSize(wHint, hHint, flushCache);
					result.x += p.x;
					result.y = Math.max(result.y, p.y);
				}
			}
			
			if (wHint != SWT.DEFAULT) {
				result.x = wHint;
			}
			
			if (hHint != SWT.DEFAULT) {
				result.y = hHint;
			}
			
			return result;
		}

		protected void layout(Composite cmp, boolean flushCache) {
                        
                        // Layout the pages
                        if(flushCache) {
                                Iterator it = getPages().iterator();
                                while (it.hasNext()) {
                                        ((JSmoothPage) it.next()).getControl().layout();
                                }
                        }
                        //
                        
			Rectangle clientArea = cmp.getClientArea();
			
			Control[] wgs = cmp.getChildren();
			
			Point p = null;
			for (int i = 0; i < wgs.length; i++) {
				Control w = wgs[i];
				
				if (menuSepar == w) {
					p = w.computeSize(SWT.DEFAULT, SWT.DEFAULT, flushCache);
					w.setBounds(clientArea.x, clientArea.y, clientArea.width, p.y);
					clientArea.y += p.y;
					clientArea.height += p.y;
				} else if(pageSwitcher == w) {
					p = w.computeSize(SWT.DEFAULT, SWT.DEFAULT, flushCache);
					w.setBounds(clientArea.x, clientArea.y, p.x, p.y);
					clientArea.x += p.x;
					clientArea.width -= p.x;
				} else if(switcherSepar == w) {
					p = w.computeSize(SWT.DEFAULT, SWT.DEFAULT, flushCache);
					w.setBounds(clientArea.x, clientArea.y, p.x, clientArea.height);
					clientArea.x += p.x;
					clientArea.width -= p.x;
				} else if(cmpPageArea == w){
					w.setBounds(clientArea.x, clientArea.y, clientArea.width, clientArea.height);
				}
			}
                        
                        // Resize to fit all child controls
                        if(flushCache) {
                                cmp.setSize(cmp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                        }
                        
		}

	}
        
        private JSmoothResources jsRes;
        private Shell shell;
        private Display display;
        
	private Menu menuBar;
	private ToolBar pageSwitcher;
	
	// Separators
	private Label menuSepar;
	private Label switcherSepar;
	
	// The layout of the page area composite
	private StackLayout stackLayout;
	
	// JSmooth Pages
	public static final String PAGE_SKELETON = "JSmoothPage.Skeleton";
	public static final String PAGE_EXECUTABLE = "JSmoothPage.Executable";
	public static final String PAGE_WELCOME = "JSmoothPage.Welcome";
        public static final String PAGE_JAVA_APP = "JSmoothPage.Java_Application";
        
	private Map jsthPages = new HashMap();
	//
	
        // MVC Model
        private JSmoothApplication jsApp;
        
        // Page area
        private Composite cmpPageArea;
        
        private MenuManager menuMng;
        private ToolBarManager switcherMng;
        
	public JSmoothWindow(JSmoothApplication jsthApp) {
                (this.jsApp = jsthApp).addObserver(this);

                jsRes = new JSmoothResources(display = Display.getDefault());

                addPages();
	}
        
        private void addPages() {
                putPage(PAGE_WELCOME, new WelcomePage(this));
                
                JSmoothPage skelPage = new SkeletonPage(this, jsApp);
                jsApp.addObserver(skelPage);
                putPage(PAGE_SKELETON, skelPage);
                
                JSmoothPage javaAppPage = new JavaAppPage(this);
                jsApp.addObserver(javaAppPage);
                putPage(PAGE_JAVA_APP, javaAppPage);
                // TODO ...more to come
	}
        
//        private Collection getPageControls() {
//                Iterator it = getPages().iterator();
//                while (it.hasNext()) {
//                        Composite pageCtrl = ((JSmoothPage) it.next()).getControl();
//                        if(pageCtrl != null) {
//                                pageCtrl.layout();
//                        }
//                }
//        }
        
        /**
         * Creates the "Page Switcher" toolbar manager and its control.
         * The "Page Switcher" lies on the left of the JSmooth window, and
         * is a vertical toolbar, with toggle items. On item selection,
         * the window contents change, something like switching a "page", thus the name.
         */
	private void createSwitcherManager(Shell shell) {
                switcherMng = new ToolBarManager(
                        SWT.FLAT |
                        SWT.NO_FOCUS |
                        SWT.VERTICAL);
                
                // TODO Add actions
                ShowPageAction actnSkel = new ShowPageAction(this, jsApp);
                actnSkel.setPage(getPage(PAGE_SKELETON));
                actnSkel.setId(JSmoothAction.ACTION_SHOWPAGE_SKELETON);
                actnSkel.setToolTipText("Skeleton Page");
                switcherMng.add(actnSkel);
                
                ShowPageAction actnJavaApp = new ShowPageAction(this, jsApp);
                actnJavaApp.setPage(getPage(PAGE_JAVA_APP));
                actnJavaApp.setId(JSmoothAction.ACTION_SHOWPAGE_JAVA_APP);
                actnJavaApp.setToolTipText("Java Application");
                switcherMng.add(actnJavaApp);
                
                pageSwitcher = switcherMng.createControl(shell);
        }
        
        private void createMenuManager(Shell shell) {
                menuMng = new MenuManager();
                shell.setMenuBar(menuMng.createMenuBar((Decorations) shell));
                
                MenuManager fileMenuMng = new MenuManager("File");
                fileMenuMng.add(new NewProjectAction(this, jsApp));
                fileMenuMng.add(new CompileAction(this, jsApp));
                fileMenuMng.add(new SaveProjectAction(this, jsApp));
                
                // TODO Add actions
                menuMng.add(fileMenuMng);
                
                menuMng.updateAll(true);
        }
        
	private void createPageArea(Shell shell) {
		cmpPageArea = new Composite(shell, SWT.NONE);
		stackLayout = new StackLayout();
                cmpPageArea.setLayout(stackLayout);
                
                Iterator it = getPages().iterator();
                while (it.hasNext()) {
			((JSmoothPage) it.next()).create(cmpPageArea);
		}
	}
	
        private Collection getPages() {
                return jsthPages.values();
        }
        
        private JSmoothPage getPage(String id) {
                return (JSmoothPage) jsthPages.get(id);
        }
        
        private void putPage(String id, JSmoothPage page) {
                jsthPages.put(id, page);
        }
        
        public void showPage(JSmoothPage jsthPage) {
                stackLayout.topControl = jsthPage.getControl();
                if(cmpPageArea != null) {
                        cmpPageArea.layout();
                }
        }
	
        public static void main(String[] args) {
                new JSmoothWindow(new JSmoothApplication()).open();
	}
        
        private void createControls() {
                //Create the shell
                shell = new Shell(display, SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.RESIZE);

                shell.setData(this);

                // The application's menu bar
                createMenuManager(shell);
                
                // Horizontal separator label.
                // Separes the menu bar from the rest.
                menuSepar = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
                
                // The page switcher toolbar
                createSwitcherManager(shell);
                
                // Vertical separator label.
                // Separes the switcher toolbar from the rest.
                switcherSepar = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
                
                createPageArea(shell);

                // The JSmooth layout
                // NOTE: it should be set _after_ creating the controls
                shell.setLayout(new JSmoothLayout());
                
                //initialize the bounds of the shell to that appropriate for the contents
                initializeBounds();
        }
        
        private void initializeBounds() {
                Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
                Point location = getInitialLocation(size);

                shell.setBounds(location.x, location.y, size.x, size.y);
        }

        private Point getInitialLocation(Point size) {
                Rectangle screen = display.getClientArea();
                int x = Math.max(0, screen.x + (screen.width - size.x) / 2);
                int y = Math.max(0, screen.y + (screen.height - size.y) / 3);
                return new Point(x, y);
        }

        public void open() {
                
                if(shell == null) {
                        createControls();
                }
                
                jsApp.loadProject(null);
                
                // open the window
                shell.open();

                while (shell != null && ! shell.isDisposed()) {
                        if (!display.readAndDispatch()) {
                                display.sleep();
                        }
                }
                
                display.update();
        }
        
        public void update(Observable o, Object arg) {
                // JSmoothWindow's title
                shell.setText(jsApp.getProjectName());
                
                // Show and update the skeleton page
                if(!jsApp.hasProjectFile()) {
                        showPage(getPage(PAGE_WELCOME));
                }
                
                if(!arg.equals(UpdateMessage.MSGUPD_NULL_PROJECT)) {
                        IContributionItem citm = switcherMng.find(JSmoothAction.ACTION_SHOWPAGE_SKELETON);
                        ActionContributionItem acitm = (ActionContributionItem) citm;
                        ((JSmoothAction) acitm.getAction()).run();
                }
        }
        
        public Shell getShell() {
                return shell;
        }
        
        public JSmoothResources getResources() {
                return jsRes;
        }
        
}