package com.aegaeon.zamenhof.parser;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WiktionaryDumpParser extends XMLFileParser {

    private static final Logger logger = Logger.getLogger(WiktionaryDumpParser.class.getName());

    protected boolean inPage;

    protected String baseUrl;

    protected WiktionaryPageParser pageParser;

    private int pagectr;

    private Set<String> namespaces;

    public WiktionaryDumpParser(WiktionaryPageParser pageParser) {
        namespaces = new HashSet<>();
        this.pageParser = pageParser;
        pagectr=0;
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
        if ("baseUrl".equals(name)) {
            this.setBaseUrl(handler.getContents());
        } else if ("namespace".equals(name)&&handler.hasContent()){
            this.addNamespace(handler.getContents());
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

    private void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    private void setPageId(String contents) {
        pageParser.setId(contents);
        //TODO:convert this to long
    }

    private void setPageTitle(String title) {
        String namespace = null;
        int idx = title.indexOf(':');
        if(idx>=0)
        {
            namespace = title.substring(0,idx);
            if(!namespaces.contains(namespace))
            {
                namespace = null;
            }
            else
            {
                title.substring(idx+1);
            }
        }
        pageParser.setTitle(title,namespace);
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

    protected void addNamespace(final String namespace)
    {
        namespaces.add(namespace);
    }

    protected void onPageStart()
    {

        pageParser.onPageStart();
    }

    protected void onPageEnd()
    {
        pageParser.onPageEnd();
        this.pagectr++;
        if(pagectr%1000==0)
        {
            logger.log(Level.INFO,String.format("Parsed %d pages",pagectr));
        }
        //gather pages to save here
    }




}
