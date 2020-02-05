package com.github.kenmurrell.zamenhof.utils;

import com.github.kenmurrell.zamenhof.model.PageObject;

public interface IPageObjectFilter
{
	boolean allow(PageObject o);
}
