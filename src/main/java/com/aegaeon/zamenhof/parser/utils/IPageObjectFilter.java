package com.aegaeon.zamenhof.parser.utils;

import com.aegaeon.zamenhof.parser.model.PageObject;

public interface IPageObjectFilter
{
	boolean allow(PageObject o);
}
