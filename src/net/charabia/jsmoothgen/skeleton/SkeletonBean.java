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
 * SkeletonBean.java
 *
 * Created on 7 août 2003, 20:09
 */

package net.charabia.jsmoothgen.skeleton;

public class SkeletonBean
{
	private String m_shortName;
	private String m_description;
	private String m_resourceCategory;
	private int m_resourceJarId;
	private int m_resourcePropsId;
	
	/** Creates a new instance of SkeletonBean */
	public SkeletonBean()
	{
	}
	
	public void setShortName(String name)
	{
		m_shortName = name;
	}
	public String getShortName()
	{
		return m_shortName;
	}	

	public void setDescription(String desc)
	{
		m_description = desc;
	}
	public String getDescription()
	{
		return m_description;
	}	

	public void setResourceCategory(String cat)
	{
		m_resourceCategory = cat;
	}

	public String getResourceCategory()
	{
		return m_resourceCategory;
	}

	public void setResourceJarId(int id)
	{
		m_resourceJarId = id;
	}	

	public int getResourceJarId()
	{
		return m_resourceJarId;
	}	

	public void setResourcePropsId(int id)
	{
		m_resourcePropsId = id;
	}	

	public int getResourcePropsId()
	{
		return m_resourcePropsId;
	}	

}
