JSmooth: a VM wrapper toolkit for Windows
Copyright (C) 2003 Rodrigo Reyes <reyes@charabia.net>

JSmooth is a Java Executable Wrapper that makes a standard Windows
executable binary (.exe) from a jar file. It makes java deployment
much smoother and user-friendly, as it is able to find a Java VM by
itself, and give feed-back to the user when no VM is available.


1. Status of the project

 JSmooth is currently in pre-alpha. The proof-of-concept stage is now
 complete, and the application is still under development. No public
 release of the compiled software is available yet.


2. Architecture

 JSmooth is made up of two sub-projects:

 - JSmoothGen: the main program. It is responsible for creating and
   editing the project, and generating the final windows executable.

 - The skeletons: those are what the final windows executable are made
   of.

Typically, one creates a new project by selecting a skeleton and by
specifying the jar and options (such as classpath, main class, VM
options).

The rationale of the skeleton system is to allow the user to select a
specialized and customizable executable, rather than having just one
big executable that is supposed to manage all the possible
situations. For instance, there may be a skeleton for windowed
applications, a skeleton for command-line application, a skeleton for
NT Services applications, or even skeletons customized for
distribution on your company's intranet.


3. How to compile

- First, you'll need ant, to compile the JSmoothGen software, and all
  the sample java applications (found in each of the skeleton
  directories).

  ant is available at http://ant.apache.org/

- Then you'll need MingW to compile the Windows executables. My
  recommandation is to just download and use the dev-c++ IDE, which
  includes the MingW package as well as all what you need to
  comfortably compile the projects.

  Dev-C++ is avaiable at http://www.bloodshed.net/
  At this time, I use version 4.9.8.0 of dev-c++.

 There is no global compilation script, so you'll need to do it by
 hand. However each project contains either an ant-script, either a
 makefile or a dev-c++ project file.


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
