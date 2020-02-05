package com.github.kenmurrell.zamenhof.utils;

import com.github.kenmurrell.zamenhof.model.PageObject;

public class NullFilter implements IPageObjectFilter
{
	public boolean allow(PageObject o)
	{
		return true;
	}
}
