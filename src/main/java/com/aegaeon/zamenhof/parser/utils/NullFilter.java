package com.aegaeon.zamenhof.parser.utils;

import com.aegaeon.zamenhof.parser.model.PageObject;

public class NullFilter implements IPageObjectFilter
{
	public boolean allow(PageObject o)
	{
		return true;
	}
}
