/*
 * PEFile.java
 *
 * Created on 28 juillet 2003, 21:28
 */

package net.charabia.jsmoothgen.pe;

import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 *
 * @author  Rodrigo
 */
public class PEFile
{
    private File m_file;
    private FileInputStream m_in = null;
    private FileChannel m_channel = null;

    private PEOldMSHeader m_oldmsheader;
    private PEHeader m_header;
    private Vector m_sections = new Vector();
    
    private PEResourceDirectory m_resourceDir;

    /** Creates a new instance of PEFile */
    public PEFile(File f)
    {
	m_file = f;
    }

    public void close() throws IOException
    {
	m_in.close();
    }
	
    public void open() throws FileNotFoundException, IOException
    {
	m_in = new FileInputStream(m_file);
	m_channel = m_in.getChannel();

	m_oldmsheader = new PEOldMSHeader(this);				

	m_oldmsheader.read();
	m_oldmsheader.dump(System.out);
	long headoffset = m_oldmsheader.e_lfanew;
		
	m_header = new PEHeader(this, headoffset);
	m_header.read();
	m_header.dump(System.out);

	int seccount = m_header.NumberOfSections;
	System.out.println("LOADING " + seccount + " sections...");

	long offset = headoffset + (m_header.NumberOfRvaAndSizes * 8) + 24 + 96;
		
	for (int i=0; i<seccount; i++)
	    {
		System.out.println("Offset: " + offset + " (" + this.m_channel.position());

		PESection sect = new PESection(this, offset);
		sect.read();
		sect.dump(System.out);
		m_sections.add(sect);
		offset += 40;
	    }
	System.out.println("After sections: " + this.m_channel.position() + " (" + offset + ")");

	ByteBuffer resbuf = null;
	long resourceoffset = m_header.ResourceDirectory_VA;
	for (int i=0; i<seccount; i++)
	    {
		PESection sect = (PESection)m_sections.get(i);
		if (sect.VirtualAddress == resourceoffset)
		    {
			System.out.println("  Resource section found: " + resourceoffset);
			PEResourceDirectory prd = new PEResourceDirectory(this, sect);
			resbuf = prd.buildResource(sect.VirtualAddress);
			break;
		    }
	    }
    }
	
    public FileChannel getChannel()
    {
	return m_channel;
    }
	
    public static void main(String args[]) throws IOException, CloneNotSupportedException
    {
	System.out.println("Here !");

	//(no)PEFile pe = new PEFile(new File("F:/Program Files/LAN Search PRO/lansearch.exe"));
		
	PEFile pe = new PEFile(new File("c:/projects/jwrap/JWrap.exe"));
	//PEFile pe = new PEFile(new File("c:/projects/jwrap/Copie.exe"));
	// PEFile pe = new PEFile(new File("c:/projects/jwrap/test.exe"));
	// PEFile pe = new PEFile(new File("F:/Program Files/bQuery/bQuery.exe"));
	// PEFile pe = new PEFile(new File("F:/Program Files/Server Query/query.exe"));
	 // PEFile pe = new PEFile(new File("F:/Program Files/AvRack/rtlrack.exe"));
	pe.open();

	System.out.println("===============\nADDING A RES");
	ByteBuffer data = ByteBuffer.allocate(0x666);
	PEResourceDirectory resdir = pe.getResourceDirectory();
	resdir.addNewResource("POUET", "A666", "#1033", data);
	resdir.dump(System.out);
	System.out.println("New size = " + resdir.size());
	File out = new File("c:/projects/jwrap/COPIE.exe");
	pe.dumpTo(out);
    }

    public PEResourceDirectory getResourceDirectory() throws IOException
    {
	if (m_resourceDir != null)
	    return m_resourceDir;

	long resourceoffset = m_header.ResourceDirectory_VA;
	for (int i=0; i<m_sections.size(); i++)
	    {
		PESection sect = (PESection)m_sections.get(i);
		if (sect.VirtualAddress == resourceoffset)
		    {
			m_resourceDir = new PEResourceDirectory(this, sect);
			return m_resourceDir;
		    }
	    }

	return null;
    }

