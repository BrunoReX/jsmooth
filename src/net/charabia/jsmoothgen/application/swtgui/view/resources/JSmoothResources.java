/*
 * Created on Nov 29, 2003
 */
package net.charabia.jsmoothgen.application.swtgui.view.resources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Contains the resources used by JSmooth.
 * Provides static methods and constants for access.
 * 
 * @author Dumon
 */
public final class JSmoothResources {
	public static final String IMG_WIZBAN_NEW_PROJECT = "img.wizban.new_project";
	public static final String IMG_WIZBAN_CLASSPATH = "img.wizban.classpath";
	public static final String IMG_WIZBAN_MAIN_CLASS = "img.wizban.main_class";
	public static final String IMG_WIZBAN_JRE = "img.wizban.jre";
	public static final String IMG_WIZBAN_EXE = "img.wizban.exe";

	public static final String IMG_NEW_PROJECT_WIZARD = "img.new_project_wizard";
        public static final String IMG_NEW_PROJECT = "img.new_project";
	public static final String IMG_ECLIPSE_32 = "img.eclipse32";
        public static final String IMG_ECLIPSE_16 = "img.eclipse16";
	public static final String IMG_SKEL_SELEC = "img.skeleton_selection";
	public static final String IMG_SKEL_PROPS = "img.skeleton_properties";

	private static ImageRegistry imageRegistry;
	private static ResourceBundle bundle;
	
	private static int uniqueId = 26051981; // dummy initial value
	
	public JSmoothResources(Display display) {
		loadImages(display);
	}
	
	private void loadImages(Display display) {
                System.out.println("[DEBUG] Loading images...");;
		imageRegistry = new ImageRegistry(display);
		bundle = ResourceBundle.getBundle("net.charabia.jsmoothgen.application.swtgui.view.resources.jsmooth");
		loadImage(IMG_WIZBAN_NEW_PROJECT);
		loadImage(IMG_WIZBAN_CLASSPATH);
		loadImage(IMG_WIZBAN_MAIN_CLASS);
		loadImage(IMG_NEW_PROJECT_WIZARD);
                loadImage(IMG_NEW_PROJECT);
		loadImage(IMG_ECLIPSE_32);
                loadImage(IMG_ECLIPSE_16);
		loadImage(IMG_SKEL_SELEC);
		loadImage(IMG_SKEL_PROPS);
	}

	private void loadImage(String key) {
                String img = null;
                try {
                        img = bundle.getString(key);
                } catch (MissingResourceException mre) {
                        System.out.println("[DEBUG] Couldn't find image file name for key [" + key + "]. Skipping...");
                        return;
                }
		ImageDescriptor descriptor = ImageDescriptor.createFromFile(JSmoothResources.class, img);
		imageRegistry.put(key, descriptor);
	}

	public Image getImage(String key) {
		return getDescriptor(key).createImage(true);
	}

	public ImageDescriptor getDescriptor(String key) {
		return imageRegistry.getDescriptor(key);
	}
	
	public void doRegister(Image image) {
		imageRegistry.put(getUniqueId(), image);
	}
	
	private String getUniqueId() {
		return Integer.toString(uniqueId ++);
	}
	
}
