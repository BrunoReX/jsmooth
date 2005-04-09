/*
 * Created on Nov 19, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.charabia.jsmoothgen.application.swtgui.resources.JSmoothResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author Dumon
 */
public final class JSmoothApplication {
    // Menu actions
    public static final String ID_ACTION_SHOWPAGE_JAVA_APP = "Action.ShowPage.Java_Application";
    public static final String ID_ACTION_SHOWPAGE_WELCOME = "Action.ShowPage.Welcome";
    public static final String ID_ACTION_SHOWPAGE_EXE = "Action.ShowPage.Executable";
    public static final String ID_ACTION_SHOWPAGE_SKELETON = "Action.ShowPage.Skeleton";
    public static final String ID_ACTION_NEW_PROJECT = "Action.NewProject";
    public static final String ID_ACTION_COMPILE = "Action.Compile_Project";
    public static final String ID_ACTION_SAVE_PROJECT = "Action.Save_Project";
    
    private Shell shell;
    private Display display;

    // Separators
    private Label menuSeparator;
    private Label switcherSeparator;

    // The layout of the page area composite
    private StackLayout stackLayout;
    
    // JSmooth Pages
    public static final String ID_PAGE_SKELETON = "page.skeleton";
    public static final String ID_PAGE_EXE = "page.executable";
    public static final String ID_PAGE_WELCOME = "page.welcome";
    public static final String ID_PAGE_JAVA_APP = "page.application";
    
    private List pages = new ArrayList();
    
    // Page area
    private Composite pageArea;

    private Menu mainMenu;
    private ToolBar switcherControl;

    public JSmoothApplication(Display display, __JSmoothApplication__ jsmoothApp) {
        new JSmoothResources(this.display = display);
        addPages();
    }

    private void addPages() {
        JSmoothPage page = new WelcomePage(this);
        page.setImage(JSmoothResources.IMG_SWITCHER_WELCOME);
        page.setToolTip("Welcome");
        pages.add(page);
        
        page = new SkeletonPage(this);
        page.addPageModifyListener(new PageModifyListener() {
            public void pageModified() {
//                IMenuManager actionMenu = mainMenu.findMenuUsingPath(ID_MENU_ACTION);
//                ActionContributionItem saveAction = (ActionContributionItem) actionMenu.find("net.charabia.jsmoothgen.application.swtgui.ACTION_SAVE_PROJECT");
//                saveAction.getAction().setEnabled(true);
            }
        });
        page.setImage(JSmoothResources.IMG_SWITCHER_SKELETON_PAGE);
        page.setToolTip("Skeleton");
        pages.add(page);

        page = new JavaAppPage(this);
        page.addPageModifyListener(new PageModifyListener() {
            public void pageModified() {
//                IMenuManager actionMenu = mainMenu.findMenuUsingPath(ID_MENU_ACTION);
//                ActionContributionItem saveAction = (ActionContributionItem) actionMenu.find(net.charabia.jsmoothgen.application.swtgui.ACTION_SAVE_PROJECT);
//                saveAction.getAction().setEnabled(true);
            }
        });
        page.setImage(JSmoothResources.IMG_SWITCHER_APPLICATION);
        page.setToolTip("Java Application");
        pages.add(page);
        
        
        page = new ExecutablePage(this);
        page.addPageModifyListener(new PageModifyListener() {
            public void pageModified() {
//                IMenuManager actionMenu = mainMenu.findMenuUsingPath(ID_MENU_ACTION);
//                ActionContributionItem saveAction = (ActionContributionItem) actionMenu.find(net.charabia.jsmoothgen.application.swtgui.ACTION_SAVE_PROJECT);
//                saveAction.getAction().setEnabled(true);
            }
        });
        page.setImage(JSmoothResources.IMG_SWITCHER_EXECUTABLE);
        page.setToolTip("Windows Executable");
        pages.add(page);
    }

