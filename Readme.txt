JSmooth: a VM wrapper toolkit for Windows
Copyright (C) 2003 Rodrigo Reyes <reyes@charabia.net>

JSmooth is a Java Executable Wrapper that makes a standard Windows
executable binary (.exe) from a jar file. It makes java deployment
much smoother and user-friendly, as it is able to find a Java VM by
itself. When no VM is available, it provides feed-back to the users, 
and can launch the default web browser to an URL that explains how 
to download a Java VM.

1. Status of the project

 JSmooth is currently in beta phase. It is provided in the hope it can
 be useful to the java community, but always keep in mind that it's
 still beta.

2 Using the software

 Double-clik on the JSmoothGen.exe icon.

3. Compiling the project

 -- PREREQUISITE --
 
 3.1 Get the source

 You can get the source code at sourceforge's:
 http://sourceforge.net/project/jsmooth

 3.2 MINGW for Windows (required)

 You need the MINGW GCC compiler to compile the Windows wrappers. Just
 install the last version (3.1.0 or above) from the following website:

  http://www.mingw.org/

 You need to download the MINGW-v.exe package (where v is the
 version). For instance MinGW-3.1.0-1.exe should be fine. You don't need
 any other package, so don't be afraid of all the stuff available on
 their web site.

 Once installed, you still need to setup your %PATH% environment variable to add the bin/
 directory where MINGW is installed.
 
 3.3 DOCUMENTATION STUFF (optional)

 If you want to build the whole distribution package (including the
 documentation, you need to install the docbook compilation chain).

 Install:
	- http://xml.apache.org/xalan-j/ and put the xalan jars into
	the lib folder of ANT. This is required to make ANT able to
	process XSLT.

	- docbook-xsl, available at
          http://sourceforge.net/projects/docbook/
	  Just download the docbook-xsl package, you don't need anything
          else here.

	- FOP, available at http://xml.apache.org/fop/

 Configure:

	- Open the build.xml ant script at the root of the project, and
         change the properties located at the top of the file. They are
         just under the comment:
         <!-- set here the properties specific to your computer -->

 -- BUILDING --

  3.3 Building the project

  To build the project, run the following command: ant jar
  To run the program: ant run
  To build a distribution: ant dist

4. License

All the JSmooth project is distributed under the terms of the GNU
General Public License. Please read the License.txt file that comes
with the package.

This license applies to all the files of the project, but not on the
generated executable. This means that you are free to generate
executable wrappers for proprietary software and distribute them
without applying the terms of the GPL to them.

Of course, this is the one and only exception. All other kind of
distribution of any file of the JSmooth package must conform with the
terms of the GNU General Public License.
