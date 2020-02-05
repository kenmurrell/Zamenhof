package com.github.kenmurrell.zamenhof.model;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;


public abstract class XMLFileParser
{

	private static final Logger logger = Logger.getLogger(XMLFileParser.class.getName());

	protected void onParserStart()
	{
	}

	protected abstract void onElementStart(final String name, final DumpHandler handler);

	protected abstract void onElementEnd(final String name, final DumpHandler handler);

	protected void onParserEnd()
	{
	}

	public void parse(final File dumpFile)
	{
		try {
			runThisShit(new FileInputStream(dumpFile));
		}
		catch (NullPointerException nullex) {
			logger.log(Level.INFO, "Lost tag error: " + nullex.toString());
		}
		catch (Exception otherex) {
			logger.log(SEVERE, "Parsing error: " + otherex.toString());
		}
	}

	private void runThisShit(FileInputStream in) throws Exception
	{
		SAXParser parser = getParserFactory().newSAXParser();
		parser.parse(in, new DumpHandler());
	}

	private SAXParserFactory getParserFactory()
	{
		//use some github thing?
		return SAXParserFactory.newInstance();
	}

	protected class DumpHandler extends DefaultHandler
	{
		protected StringBuffer buffer;
		protected Stack<String> tags;
		protected Map<String, String> attr;

		@Override
		public void startDocument()
		{
			tags = new Stack<>();
			buffer = new StringBuffer();
			attr = new HashMap<>();
			onParserStart();
		}

		@Override
		public void characters(char[] ch, int start, int length)
		{
			buffer.append(ch, start, length);
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
		{
			tags.push(qName);
			if (attributes.getLength() == 2) {
				attr.put(attributes.getQName(0), attributes.getValue(0));
				attr.put(attributes.getQName(1), attributes.getValue(1));
			}
			buffer.setLength(0);
			onElementStart(qName, this);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
		{
			tags.pop();
			onElementEnd(qName, this);
		}

		@Override
		public void endDocument()
		{
			onParserEnd();
		}

		public String getContents()
		{
			return buffer.toString();
		}

		public Map<String, String> getAttributes()
		{
			return attr;
		}

		public boolean hasContent()
		{
			return buffer.length() > 0;
		}

		public boolean hasAttributes()
		{
			return !attr.isEmpty();
		}

		public String getParent()
		{
			return tags.peek();
		}
	}
}
