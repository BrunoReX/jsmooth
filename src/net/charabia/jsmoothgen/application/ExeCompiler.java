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

package net.charabia.jsmoothgen.application;

import net.charabia.jsmoothgen.skeleton.*;
import net.charabia.jsmoothgen.pe.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class ExeCompiler
{
	static public boolean compile(File skelroot, SkeletonBean skel, JSmoothModelBean data, File out) throws Exception
	{
		File basedir = new File(data.getBaseDir());
		
		File pattern = new File(skelroot, skel.getExecutableName());
		if (pattern.exists() == false)
		{
			System.out.println("Can't find skeleton at " + skelroot);
			return false;
		}

		PEFile pe = new PEFile(pattern);
		pe.open();
		
		File jarloc = concFile(basedir, new File(data.getJarLocation()));
		if (jarloc.exists() == false)
		{
			System.out.println("Can't find jar at " + jarloc);
			return false;
		}
		ByteBuffer jardata = load(jarloc);
		
		PEResourceDirectory resdir = pe.getResourceDirectory();
		boolean resb = resdir.replaceResource(skel.getResourceCategory(), skel.getResourceJarId(), 1033, jardata);
		if (resb == false)
		{
			System.out.println("Can't replace jar resource !");
		}
		
		String props = PropertiesBuilder.makeProperties(data);
		ByteBuffer propdata = convert(props);
		resb = resdir.replaceResource(skel.getResourceCategory(), skel.getResourcePropsId(), 1033, propdata);
		
		pe.dumpTo(out);

		System.out.println("PROPERTIES:\n" + props);
	
		return true;
	}
	
	static private ByteBuffer load(File in) throws Exception
	{
		FileInputStream fis = new FileInputStream(in);
		ByteBuffer data = ByteBuffer.allocate((int)in.length());
		data.order(ByteOrder.LITTLE_ENDIAN);
		FileChannel fischan = fis.getChannel();
		fischan.read(data);
		data.position(0);
		fis.close();

		return data;
	}
	
	static private ByteBuffer convert(String data)
	{
		ByteBuffer result = ByteBuffer.allocate(data.length()+1);
		result.position(0);
		
		for (int i=0; i<data.length(); i++)
		{
			result.put((byte)data.charAt(i));
		}
		result.put((byte)0);
		
		result.position(0);
		return result;
	}

	static public File concFile(File root, File name)
	{
		if (name.isAbsolute())
			return name;
		
		return new File(root, name.toString());
	}
	
}
