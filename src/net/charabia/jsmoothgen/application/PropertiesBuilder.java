/*
 * PropertiesBuilder.java
 *
 * Created on 9 août 2003, 19:36
 */

package net.charabia.jsmoothgen.application;

/**
 *
 * @author  Rodrigo
 */
public class PropertiesBuilder
{
	static public String makeProperties(JSmoothModelBean obj)
	{
		StringBuffer out = new StringBuffer();

		addPair("arguments", obj.getArguments(), out);
		addPair("mainclassname", obj.getMainClassName(), out);
		addPair("classpath", makePathConc(obj.getClassPath()), out);
		addPair("jvmsearch", makePathConc(obj.getJVMSearchPath()), out);
		addPair("minversion", obj.getMinimumVersion(), out);
		addPair("maxversion", obj.getMaximumVersion(), out);

		return out.toString();
	}
	
	static private void addPair(String name, String value, StringBuffer out)
	{
		out.append(name);
		out.append("=");
		out.append(value);
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
