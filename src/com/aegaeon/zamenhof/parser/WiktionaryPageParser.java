package com.aegaeon.zamenhof.parser;

import java.util.List;
import java.util.function.Consumer;

public class WiktionaryPageParser {

    private WiktionaryPage page;

    private WiktionaryEntryParser entryparser;

    private String currentNamespace;

    private Consumer<PageObject> collector;

    private WiktionaryPageParser(Consumer<PageObject> collector)
    {
        this.collector = collector;
    }

    public static WiktionaryPageParser create(Consumer<PageObject> collector)
    {
        //TODO:multiple dump languages?
        return new WiktionaryPageParser(collector);
    }


    public void setTimestamp(String timestamp) {
        this.page.setTimestamp(timestamp);
    }

    public void setText(String text) {
        //namespace!=null means an word entry
        //all non-word entry spaces have a namespace, word entry pages have a namespace "0" or null
        if(currentNamespace==null) {
            this.entryparser.parse(page,text);
        }
    }

    public void setTitle(String title) {
        this.page.setTitle(title);
    }

    public void setNamespace(String namespace)
    {
        this.currentNamespace = namespace;
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
        this.entryparser = new WiktionaryEntryParser();
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