    /**
     * Creates the "Page Switcher" toolbar manager and its control. The "Page
     * Switcher" lies on the left of the JSmooth window, and is a vertical
     * toolbar, with toggle items. On item selection, the window contents
     * change, something like switching a "page", thus the name.
     */
    private void createSwitcherManager(Shell shell) {
        switcherControl = new ToolBar(shell, SWT.FLAT | SWT.NO_FOCUS | SWT.VERTICAL);
        Iterator it = getPages().iterator();
        while (it.hasNext()) {
            final JSmoothPage page = (JSmoothPage) it.next();
            ToolItem item = new ToolItem(switcherControl, SWT.RADIO);
            item.setImage(page.getImage());
            item.setToolTipText(page.getToolTip());
            item.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    showPage(page);
                }
            });
        }
    }

    private void createMainMenu(Shell shell) {
        mainMenu = new Menu(shell, SWT.BAR);

        MenuItem topItem = new MenuItem(mainMenu, SWT.CASCADE);
        topItem.setText("Action");
        Menu menu = new Menu(shell, SWT.DROP_DOWN);
        topItem.setMenu(menu);
        
        MenuItem item = new MenuItem(menu, SWT.NULL);
        item.setText("Compile");
        item.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                (new CompileAction(JSmoothApplication.this)).run();
            }
        });
        
        shell.setMenuBar(mainMenu);
    }

    private void createPages(Shell shell) {
        pageArea = new Composite(shell, SWT.NONE);
        stackLayout = new StackLayout();
        pageArea.setLayout(stackLayout);
        Iterator it = getPages().iterator();
        while (it.hasNext()) {
            ((JSmoothPage) it.next()).createControl(pageArea);
        }
    }

    private List getPages() {
        return pages;
    }

    private JSmoothPage getPage(Class clazz) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void showPage(JSmoothPage jsthPage) {
        stackLayout.topControl = jsthPage.getControl();
        if (pageArea != null) {
            pageArea.layout();
        }
    }

    public static void main(String[] args) {
        new JSmoothApplication(Display.getDefault(), new __JSmoothApplication__()).run();
    }
    
    public void run() {
        if (shell == null) {
            createControls();
        }

//        jsmoothApp.loadProject(null);

        // open the window
        shell.open();
        
        try {
            while (shell != null && !shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
            display.update();
        } finally {
            display.dispose();
        }
    }
    
    private void createControls() {
        // Create the shell
        shell = new Shell(display, SWT.TITLE | SWT.CLOSE | SWT.MIN /* | SWT.RESIZE */);

        shell.setData(this);

        // The application's menu bar
        createMainMenu(shell);

        // Horizontal separator label.
        // Separes the menu bar from the rest.
        menuSeparator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);

        // The page switcher toolbar
        createSwitcherManager(shell);

        // Vertical separator label.
        // Separes the switcher toolbar from the rest.
        switcherSeparator = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);

        createPages(shell);

        // The JSmooth layout
        // NOTE: it should be set *after* creating the controls
        shell.setLayout(new JSmoothLayout());

        // Initialize the bounds of the shell to that appropriate for the contents
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

    public Shell getShell() {
        return shell;
    }
    
    /**
     * Special layout for the JSmooth window.
     */
    class JSmoothLayout extends Layout {

        protected Point computeSize(Composite composite, int wHint, int hHint,
                boolean flushCache) {
            if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
                return new Point(wHint, hHint);
            }

            Point result = new Point(0, 0);
            Control[] ctrls = composite.getChildren();

            Point p = null;
            for (int i = 0; i < ctrls.length; i++) {
                Control w = ctrls[i];

                if (menuSeparator == w) {
                    p = w.computeSize(wHint, hHint, flushCache);
                    result.y += p.y;
                } else if (switcherControl == w) {
                    p = w.computeSize(wHint, hHint, flushCache);
                    result.x += p.x;
                    result.y += p.y;
                } else if (switcherSeparator == w) {
                    p = w.computeSize(wHint, hHint, flushCache);
                    result.x += p.x;
                } else if (pageArea == w) {
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
            if (flushCache) {
                Iterator it = getPages().iterator();
                while (it.hasNext()) {
                    JSmoothPage page = (JSmoothPage) it.next();
                    ((Composite) page.getControl()).layout();
                }
            }
            //

            Rectangle clientArea = cmp.getClientArea();

            Control[] wgs = cmp.getChildren();

            Point p = null;
            for (int i = 0; i < wgs.length; i++) {
                Control w = wgs[i];

                if (menuSeparator == w) {
                    p = w.computeSize(SWT.DEFAULT, SWT.DEFAULT, flushCache);
                    w.setBounds(clientArea.x, clientArea.y, clientArea.width,
                            p.y);
                    clientArea.y += p.y;
                    clientArea.height += p.y;
                } else if (switcherControl == w) {
                    p = w.computeSize(SWT.DEFAULT, SWT.DEFAULT, flushCache);
                    w.setBounds(clientArea.x, clientArea.y, p.x, p.y);
                    clientArea.x += p.x;
                    clientArea.width -= p.x;
                } else if (switcherSeparator == w) {
                    p = w.computeSize(SWT.DEFAULT, SWT.DEFAULT, flushCache);
                    w.setBounds(clientArea.x, clientArea.y, p.x,
                            clientArea.height);
                    clientArea.x += p.x;
                    clientArea.width -= p.x;
                } else if (pageArea == w) {
                    w.setBounds(clientArea.x, clientArea.y, clientArea.width,
                            clientArea.height);
                }
            }

            // Resize to fit all child controls
            if (flushCache) {
                cmp.setSize(cmp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            }

        }

    }
}