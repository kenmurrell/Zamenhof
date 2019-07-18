package com.aegaeon.zamenhof.parser;

import com.aegaeon.zamenhof.parser.utils.ILanguage;
import com.aegaeon.zamenhof.parser.utils.Language;

import java.util.ArrayList;
import java.util.List;

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
}