    public void dumpTo(File destination) throws IOException, CloneNotSupportedException
    {
	System.out.println("----------------\n\nDUMPING...");
	int outputcount = 0;
	FileOutputStream fos = new FileOutputStream(destination);
	FileChannel out = fos.getChannel();
		

	//
	// Make a copy of the Header, for safe modifications
	//
	PEOldMSHeader oldmsheader = (PEOldMSHeader) this.m_oldmsheader.clone();
	PEHeader peheader = (PEHeader) m_header.clone();
	Vector sections = new Vector();
	for (int i=0; i<m_sections.size(); i++)
	    {
		PESection sect = (PESection)m_sections.get(i);
		PESection cs = (PESection)sect.clone();
		sections.add(cs);
	    }
		
	//
	// First, write the old MS Header, the one starting
	// with "MZ"...
	//
	long newexeoffset = oldmsheader.e_lfanew;
	ByteBuffer msheadbuffer = oldmsheader.get();
	outputcount = out.write(msheadbuffer);
	this.m_channel.position(64);
	out.transferFrom(this.m_channel,  64, newexeoffset - 64);
		
		
	//
	// Then Write the new Header...
	//
	ByteBuffer headbuffer = peheader.get();
	out.position(newexeoffset);
	outputcount = out.write(headbuffer);

	//
	// After the header, there are all the section
	// headers...
	//
	long offset = oldmsheader.e_lfanew + (m_header.NumberOfRvaAndSizes * 8) + 24 + 96;
	out.position(offset);
	for (int i=0; i<sections.size(); i++)
	    {
		System.out.println("  offset: " + out.position());
		PESection sect = (PESection) sections.get(i);
		
		ByteBuffer buf = sect.get();
		outputcount = out.write(buf);
	    }

	//
	// Now, we write the real data: each of the section
	// and their data...
	//

	// Not sure why it's always at 1024... ?
	offset = 1024;

	//
	// Dump each section data
	//

	long virtualAddress = offset;
	if ((virtualAddress % peheader.SectionAlignment)>0)
	    virtualAddress += peheader.SectionAlignment - (virtualAddress%peheader.SectionAlignment);

	long resourceoffset = m_header.ResourceDirectory_VA;
	for (int i=0; i<sections.size(); i++)
	    {
		PESection sect = (PESection) sections.get(i);
		if (resourceoffset == sect.VirtualAddress)
		    {
			System.out.println("Dumping RES section " + i + " at " + offset + " from " + sect.PointerToRawData + " (VA=" + virtualAddress + ")");
			out.position(offset);
			long sectoffset = offset;
			PEResourceDirectory prd = this.getResourceDirectory();
			ByteBuffer resbuf = prd.buildResource(sect.VirtualAddress);
			resbuf.position(0);

			out.write(resbuf);
			offset += resbuf.capacity();
			long rem = offset % this.m_header.FileAlignment;
			if (rem != 0)
			    offset += this.m_header.FileAlignment - rem;

			if (out.size()+1 < offset)
			    {
				ByteBuffer padder = ByteBuffer.allocate(1);
				out.write(padder, offset - 1);
			    }
			
			long virtualSize = resbuf.capacity();
			if ((virtualSize  % peheader.FileAlignment)>0)
			    virtualSize += peheader.SectionAlignment - (virtualSize%peheader.SectionAlignment);

			sect.PointerToRawData = sectoffset;
			sect.SizeOfRawData = resbuf.capacity();
			if ((sect.SizeOfRawData%this.m_header.FileAlignment)>0)
			    sect.SizeOfRawData += (this.m_header.FileAlignment-(sect.SizeOfRawData%this.m_header.FileAlignment));
			sect.VirtualAddress = virtualAddress;
			sect.VirtualSize = virtualSize;

			virtualAddress += virtualSize;

		    }
		else if (sect.PointerToRawData > 0)
		    {
			System.out.println("Dumping section " + i + "/" + sect.getName() + " at " + offset + " from " + sect.PointerToRawData + " (VA=" + virtualAddress + ")");
			out.position(offset);
			this.m_channel.position(sect.PointerToRawData);
			long sectoffset = offset;

			out.position(offset + sect.SizeOfRawData);
			ByteBuffer padder = ByteBuffer.allocate(1);
			out.write(padder, offset + sect.SizeOfRawData - 1);
				
			long outted = out.transferFrom(this.m_channel, offset, sect.SizeOfRawData);
			offset += sect.SizeOfRawData;
			System.out.println("offset before alignment, " + offset);

			long rem = offset % this.m_header.FileAlignment;
			if (rem != 0)
			    {
				offset += this.m_header.FileAlignment - rem;
			    }
			System.out.println("offset after alignment, " + offset);
			
// 			long virtualSize = sect.SizeOfRawData;
// 			if ((virtualSize % peheader.SectionAlignment)>0)
// 			    virtualSize += peheader.SectionAlignment - (virtualSize%peheader.SectionAlignment);
			
			sect.PointerToRawData = sectoffset;
			//			sect.SizeOfRawData = 
			sect.VirtualAddress = virtualAddress;
			//			sect.VirtualSize = virtualSize;

			virtualAddress += sect.VirtualSize;
 			if ((virtualAddress % peheader.SectionAlignment)>0)
 			    virtualAddress += peheader.SectionAlignment - (virtualAddress%peheader.SectionAlignment);
			
		    }
		else
		    {
			// generally a BSS, with a virtual size but no
			// data in the file...
			System.out.println("Dumping section " + i + " at " + offset + " from " + sect.PointerToRawData + " (VA=" + virtualAddress + ")");
			long virtualSize = sect.VirtualSize;
			if ((virtualSize % peheader.SectionAlignment)>0)
			    virtualSize += peheader.SectionAlignment - (virtualSize%peheader.SectionAlignment);

			sect.VirtualAddress = virtualAddress;
			//			sect.VirtualSize = virtualSize;
			virtualAddress += virtualSize;

		    }
	    }

	// 
	// Now that all the sections have been written, we have the
	// correct VirtualAddress and Sizes, so we can update the new
	// header and all the section headers...
	
	peheader.updateVAAndSize(m_sections, sections);
 	headbuffer = peheader.get();
 	out.position(newexeoffset);
 	outputcount = out.write(headbuffer);

	peheader.dump(System.out);
	System.out.println("Dumping the section again...");
 	offset = oldmsheader.e_lfanew + (m_header.NumberOfRvaAndSizes * 8) + 24 + 96;
 	out.position(offset);
	for (int i=0; i<sections.size(); i++)
	    {
		System.out.println("  offset: " + out.position());
		PESection sect = (PESection) sections.get(i);
		sect.dump(System.out);
		ByteBuffer buf = sect.get();
		outputcount = out.write(buf);
	    }


	fos.flush();
	fos.close();
    }
    /*
     */
	
}
