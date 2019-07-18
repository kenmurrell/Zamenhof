package com.aegaeon.zamenhof.parser;

import sun.jvm.hotspot.debugger.Page;

import java.util.HashSet;
import java.util.function.Consumer;

public class PageObjectCollector implements Consumer<PageObject> {

    HashSet<PageObject> collection = new HashSet<>();


    @Override
    public void accept(PageObject pageObject) {
        collection.add(pageObject);
    }

    public HashSet<PageObject> getCollection()
    {
        return this.collection;
    }
}
