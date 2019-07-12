package com.aegaeon.zamenhof.parser;

public class WiktionaryPageParser {

    private static String lang;

    private String timestamp;
    //TODO:convert these

    private String text;

    private String title;

    private String id;

    private String revision;


    private WiktionaryPageParser(String lang)
    {
        this.lang = lang;
    }

    public static WiktionaryPageParser create(String lang)
    {
        return new WiktionaryPageParser(lang);
    }


    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    protected void onPageStart()
    {
        //create and parse entry
    }

    protected void onPageEnd()
    {
        //save shit
    }
}
