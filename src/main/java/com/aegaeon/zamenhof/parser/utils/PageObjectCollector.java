package com.aegaeon.zamenhof.parser.utils;

import com.aegaeon.zamenhof.parser.model.PageObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PageObjectCollector implements Consumer<PageObject> {

    private final List<PageObject> collection = new ArrayList<>();

    private static final Logger logger = Logger.getLogger(PageObjectCollector.class.getName());

    private IPageObjectFilter filter;

    public PageObjectCollector(IPageObjectFilter filter)
    {
        this.filter = filter;
    }

    @Override
    public void accept(PageObject object) {
        addToCollection(object);
    }

    private void addToCollection(PageObject object)
    {
        try
        {
            if(filter.allow(object))
            {
                collection.add(object);
            }
        }
        catch (OutOfMemoryError e)
        {
            logger.log(Level.SEVERE, "Out of Memory: "+e.toString());
            logger.log(Level.SEVERE,"Collection size "+collection.size());
            logger.log(Level.SEVERE, "Adding: "+ object.toString());
        }
    }

    public Collection<PageObject> getCollection()
    {
        return this.collection;
    }
}
