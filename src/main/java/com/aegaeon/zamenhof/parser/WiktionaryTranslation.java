package com.aegaeon.zamenhof.parser;

import com.aegaeon.zamenhof.parser.utils.ILanguage;
import com.aegaeon.zamenhof.parser.utils.IWordType;
import com.aegaeon.zamenhof.parser.utils.WordType;

public class WiktionaryTranslation extends PageObject{

    private String sourceWord;

    private ILanguage sourceLanguage;

    private String targetWord;

    private ILanguage targetLanguage;

    private String sense;

    private IWordType wordType;

    public WiktionaryTranslation(WiktionaryPage page,ILanguage sourceLanguage, String sourceWord, ILanguage targetLanguage, String targetWord, IWordType wordType)
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
        return new WiktionaryTranslation(page, srcLang,srcWord,tgtLang,tgtWord, (wordType==null)? WordType.UKNOWN : wordType);
    }

    public void setSense(String sense)
    {
        this.sense = sense;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.sourceLanguage.getName()).append(",");
        sb.append(this.sourceWord).append(",");
        sb.append(this.targetLanguage.getName()).append(",");
        sb.append(this.targetWord).append(",");
        sb.append(this.wordType.getName()).append(",");
        sb.append(this.getPageid()).append(",");
        sb.append(this.getRevision()).append(",");
        sb.append(this.getStringTimestamp()).append(",");
        sb.append(this.sense);
        return sb.toString();
    }
}

