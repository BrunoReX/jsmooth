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

package net.charabia.jsmoothgen.ant;

import net.charabia.jsmoothgen.application.*;
import net.charabia.jsmoothgen.skeleton.*;
import net.charabia.jsmoothgen.pe.*;
import java.io.*;

public class JSmoothGen extends org.apache.tools.ant.Task
{
    private java.io.File m_prjfile;
    private java.io.File m_skeletonRoot;
    private boolean verbose;
    
    public void setProject(java.io.File prjfile)
    {
	m_prjfile = prjfile;
    }

    public void setSkeletonRoot(java.io.File skeletonRoot)
    {
	m_skeletonRoot = skeletonRoot;
    }

    public void setVerbose(boolean val) 
    {
        verbose = val;
    }

    public void execute() throws org.apache.tools.ant.BuildException
    {
	if (m_prjfile == null)
	    throw new org.apache.tools.ant.BuildException("Project file not set");
	if (m_skeletonRoot == null)
	    throw new org.apache.tools.ant.BuildException("Skeleton Root dir file not set");

	File prj = m_prjfile;
	if (prj.exists() == false)
	    {
		prj = new File(prj.toString() + ".jsmooth");
	    }

	if (prj.exists() == false)
	    {
		throw new org.apache.tools.ant.BuildException("Project file " + prj + " not found");
	    }

	try {
	    JSmoothModelBean model = JSmoothModelPersistency.load(prj);

	    File basedir = prj.getParentFile();

	    SkeletonList skelList = new SkeletonList(m_skeletonRoot);

	    File out = new File(basedir, model.getExecutableName());
	    SkeletonBean skel = skelList.getSkeleton(model.getSkeletonName());
	    File skelroot = skelList.getDirectory(skel);
	    
	    final ExeCompiler compiler = new ExeCompiler();
	    if (verbose) {
                compiler.addListener(new ExeCompiler.StepListener() {
			public void complete() {}
			public void failed() {}
			
			public void setNewState(int percentComplete, String state) {
			    log("jsmooth: " + state + " ( " + percentComplete + "%)");
			}
		    });
            }
	    
	    if (compiler.compile(skelroot, skel, basedir, model, out))
		log("Java application wrapped in " + model.getExecutableName());
	    else
		log("jsmoothgen failed: " + compiler.getErrors());

	} catch (Exception exc)
	    {
		throw new org.apache.tools.ant.BuildException("Error building the jsmooth wrapper", exc);
	    }
    }

}
