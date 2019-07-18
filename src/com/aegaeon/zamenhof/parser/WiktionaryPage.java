package com.aegaeon.zamenhof.parser;


public class WiktionaryPage {

    private String title;

    //private Language language;

    private String id;
    private String revision;
    private String timestamp;

    public WiktionaryPage()
    {
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getTimestamp()
    {
        return this.timestamp;
    }

    public String getRevision()
    {
        return this.revision;
    }

    public String getId()
    {
        return this.id;
    }
}
