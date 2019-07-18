package com.aegaeon.zamenhof.parser;

public abstract class PageObject {

    private String timestamp;

    private String pageid;

    private String revision;

    public PageObject(WiktionaryPage page)
    {
        this.timestamp = page.getTimestamp();
        this.revision = page.getRevision();
        this.pageid = page.getId();
    }

    public String getRevision() {
        return revision;
    }

    public String getPageid() {
        return pageid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public abstract String toString();

}
