package com.aegaeon.zamenhof.parser.model;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WiktionaryPageParser
{

	private WiktionaryPage page;

	private IWiktionaryEntryParser entryparser;

	private String currentNamespace;

	private Consumer<PageObject> collector;

	private Supplier<IWiktionaryEntryParser> entryParserSupplier;

	public WiktionaryPageParser(Consumer<PageObject> collector, Supplier<IWiktionaryEntryParser> entryParserSupplier)
	{
		this.collector = collector;
		this.entryParserSupplier = entryParserSupplier;
	}

	public void setTimestamp(String timestamp)
	{
		this.page.setTimestamp(timestamp);
	}

	public void setText(String text)
	{
		//namespace!=null means an word entry
		//all non-word entry spaces have a namespace, word entry pages have a namespace "0" or null
		if (currentNamespace == null) {
			this.entryparser.parse(page, text);
		}
	}

	public void setTitle(String title)
	{
		this.page.setTitle(title);
	}

	public void setNamespace(String namespace)
	{
		this.currentNamespace = namespace;
	}

	public void setId(String id)
	{
		this.page.setId(id);
	}

	public void setRevision(String revision)
	{
		this.page.setRevision(revision);
	}

	protected void onPageStart()
	{
		this.page = new WiktionaryPage();
		this.entryparser = entryParserSupplier.get();
	}

	protected void onPageEnd()
	{
		savePageTranslations(entryparser.getPageObjects());
	}

	private void savePageTranslations(List<PageObject> pageObjects)
	{
		pageObjects.forEach(t -> collector.accept(t));
	}


}
