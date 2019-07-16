package com.aegaeon.zamenhof.parser;


import java.util.ArrayList;
import java.util.List;

public class WiktionaryPage {

    private String title;

    //private Language language;

    private List<WiktionaryTranslation> translations;
    private String id;
    private String revision;
    private String timestamp;

    public WiktionaryPage()
    {
        translations = new ArrayList<>();
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void addTranslation(WiktionaryTranslation translation)
    {
        translations.add(translation);
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
}
