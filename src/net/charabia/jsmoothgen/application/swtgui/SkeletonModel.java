/*
 * Created on May 14, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import net.charabia.jsmoothgen.skeleton.SkeletonBean;
import net.charabia.jsmoothgen.skeleton.SkeletonList;
import net.charabia.jsmoothgen.skeleton.SkeletonProperty;

public class SkeletonModel extends Observable {

    public static final int TYPE_NONE = -1;

    public static final int TYPE_TEXT_SINGLE = 1;

    public static final int TYPE_TEXT_MULTI = 2;

    public static final int TYPE_CHECK = 3;

    private SkeletonList skeletonList;

    private JSmoothApplication jsmoothApp;

    // Cached list of skeleton names
    private List skelNames;

    public SkeletonModel(JSmoothApplication jsthModel) {
        this.skeletonList = new SkeletonList(new File("skeletons"));
        this.jsmoothApp = jsthModel;
    }

    private List getSkeletonList(boolean flush) {
        // True at the first call to this method
        if (skelNames == null) {
            fillSkeletonList(skelNames = new Vector());
        }

        if (flush) {
            skelNames.clear();
            fillSkeletonList(skelNames);
        }

        // From the "cache"
        return skelNames;
    }

    private void fillSkeletonList(List list) {
        Iterator i = skeletonList.getIteratorNoDebugName();
        while (i.hasNext()) {
            list.add((String) i.next());
        }
    }

    public String[] getSkeletonNames() {
        return (String[]) getSkeletonList(false).toArray(new String[0]);
    }

    public SkeletonProperty[] getProperties() {
        SkeletonBean skelBean = skeletonList.getSkeleton(jsmoothApp.getSkeletonName());
        return skelBean.getSkeletonProperties();
    }

    public int getPropertyType(SkeletonProperty prop) {
        if (prop.getType().equals(SkeletonProperty.TYPE_STRING))
            return TYPE_TEXT_SINGLE;
        if (prop.getType().equals(SkeletonProperty.TYPE_TEXTAREA))
            return TYPE_TEXT_MULTI;
        if (prop.getType().equals(SkeletonProperty.TYPE_BOOLEAN))
            return TYPE_CHECK;

        return TYPE_NONE;
    }

    public String getPropertyId(SkeletonProperty prop) {
        return prop.getIdName();
    }

    public String getPropertyLabel(SkeletonProperty prop) {
        return prop.getLabel();
    }

    public String getPropertyValue(SkeletonProperty prop) {
        // The JSmooth API is a little bit inconsistent
        // and the SkeletonProperty value is not used actually.
        // Instead, we return the JSmoothModelBean.Property value
        return jsmoothApp.getPropertyValue(prop.getIdName());
    }

    public void setPropertyValue(SkeletonProperty prop, String value) {
        System.out.println("[DEBUG] Setting property \""
                + ((SkeletonProperty) prop).getIdName() + "\" to value \""
                + value + "\"");

        jsmoothApp.setPropertyValue(((SkeletonProperty) prop).getIdName(), value);
    }
    
    public SkeletonList getSkeletonList() {
        return skeletonList;
    }
    
}