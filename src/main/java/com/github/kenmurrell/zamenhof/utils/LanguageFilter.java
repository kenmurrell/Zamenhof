package com.github.kenmurrell.zamenhof.utils;

import com.github.kenmurrell.zamenhof.model.PageObject;
import com.github.kenmurrell.zamenhof.model.WiktionaryTranslation;

import java.util.Objects;

public class LanguageFilter implements IPageObjectFilter
{
	private ILanguage srcLanguage;

	private ILanguage tgtLanguage;

	public LanguageFilter(String pair)
	{
		String[] langpair = pair.split("[\\-\\,]");
		this.srcLanguage = Language.getByCode(langpair[0]);
		this.tgtLanguage = Language.getByCode(langpair[1]);
	}

	@Override
	public boolean allow(PageObject o)
	{
		if (Objects.isNull(tgtLanguage) || Objects.isNull(srcLanguage)) {
			return false;
		}
		if (!(o instanceof WiktionaryTranslation)) {
			return false;
		}
		return ((WiktionaryTranslation) o).getTargetLanguage() == tgtLanguage && ((WiktionaryTranslation) o).getSourceLanguage() == srcLanguage;
	}
}
