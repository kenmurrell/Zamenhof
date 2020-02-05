package com.github.kenmurrell.zamenhof.writers;

import com.github.kenmurrell.zamenhof.model.PageObject;

import java.util.Collection;

public interface IWriter
{
	void write(Collection<PageObject> objects, String basename);
}
