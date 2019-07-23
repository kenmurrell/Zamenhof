package com.aegaeon.zamenhof.parser;

import java.util.Collection;

public interface IWriter
{
	void write(Collection<PageObject> objects,String basename);
}
