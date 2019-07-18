package com.aegaeon.zamenhof.parser;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class WiktionaryPageParser {

    private static Logger logger = Logger.getLogger(WiktionaryPageParser.class.getName());

    private WiktionaryPage page;

    private WiktionaryEntryParser entryparser;

    private String currentNamespace;

    private Consumer<PageObject> collector;

    private static String lang;

    private WiktionaryPageParser(String lang, Consumer<PageObject> collector)
    {
        this.collector = collector;
        WiktionaryPageParser.lang = lang;

    }

    public static WiktionaryPageParser create(String lang, Consumer<PageObject> collector)
    {
        //TODO:multiple dump languages?
        return new WiktionaryPageParser("en",collector);
    }


    public void setTimestamp(String timestamp) {
        this.page.setTimestamp(timestamp);
    }

    public void setText(String text) {
        //namespace!=null means an infopage
        if(currentNamespace==null) {
            entryparser.parse(page,text);
        }
    }

    public void setTitle(String title, String namespace) {
        this.currentNamespace = namespace;
        this.page.setTitle(title);
    }

    public void setId(String id) {
        this.page.setId(id);
    }

    public void setRevision(String revision) {
        this.page.setRevision(revision);
    }

    protected void onPageStart()
    {
        this.page = new WiktionaryPage();
        entryparser = new WiktionaryEntryParser();
        //create and parse entry
    }

    protected void onPageEnd()
    {
        savePageTranslations(entryparser.getPageObjects());
    }

    private void savePageTranslations(List<PageObject> pageObjects)
    {
        pageObjects.forEach(t->collector.accept(t));
    }


}
