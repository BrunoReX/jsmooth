JSmooth: a VM wrapper toolkit for Windows
Copyright (C) 2003 Rodrigo Reyes <reyes@charabia.net>

JSmooth is a Java Executable Wrapper that makes a standard Windows
executable binary (.exe) from a jar file. It makes java deployment
much smoother and user-friendly, as it is able to find a Java VM by
itself. When no VM is available, it provides feed-back to the users, 
and can launch the default web browser to an URL that explains how 
to download a Java VM.

1. Status of the project

 JSmooth is currently in beta phase. The proof-of-concept stage is now
 complete, and the application is still under development.

2. Compiling the project

 -- PREREQUISITE --
 
 2.1 The JIMI package

 To compile the project, you need to install Sun's JIMI package. This
 package is not provided due to the non-free license used by Sun to
 distribute it. JIMI adds the possibility to load additional image
 file formats (the .ico for instance) and to reduce the 16 and 32-bit
 images to 256 colours.

 Download it on http://www.javasoft.com website, then extract the
 JimiProClasses.zip file from the archive downloaded, and copy it in
 the lib/ directory.

 2.2 The GCC compiler for Windows

 You need the MINGW GCC compiler to compile the wrappers. The easiest
 way is probably to download and install the last version of DEV-C++,
 which is a free Windows IDE for GCC/MINGW.

 Download DEV-C++ it at http://www.bloodshed.net/

 Alternatively, you can also just download and install the MINGW
 package instead of DEV-C++: http://www.mingw.org/

 In both case, you need to setup your %PATH% environment variable to
 add the directory where the make.exe/strip.exe executable are
 installed.
 
 -- BUILDING --

 2.3

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
