package com.aegaeon.zamenhof.parser.model;

import java.util.List;

public interface IWiktionaryEntryParser
{

	void parse(final WiktionaryPage page, String text);

	List<PageObject> getPageObjects();

}
