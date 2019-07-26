package com.aegaeon.zamenhof.parser.writers;

import com.aegaeon.zamenhof.parser.model.PageObject;

import java.util.Collection;

public interface IWriter
{
	void write(Collection<PageObject> objects, String basename);
}
