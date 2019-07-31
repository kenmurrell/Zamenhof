package com.aegaeon.zamenhof.parser.utils;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;


/**
 *   This writes xml databases which are a list of flat (1 level) maps.
 *   Minimal xml integrity checks have been added, so its up to the user
 *   to carefully order their methods or else the structure will be shit.
 */

public class FlatMapXmlWriter implements Closeable
{

	//can't find an xmlwriter or xmlstreamwriter that won't crash...ill write a basic/specific one myself
	private BufferedWriter writer;

	private boolean inDoc;

	private boolean inRoot;

	private String currentRootName;

	public FlatMapXmlWriter(String filename) throws IOException
	{
		writer = new BufferedWriter(new FileWriter(filename));
		inDoc = inRoot = false;
	}

	private String LVL(int lvl)
	{
		return lvl>0?("   "+LVL(lvl-1)):("");
	}

	private void write(int lvl, String str) throws IOException
	{
		writer.write(LVL(lvl)+str);
		writer.newLine();
	}

	private String createStartHeader(String name, Map<String,String> attributes)
	{
		StringBuilder b = new StringBuilder();
		b.append("<");
		b.append(name);
		if(attributes!=null && !attributes.isEmpty()) {
			for (Map.Entry<String, String> attr : attributes.entrySet()) {
				b.append(" ").append(attr.getKey()).append("=\"").append(attr.getValue()).append("\"");
			}
		}
		b.append(">");
		return b.toString();
	}

	private String createEndHeader(String name)
	{
		return "</"+name+">";
	}

	public boolean createDocument(String encoding, String version) throws IOException
	{
		if(!inDoc && !inRoot && StringTools.isNumeric(version) && StringTools.isEncoding(encoding))
		{
			String header = "<?xml version=\""+version+"\" encoding=\""+encoding+"\"?>";
			write(0,header);
			inDoc = true;
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean endDocument()
	{
		if(inDoc && !inRoot)
		{
			inDoc = false;
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean createRoot(String rootname, Map<String,String> attributes) throws IOException
	{
		if(!inRoot && inDoc)
		{
			String header = createStartHeader(rootname,attributes);
			write(0,header);
			currentRootName = rootname;
			inRoot = true;
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean endRoot() throws IOException
	{
		if(inRoot && inDoc)
		{
			String header = createEndHeader(currentRootName);
			write(0,header);
			currentRootName = null;
			inRoot = false;
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean addEntry(String name, Map<String,String> attributes, Map<String,String> data) throws IOException
	{
		if(inDoc && inRoot)
		{
			write(1,createStartHeader(name, attributes));
			for(Map.Entry<String, String> e : data.entrySet())
			{
				write(2,createStartHeader(e.getKey(),null) + e.getValue() + createEndHeader(e.getKey()));
			}
			write(1,createEndHeader(name));
			return true;
		}
		else
		{
			return false;
		}
	}


	@Override
	public void close() throws IOException
	{
		if(inRoot)
		{
			endRoot();
		}
		if(inDoc)
		{
			endDocument();
		}
		writer.flush();
		writer.close();
	}
}
