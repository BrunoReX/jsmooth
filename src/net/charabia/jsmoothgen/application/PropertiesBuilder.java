/*
 * PropertiesBuilder.java
 *
 * Created on 9 août 2003, 19:36
 */

package net.charabia.jsmoothgen.application;

import java.io.*;

/**
 *
 * @author  Rodrigo
 */
public class PropertiesBuilder
{
    static public String makeProperties(File basedir, JSmoothModelBean obj)
    {
	StringBuffer out = new StringBuffer();

	addPair("arguments", obj.getArguments(), out);
	addPair("mainclassname", obj.getMainClassName(), out);
	addPair("jvmsearch", makePathConc(obj.getJVMSearchPath()), out);
	addPair("minversion", obj.getMinimumVersion(), out);
	addPair("maxversion", obj.getMaximumVersion(), out);

	addPair("currentdir", obj.getCurrentDirectory(), out);

	if (obj.getMaximumMemoryHeap() > 1)
	    {
		addPair("maxheap", Integer.toString(obj.getMaximumMemoryHeap()), out);
	    }

	if (obj.getInitialMemoryHeap() > 1)
	    {
		addPair("initialheap", Integer.toString(obj.getInitialMemoryHeap()), out);
	    }

	// BundledVM & classpaths are changed to be accessible
	// from the current directory
	File curdir = new File(obj.getExecutableName()).getParentFile();

	if (curdir == null)
	    curdir = basedir.getAbsoluteFile();

	if (curdir.isAbsolute() == false)
	    {
		curdir = new File(basedir, curdir.toString());
	    }

	if (obj.getCurrentDirectory() != null)
	    {
		File newcurdir = new File(obj.getCurrentDirectory());
		if (newcurdir.isAbsolute() == false)
		    {
			curdir = new File(curdir, newcurdir.toString());
		    }
		else
		    curdir = newcurdir;
	    }

	if (obj.getBundledJVMPath() != null)
	    addPair("bundledvm", getRenormalizedPathIfNeeded(obj.getBundledJVMPath(), basedir, curdir), out);

        if (obj.getClassPath() != null)
        {
            String[] relcp = new String[obj.getClassPath().length];
            for (int i=0; i<relcp.length; i++)
                {
                    relcp[i] = getRenormalizedPathIfNeeded(obj.getClassPath()[i], basedir, curdir);
                }
            addPair("classpath", makePathConc(relcp), out);
        }
	//
	// Adds all the skeleton-specific properties
	//
	if (obj.getSkeletonProperties() != null)
	    {
		for (int i=0; i<obj.getSkeletonProperties().length; i++)
		    {
			JSmoothModelBean.Property prop = obj.getSkeletonProperties()[i];
			if (prop.getKey() != null)
			    {
				String val = prop.getValue();
				if (val == null)
				    val = "";
				addPair("skel_" + prop.getKey(), val, out);
			    }
		    }
	    }


	//
	// Adds all the java properties. Those properties are
	// typically passed as -Dname=value arguments for the sun's
	// JVM.
	//

	JavaPropertyPair[] javapairs = obj.getJavaProperties();
        if (javapairs != null)
        {
            addPair("javapropertiescount", new Integer(javapairs.length).toString(), out);
            for (int i=0; i<javapairs.length; i++)
                {
                    addPair("javaproperty_name_" + i, javapairs[i].getName(), out);
                    addPair("javaproperty_value_" + i, javapairs[i].getValue(), out);
                }
        }

	return out.toString();
    }
	
    static public String getRenormalizedPathIfNeeded(String value, File previousbasedir, File newbasedir)
    {
	if (newbasedir == null)
	    return value;

	if (value == null)
	    return "";

	File abs = new File(previousbasedir, value).getAbsoluteFile();
	File n = JSmoothModelPersistency.makePathRelativeIfPossible(newbasedir, abs);
	System.out.println("PROPERTY RENORM " + value + " -> " + n.toString());

	return n.toString();
    }

    static public String escapeString(String str)
    {
	if (str == null)
	    return "";

	StringBuffer out = new StringBuffer();
	for (int i=0; i<str.length(); i++)
	    {
		char c = str.charAt(i);
		switch(c)
		    {
		    case '\n':
			out.append("\\n");
			break;
		    case '\t':
			out.append("\\t");
			break;
		    case '\r':
			out.append("\\r");
			break;
		    case '\\':
			out.append("\\\\");
			break;
		    default:
			out.append(c);
		    }
	    }
	return out.toString();
    }
	
    static private void addPair(String name, String value, StringBuffer out)
    {
	out.append(escapeString(name));
	out.append("=");
	out.append(escapeString(value));
	out.append("\n");
    }
	
    static public String makePathConc(String[] elements)
    {
	StringBuffer buf = new StringBuffer();
	for (int i=0; i<elements.length; i++)
	    {
		buf.append(elements[i]);
		if ((i+1)<elements.length)
		    buf.append(";");
	    }
	return buf.toString();
    }

}
