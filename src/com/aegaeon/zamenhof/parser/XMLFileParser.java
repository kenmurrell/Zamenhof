package com.aegaeon.zamenhof.parser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.Stack;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;


public abstract class XMLFileParser {

    private static final Logger logger = Logger.getLogger(XMLFileParser.class.getName());

    protected class DumpHandler extends DefaultHandler {
        protected StringBuffer buffer;
        protected Stack<String> tags;

        @Override
        public void startDocument() {
            tags = new Stack<>();
            buffer = new StringBuffer();
            onParserStart();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            tags.push(qName);
            buffer.setLength(0);
            onElementStart(qName, this);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            tags.pop();
            onElementEnd(qName, this);
        }

        @Override
        public void endDocument() {
            onParserEnd();
        }

        public String getContents() {
            return buffer.toString();
        }

        public boolean hasContent() {
            return buffer.length() > 0;
        }

        public String getParent() {
            return tags.peek();
        }
    }

    protected void onParserStart() {
    }

    protected abstract void onElementStart(final String name, final DumpHandler handler) ;

    protected abstract void onElementEnd(final String name, final DumpHandler handler);

    protected void onParserEnd() {
    }

    public void parse(final File dumpeFile) {
        try {
            runThisShit(new FileInputStream(dumpeFile));
        } catch (Exception ex) //make this global, idc
        {
            logger.log(SEVERE, ex.toString());
        }
    }

    private void runThisShit(FileInputStream in) throws Exception {
        SAXParser parser = getParserFactory().newSAXParser();
        parser.parse(in, new DumpHandler());
    }

    private SAXParserFactory getParserFactory() {
        //use some github thing?
        return SAXParserFactory.newInstance();
    }
}
