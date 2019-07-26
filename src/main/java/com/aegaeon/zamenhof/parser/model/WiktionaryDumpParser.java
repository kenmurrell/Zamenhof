package com.aegaeon.zamenhof.parser.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WiktionaryDumpParser extends XMLFileParser {

    private static final Logger logger = Logger.getLogger(WiktionaryDumpParser.class.getName());

    protected boolean inPage;

    protected String baseUrl;

    protected WiktionaryPageParser pageParser;

    private int pagectr;

    private Map<String,String> namespaces;

    public WiktionaryDumpParser(WiktionaryPageParser pageParser) {
        namespaces = new HashMap<>();
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
        } else if ("namespace".equals(name)&&handler.hasContent()&&handler.hasAttributes()){
            this.addNamespace(handler.getContents(),handler.getAttributes().get("key"));
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
                } else if("ns".equals(name)) {
                	this.setNamespace(handler.getContents());
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
    }

    private void setPageTitle(String title) {
        pageParser.setTitle(title);
    }

    private void setNamespace(String contents)
    {
    	pageParser.setNamespace(namespaces.getOrDefault(contents,null));
    }

    private void setRevision(String contents)
    {
        pageParser.setRevision(contents);
    }

    private void setTimestamp(String contents)
    {
        pageParser.setTimestamp(contents);
    }

    private void setText(String contents)
    {

        pageParser.setText(contents);
    }

    protected void addNamespace(final String namespace,String val)
    {
        namespaces.put(val,namespace);
    }

    protected void onPageStart()
    {

        pageParser.onPageStart();
    }

    protected void onPageEnd()
    {
        pageParser.onPageEnd();
        this.pagectr++;
        status();
    }

    private void status()
    {
        if(pagectr%50000==0)
        {
            logger.log(Level.INFO,String.format("Parsed %d pages",pagectr));
        }
    }




}
