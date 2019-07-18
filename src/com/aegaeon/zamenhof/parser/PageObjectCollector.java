package com.aegaeon.zamenhof.parser;

import java.util.HashSet;
import java.util.function.Consumer;

public class PageObjectCollector implements Consumer<PageObject> {

    private HashSet<PageObject> collection = new HashSet<>();

    @Override
    public void accept(PageObject pageObject) {
        collection.add(pageObject);
    }

    public HashSet<PageObject> getCollection()
    {
        return this.collection;
    }
}
