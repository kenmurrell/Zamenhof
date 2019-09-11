package com.aegaeon.zamenhof.parser.model;

import com.aegaeon.zamenhof.parser.utils.ILanguage;
import com.aegaeon.zamenhof.parser.utils.IWordType;
import com.aegaeon.zamenhof.parser.utils.WordType;

import java.util.HashMap;
import java.util.Map;

public class WiktionaryTranslation extends PageObject
{

	private String sourceWord;

	private ILanguage sourceLanguage;

	private String targetWord;

	private ILanguage targetLanguage;

	private String sense;

	private IWordType wordType;

	private WiktionaryTranslation(WiktionaryPage page, ILanguage sourceLanguage, String sourceWord, ILanguage targetLanguage, String targetWord, IWordType wordType)
	{
		super(page);
		this.sourceLanguage = sourceLanguage;
		this.sourceWord = sourceWord;
		this.targetLanguage = targetLanguage;
		this.targetWord = targetWord;
		this.wordType = wordType;
	}

	public static WiktionaryTranslation create(WiktionaryPage page, ILanguage srcLang, String srcWord, ILanguage tgtLang, String tgtWord, IWordType wordType)
	{
		return new WiktionaryTranslation(page, srcLang, srcWord, tgtLang, tgtWord, (wordType == null) ? WordType.UNKNOWN : wordType);
	}

	public String getSourceWord()
	{
		return sourceWord;
	}

	public String getTargetWord()
	{
		return targetWord;
	}

	public ILanguage getTargetLanguage()
	{
		return targetLanguage;
	}

	public ILanguage getSourceLanguage()
	{
		return sourceLanguage;
	}

	public String getSense()
	{
		return sense;
	}

	public void setSense(String sense)
	{
		this.sense = sense;
	}

	@Override
	public Map<String, String> attributes()
	{
		return new HashMap<String, String>()
		{{
			put("sourceword", sourceWord);
			put("sourcelanguage", sourceLanguage.getName());
			put("targetword", targetWord);
			put("targetlanguage", targetLanguage.getName());
			put("wordtype", wordType.getName());
			put("pageid", String.valueOf(getPage().getId()));
			put("sense", sense);
		}};
	}
}

