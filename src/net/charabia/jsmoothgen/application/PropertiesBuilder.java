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

		addPair("currentdir", obj.getCurrentDirectory(), out);
		addPair("bundledvm", obj.getBundledJVMPath(), out);

		// addPair("nojvmmessage", obj.getNoJvmMessage(), out);
		// addPair("nojvmurl", obj.getNoJvmURL(), out);
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
		return out.toString();
	}
	
	static public String escapeString(String str)
	{
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
