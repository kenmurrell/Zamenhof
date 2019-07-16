package com.aegaeon.zamenhof.parser;

import java.io.File;
import java.util.logging.Logger;

public class WiktionaryDumpParser extends XMLFileParser {

    private static final Logger logger = Logger.getLogger(WiktionaryDumpParser.class.getName());

    protected boolean inPage;

    protected String base;

    protected WiktionaryPageParser pageParser;

    public WiktionaryDumpParser(WiktionaryPageParser pageParser) {
        this.pageParser = pageParser;
    }

    @Override
    public void parse(final File dumpFile) {
        super.parse(dumpFile);
    }


    @Override
    protected void onElementStart(String name, DumpHandler handler) {
        if ("page".equals(name)) {
            inPage = true;
            this.onPageStart();
        }
    }

    @Override
    protected void onElementEnd(String name, DumpHandler handler) {
        if ("base".equals(name)) {
            base = handler.getContents();
        } else if ("page".equals(name)) {
            inPage = false;
            this.onPageEnd();
        }

        if (inPage) {
            if ("page".equals(handler.getParent())) {
                if ("id".equals(name)) {
                    this.setPageId(handler.getContents());
                } else if ("title".equals(name)) {
                    this.setPageTitle(handler.getContents());
                }
            } else if ("revision".equals(handler.getParent())) {
                if ("id".equals(name)) {
                    this.setRevision(handler.getContents());
                } else if ("timestamp".equals(name)) {
                    this.setTimestamp(handler.getContents());
                } else if ("text".equals(name)) {
                    this.setText(handler.getContents());
                }
            }

        }
    }


    private void setPageId(String contents) {
        pageParser.setId(contents);
        //TODO:convert this to long
    }

    private void setPageTitle(String contents) {
        String namespace = null;
        int idx = contents.indexOf(':');
        if(idx>=0)
        {
            namespace = contents.substring(0,idx);
        }

        pageParser.setTitle(contents,namespace);
    }

    private void setRevision(String contents)
    {
        pageParser.setRevision(contents);
        //TODO:convert this to long
    }

    private void setTimestamp(String contents)
    {
        pageParser.setTimestamp(contents);
        //TODO:convert this to date
    }

    private void setText(String contents)
    {

        pageParser.setText(contents);
    }

    protected void onPageStart()
    {

        pageParser.onPageStart();
    }

    protected void onPageEnd()
    {
        pageParser.onPageEnd();
        //TODO:print metric of parses pages
        //gather pages to save here
    }




}
