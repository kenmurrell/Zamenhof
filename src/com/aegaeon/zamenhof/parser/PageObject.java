package com.aegaeon.zamenhof.parser;

public abstract class PageObject {

    private String timestamp;

    private String pageid;

    private String revision;

    protected PageObject(WiktionaryPage page)
    {
        this.timestamp = page.getTimestamp();
        this.revision = page.getRevision();
        this.pageid = page.getId();
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
