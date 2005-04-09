/*
 * Created on Nov 29, 2003
 */
package net.charabia.jsmoothgen.application.swtgui.resources;

import java.io.IOException;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Contains the resources used by JSmooth. Provides static methods and constants
 * for access.
 * 
 * NOTE: Before accessing any constant, the constructor must be called.
 * 
 * @author Dumon
 */
public final class JSmoothResources {
    public static Image IMG_SWITCHER_SKELETON_PAGE;
    public static Image IMG_SWITCHER_APPLICATION;
    public static Image IMG_SWITCHER_EXECUTABLE;

    private ResourceBundle bundle;
    private Display display;
    
    public JSmoothResources(Display display) {
        loadImages(this.display = display);
    }
    
    public void loadImages(Display display) {
        System.out.println("[DEBUG] Loading images...");
        Class clazz = getClass();
        URL url = clazz.getResource("jsmooth.properties");
        try {
            bundle = new PropertyResourceBundle(url.openStream());
        } catch (IOException e) {
            // Shouldn't happen. Ignore.
        }
        String name = bundle.getString("img.switcher.skeleton");
        IMG_SWITCHER_SKELETON_PAGE = new Image(display, clazz.getResourceAsStream(name));
        
        name = bundle.getString("img.switcher.application");
        IMG_SWITCHER_APPLICATION = new Image(display, clazz.getResourceAsStream(name));
        
        name = bundle.getString("img.switcher.executable");
        IMG_SWITCHER_EXECUTABLE = new Image(display, clazz.getResourceAsStream(name));
    }
}