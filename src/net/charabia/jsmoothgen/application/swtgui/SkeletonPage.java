/*
 * Created on May 13, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.util.Arrays;

import net.charabia.jsmoothgen.application.swtgui.resources.JSmoothResources;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * @author Dumon
 */
public final class SkeletonPage extends JSmoothPage {
    // Widgets
    private Dialog propsDialog;

    private JSmoothApplication js;
    private String[] skeletons;
    
    public SkeletonPage(JSmoothApplication js) {
        super(js);
    }

    public Control createPageArea(Composite parent) {
        Composite top = new Composite(parent, SWT.NONE);
        top.setLayout(new GridLayout(3, false));

        Label label = new Label(top, SWT.NONE);
        label.setText("Skeleton:");

        // The Skeleton selection combo
        final Combo combo = new Combo(top, SWT.READ_ONLY);
        GridData grid = new GridData(GridData.FILL);
        grid.widthHint = 120;
        combo.setLayoutData(grid);
        
        String[] skeletons = getApplication().getAllSkeletonNames();
        String defaultSkeleton = getApplication().getInitialSkeletonName();
        combo.setItems(skeletons);
        int index = Arrays.binarySearch(skeletons, defaultSkeleton);
        combo.select(index);
        combo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                getApplication().setSkeletonName(combo.getText());
            }
        });

        Button button = new Button(top, SWT.PUSH);
        grid = new GridData(GridData.FILL);
        grid.widthHint = 120;
        button.setLayoutData(grid);
        button.setText("Properties...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                (new SkeletonPropertiesDialog(SkeletonPage.this)).open();
            }
        });

        return top;
    }
    
    public boolean apply() {
        return false;
    }

    protected void configurePage() {
        setImage(JSmoothResources.IMG_SWITCHER_SKELETON_PAGE);
        setToolTip("Skeleton");
    }
}