package com.aegaeon.zamenhof.parser;

import com.aegaeon.zamenhof.parser.utils.ILanguage;

public class WiktionaryTranslation extends PageObject{

    private String sourceWord;

    private ILanguage sourceLanguage;

    private String targetWord;

    private ILanguage targetLanguage;

    private String sense;

    private String wordType;

    public WiktionaryTranslation(WiktionaryPage page,ILanguage sourceLanguage, String sourceWord, ILanguage targetLanguage, String targetWord)
    {
        super(page);
        this.sourceLanguage = sourceLanguage;
        this.sourceWord = sourceWord;
        this.targetLanguage = targetLanguage;
        this.targetWord = targetWord;
    }

    public String getSourceWord() {
        return sourceWord;
    }

    public ILanguage getSourceLanguage() {
        return sourceLanguage;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public ILanguage getTargetLanguage() {
        return targetLanguage;
    }

    public String getSense()
    {
        return this.sense;
    }

    public void setSense(String sense)
    {
        this.sense = sense;
    }

    public String getWordType()
    {
        return this.wordType;
    }

    public void setWordType(String wordType)
    {
        this.wordType = wordType;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getSourceLanguage().getName()).append(",");
        sb.append(this.getSourceWord()).append(",");
        sb.append(this.getTargetLanguage().getName()).append(",");
        sb.append(this.getTargetWord()).append(",");
        sb.append(this.getWordType()).append(",");
        sb.append(this.getPageid()).append(",");
        sb.append(this.getRevision()).append(",");
        sb.append(this.getTimestamp()).append(",");
        sb.append(this.getSense());
        return sb.toString();
    }
}

