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
		File pattern = new File(skelroot, skel.getExecutableName());
		if (pattern.exists() == false)
		{
			System.out.println("Can't find skeleton at " + skelroot);
			return false;
		}

		PEFile pe = new PEFile(pattern);
		pe.open();
		
		File jarloc = new File(data.getJarLocation());
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
	
}
