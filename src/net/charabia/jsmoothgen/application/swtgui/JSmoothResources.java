/*
 * Created on Nov 29, 2003
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * @author Dumon
 */
public class JSmoothResources {
	public static final String NEW_PROJECT_WIZBAN = "resources.images.new_project_wizban";
	public static final String CLASSPATH_WIZBAN = "resources.images.classpath_wizban";
	public static final String JAR_WIZBAN = "resources.images.jar_wizban";
	public static final String MAIN_CLASS_WIZBAN = "resources.images.main_class_wizban";
	public static final String JAVA_APP_WIZBAN = "resources.images.java_app_wizban";
	public static final String JRE_WIZBAN = "resources.images.jre_wizban";
	
	public static final String ADD_ITEM = "resources.images.add_item";
	public static final String REMOVE_ITEM = "resources.images.remove_item";
	public static final String EDIT_ITEM = "resources.images.edit_item";
	public static final String MOVE_UP = "resources.images.move_up";
	public static final String MOVE_DOWN = "resources.images.move_down";
	
	private static final String RESOURCES_PROPERTIES = "resources.properties";
	
	private static ImageRegistry imageRegistry;
	private static URL context;
	private static ResourceBundle bundle;
	
	private static JSmoothResources instance;
	
	public static void loadResources(Display display, String path) {
		imageRegistry = new ImageRegistry(display);
		try{
			context = new URL("file", "localhost", path);
		} catch(MalformedURLException muex) {
			muex.printStackTrace(System.err);
		}
		
		InputStream input = null;
		try {
			bundle = new PropertyResourceBundle(
				new FileInputStream(
					path + "/" + RESOURCES_PROPERTIES));
		} catch (FileNotFoundException fnfex) {
			fnfex.printStackTrace(System.err);
		} catch (IOException ioex) {
			ioex.printStackTrace(System.err);
		}
		
		loadImages();
	}
	
	public static void loadResources(String path) {
		loadResources(Display.getCurrent(), path);
	}
	
	private static void loadImages() {
		loadImage(NEW_PROJECT_WIZBAN);
		loadImage(CLASSPATH_WIZBAN);
		loadImage(JAR_WIZBAN);
		loadImage(MAIN_CLASS_WIZBAN);
		loadImage(JAVA_APP_WIZBAN);
		loadImage(JRE_WIZBAN);
		loadImage(ADD_ITEM);
		loadImage(REMOVE_ITEM);
		loadImage(EDIT_ITEM);
		loadImage(MOVE_UP);
		loadImage(MOVE_DOWN);
	}
	
	private static void loadImage(String key) {
		URL url = null;
		try {
			url = new URL(context, bundle.getString(key));
		} catch (MalformedURLException e) {
			e.printStackTrace(System.err);
		}
		ImageDescriptor descriptor =
			ImageDescriptor.createFromURL(url);
		imageRegistry.put(key, descriptor);
	}
	
	public static Image getImage(String key) {
		return getDescriptor(key).createImage(true);
	}
	
	public static ImageDescriptor getDescriptor(String key) {
		return imageRegistry.getDescriptor(key);
	}
}
