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
 * SkeletonPersistency.java
 *
 * Created on 7 août 2003, 22:22
 */

package net.charabia.jsmoothgen.skeleton;


import java.io.*;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class SkeletonPersistency
{
	public static SkeletonBean load(File fin) throws IOException
	{
		FileInputStream fis = new FileInputStream(fin);
		try {
			XMLDecoder dec = new XMLDecoder(fis);
			SkeletonBean obj = (SkeletonBean)dec.readObject();
			fis.close();
			return obj;
		} catch (Exception exc)
		{
			throw new IOException(exc.toString());
		}
	}
	
	public static void save(File fout, SkeletonBean obj) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(fout);
		try {
			XMLEncoder enc = new XMLEncoder(fos);
			enc.writeObject(obj);
			enc.close();
		} catch (Exception ex)
		{
			throw new IOException(ex.toString());
		}
		finally
		{
			fos.close();
		}
	}

}
