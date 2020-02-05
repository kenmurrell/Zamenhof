package com.github.kenmurrell.zamenhof.model;

import java.util.Map;

public abstract class PageObject
{

	private WiktionaryPage page;

	protected PageObject(WiktionaryPage page)
	{
		this.page = page;
	}

	protected WiktionaryPage getPage()
	{
		return page;
	}

	public abstract Map<String, String> attributes();

}
