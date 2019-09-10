package com.aegaeon.zamenhof.parser.utils;

import com.aegaeon.zamenhof.parser.model.PageObject;
import com.aegaeon.zamenhof.parser.model.WiktionaryTranslation;

import java.util.Objects;

public class TranslationFilter implements IPageObjectFilter
{
	private ILanguage srcLanguage;

	private ILanguage tgtLanguage;

	public TranslationFilter(String pair)
	{
		String[] langpair = pair.split("[\\-\\,]");
		this.srcLanguage = Language.getByCode(langpair[0]);
		this.tgtLanguage = Language.getByCode(langpair[1]);
	}

	@Override
	public boolean allow(PageObject o)
	{
		if(Objects.isNull(tgtLanguage)|| Objects.isNull(srcLanguage))
		{
			return false;
		}
		if(!(o instanceof WiktionaryTranslation))
		{
			return false;
		}
		return ((WiktionaryTranslation) o).getTargetLanguage()==tgtLanguage && ((WiktionaryTranslation) o).getSourceLanguage()==srcLanguage;
	}
}
