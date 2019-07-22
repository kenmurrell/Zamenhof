package com.aegaeon.zamenhof.parser;

import java.time.LocalDateTime;

public abstract class PageObject {

    private WiktionaryPage page;

    protected PageObject(WiktionaryPage page)
    {
        this.page = page;
    }

    protected int getRevision() {
        return page.getRevision();
    }

    protected int getPageid() {
        return page.getId();
    }

    protected LocalDateTime getTimestamp() {
        return page.getTimestamp();
    }

    protected String getStringTimestamp() {
        return page.getStringTimestamp();
    }

    @Override
    public abstract String toString();

}
