package com.aegaeon.zamenhof.parser;

import javax.xml.parsers.SAXParser;
import java.io.File;
import java.util.logging.Logger;

public class WiktionaryDumpParser extends XMLFileParser {

    private static final Logger logger = Logger.getLogger(WiktionaryDumpParser.class.getName());

    protected boolean inPage;

    protected String base;

    protected WiktionaryPageParser wikiparser;

    public WiktionaryDumpParser(WiktionaryPageParser pageParser) {
        wikiparser = pageParser;
    }

    @Override
    public void parse(final File dumpFile) {
        super.parse(dumpFile);
    }


    @Override
    protected void onElementStart(String name, DumpHandler handler) {
        if ("page".equals(name)) {
            inPage = true;
        }
    }

    @Override
    protected void onElementEnd(String name, DumpHandler handler) {
        if ("base".equals(name)) {
            base = handler.getContents();
        } else if ("page".equals(name)) {
            inPage = false;
        }

        if (inPage) {
            if ("page".equals(handler.getParent())) {
                if ("id".equals(name)) {
                    setPageId(handler.getContents());
                } else if ("title".equals(name)) {
                    setPageTitle(handler.getContents());
                }
            } else if ("revision".equals(handler.getParent())) {
                if ("id".equals(name)) {
                    setRevision(handler.getContents());
                } else if ("timestamp".equals(name)) {
                    setTimestamp(handler.getContents());
                } else if ("text".equals(name)) {
                    setText(handler.getContents());
                }
            }

        }
    }


    private void setPageId(String contents) {
        wikiparser.setId(contents);
        //TODO:convert this to long
    }

    private void setPageTitle(String contents) {
        wikiparser.setTitle(contents);
    }

    private void setRevision(String contents)
    {
        wikiparser.setRevision(contents);
        //TODO:convert this to long
    }

    private void setTimestamp(String contents)
    {
        wikiparser.setTimestamp(contents);
        //TODO:convert this to date
    }

    private void setText(String contents)
    {
        wikiparser.setText(contents);
    }

    protected void onPageStart()
    {
        wikiparser.onPageStart();
    }

    protected void onPageEnd()
    {
        wikiparser.onPageEnd();
        //gather pages to save here
    }




}
