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
 * SkeletonList.java
 *
 * Created on 7 août 2003, 20:09
 */

package net.charabia.jsmoothgen.skeleton;

import java.io.*;
import java.util.*;

public class SkeletonList
{
	Hashtable m_skelsToDirs = new Hashtable();
	Hashtable m_nameToSkel = new Hashtable();
	
	/** Creates a new instance of SkeletonList */
	public SkeletonList(File directoryToScan)
	{
		File[] subdirs = directoryToScan.listFiles();
		for (int i=0; i<subdirs.length; i++)
		{
			if (subdirs[i].isDirectory())
			{
				File desc = new File(subdirs[i], "description.skel");
				if (desc.exists())
				{
					addSkeletonDirectory(subdirs[i], desc);
				}	
			}
		}
	}
	
	public void addSkeletonDirectory(File dir, File desc)
	{
		try {
			System.out.println("loading skel " + dir.toString());
			SkeletonBean skel = SkeletonPersistency.load(desc);
			m_skelsToDirs.put(skel, dir);
			m_nameToSkel.put(skel.getShortName(), skel);

			System.out.println("loaded skel " + skel.toString());
			System.out.println("loaded properties: "+ skel.getSkeletonProperties());
			
			
		} catch (IOException iox)
		{
			iox.printStackTrace();
		}
	}
	
	public String toString()
	{
		return m_skelsToDirs.toString();
	}
	
	public Iterator getIteratorSkel()
	{
		return m_skelsToDirs.keySet().iterator();
	}

	public File getDirectory(SkeletonBean skel)
	{
		return (File) m_skelsToDirs.get(skel);
	}
	
	public Iterator getIteratorName()
	{
		return m_nameToSkel.keySet().iterator();
	}

	public SkeletonBean getSkeleton(String name)
	{
		return (SkeletonBean)m_nameToSkel.get(name);
	}
	
	
}
