/*
  JSmooth: a VM wrapper toolkit for Windows
  Copyright (C) 2003 Rodrigo Reyes <reyes@charabia.net>

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

*/

/*
 * JSmoothModelPersistency.java
 *
 * Created on 7 août 2003, 19:42
 */

package net.charabia.jsmoothgen;

import java.io.*;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.skaringa.javaxml.DeserializerException;
import com.skaringa.javaxml.NoImplementationException;
import com.skaringa.javaxml.ObjectTransformer;
import com.skaringa.javaxml.ObjectTransformerFactory;
import com.skaringa.javaxml.SerializerException;

public class JSmoothModelPersistency
{

	public static JSmoothModelBean load(File fin) throws IOException
	{
		FileReader fr = new FileReader(fin);
		try {
			ObjectTransformer trans = ObjectTransformerFactory.getInstance().getImplementation();
			JSmoothModelBean obj = (JSmoothModelBean) trans.deserialize(new StreamSource(fr));
			fr.close();
			return obj;
		} catch (Exception exc)
		{
			throw new IOException(exc.toString());
		}
	}
	
	public static void save(File fout, JSmoothModelBean obj) throws IOException
	{
		FileWriter fw = new FileWriter(fout);
		try {
			ObjectTransformer trans = ObjectTransformerFactory.getInstance().getImplementation();
			trans.setProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
			trans.serialize(obj, new StreamResult(fw));
		} catch (Exception ex)
		{
			throw new IOException(ex.toString());
		}
		finally
		{
			fw.close();
		}
	}
	
	public String makeProperties(JSmoothModelBean obj)
	{
		StringBuffer out = new StringBuffer();
		out.append("arguments=");
		out.append(makePathConc(obj.getArguments()));
		out.append("\n");
		
		out.append("mainclassname=");
		out.append(obj.getMainClassName());
		out.append("\n");
		
		return out.toString();
	}
	
	public String makePathConc(String[] elements)
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
