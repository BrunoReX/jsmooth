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
 * ResIcon.java
 *
 * Created on 17 août 2003, 22:51
 */

package net.charabia.jsmoothgen.pe.res;

import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class ResIcon
{
    public long Size;            /* Size of this header in bytes DWORD*/
    public long  Width;           /* Image width in pixels LONG*/
    public long  Height;          /* Image height in pixels LONG*/
    public int  Planes;          /* Number of color planes WORD*/
    public int  BitsPerPixel;    /* Number of bits per pixel WORD*/
    /* Fields added for Windows 3.x follow this line */
    public long Compression;     /* Compression methods used DWORD*/
    public long SizeOfBitmap;    /* Size of bitmap in bytes DWORD*/
    public long  HorzResolution;  /* Horizontal resolution in pixels per meter LONG*/
    public long  VertResolution;  /* Vertical resolution in pixels per meter LONG*/
    public long ColorsUsed;      /* Number of colors in the image DWORD*/
    public long ColorsImportant; /* Minimum number of important colors DWORD*/
	
    public PaletteElement[] Palette;
    public short[] BitmapXOR;
    public short[] BitmapAND;

    public class PaletteElement
    {
	public int Blue;
	public int Green;
	public int Red;
	public int Reserved;
	public String toString()
	{
	    return "{"+Blue+","+Green+","+Red+","+Reserved+"}";
	}
    }

    /** Creates a new instance of ResIcon */
    public ResIcon(ByteBuffer in)
    {
	Size = in.getInt();
	Width = in.getInt();
	Height = in.getInt();
	Planes = in.getShort();
	BitsPerPixel = in.getShort();
	Compression = in.getInt();
	SizeOfBitmap = in.getInt();
	HorzResolution = in.getInt();
	VertResolution = in.getInt();
	ColorsUsed = in.getInt();
	ColorsImportant = in.getInt();

	Palette = new PaletteElement[(int)ColorsUsed];
	for (int i=0; i<Palette.length; i++)
	    {
		PaletteElement el = new PaletteElement();
		el.Blue = in.get();
		el.Green = in.get();
		el.Red = in.get();
		el.Reserved = in.get();
	    }
	int xorbytes = (((int)Height/2) * (int)Width * (int)BitsPerPixel) / 8;

	BitmapXOR = new short[xorbytes];
	for (int i=0; i<BitmapXOR.length; i++)
	    {
		BitmapXOR[i] = in.get();
	    }

	int andbytes = (((int)Height/2) * (int)Width) / 8;
	BitmapAND = new short[andbytes];
	for (int i=0; i<BitmapAND.length; i++)
	    {
		BitmapAND[i] = in.get();
	    }

    }

    public String toString()
    {
	StringBuffer out = new StringBuffer();

	out.append("Size: " + Size);
	out.append("\nWidth: " + Width);
	out.append("\nHeight: " + Height);
	out.append("\nPlanes: " + Planes);
	out.append("\nBitsPerPixel: " + BitsPerPixel);
	out.append("\nCompression: " + Compression);
	out.append("\nSizeOfBitmap: " + SizeOfBitmap);
	out.append("\nHorzResolution: " + HorzResolution);
	out.append("\nVertResolution: " + VertResolution);
	out.append("\nColorsUsed: " + ColorsUsed);
	out.append("\nColorsImportant: " + ColorsImportant);

	for (int i = 0; i<Palette.length; i++)
	    {
		out.append("\n");
		out.append(Palette[i].toString());
	    }
	out.append("\nBitmapXOR={");
	for (int i=0; i<BitmapXOR.length; i++)
	    {
		out.append((byte)BitmapXOR[i]);
	    }
	out.append("}\nBitmapAnd={");
	for (int i=0; i<BitmapAND.length; i++)
	    {
		out.append((byte)BitmapAND[i]);
	    }

	return out.toString();
    }
	
}
