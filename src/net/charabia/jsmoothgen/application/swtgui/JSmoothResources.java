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
	public static final String IMG_NEW_PROJECT_WIZBAN = "img.new_project_wizban";
	public static final String IMG_CLASSPATH_WIZBAN = "img.classpath_wizban";
	public static final String IMG_MAIN_CLASS_WIZBAN = "img.main_class_wizban";
	public static final String IMG_JRE_WIZBAN = "img.jre_wizban";
	public static final String IMG_EXE_WIZBAN = "img.exe_wizban";

	public static final String IMG_NEW_PROJECT = "img.new_project";
	public static final String IMG_MOVE_UP = "img.move_up";
	public static final String IMG_MOVE_DOWN = "img.move_down";

	private static final String RESOURCES_PROPERTIES = "resources.properties";

	private static ImageRegistry imageRegistry;
	private static URL context;
	private static ResourceBundle bundle;

	public static void loadResources(Display display, String path) {
		imageRegistry = new ImageRegistry(display);
		try {
			context = new URL("file", "localhost", path);
		} catch (MalformedURLException muex) {
			muex.printStackTrace(System.err);
		}

		InputStream input = null;
		try {
			bundle = new PropertyResourceBundle(new FileInputStream(path + "/" + RESOURCES_PROPERTIES));
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
		loadImage(IMG_NEW_PROJECT_WIZBAN);
		loadImage(IMG_CLASSPATH_WIZBAN);
		loadImage(IMG_MAIN_CLASS_WIZBAN);
		loadImage(IMG_JRE_WIZBAN);
		loadImage(IMG_EXE_WIZBAN);
		loadImage(IMG_NEW_PROJECT);
		loadImage(IMG_MOVE_UP);
		loadImage(IMG_MOVE_DOWN);
	}

	private static void loadImage(String key) {
		URL url = null;
		try {
			url = new URL(context, bundle.getString(key));
		} catch (MalformedURLException muex) {
			muex.printStackTrace(System.err);
		}
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
		imageRegistry.put(key, descriptor);
	}

	public static Image getImage(String key) {
		return getDescriptor(key).createImage(true);
	}

	public static ImageDescriptor getDescriptor(String key) {
		return imageRegistry.getDescriptor(key);
	}
}
