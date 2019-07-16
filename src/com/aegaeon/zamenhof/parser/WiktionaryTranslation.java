package com.aegaeon.zamenhof.parser;

import com.aegaeon.zamenhof.parser.utils.Language;

import java.util.ArrayList;
import java.util.List;

public class WiktionaryTranslation implements PageObject{

    private String sourceWord;

    private Language sourceLanguage;

    private List<String> targetWords;

    private Language targetLanguage;

    private WiktionaryPage page;

    public WiktionaryTranslation()
    {
        targetWords = new ArrayList<>();
    }

    public String getSourceWord() {
        return sourceWord;
    }

    public void setSourceWord(String sourceWord) {
        this.sourceWord = sourceWord;
    }

    public Language getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(Language sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public List<String> getTargetWord() {
        return targetWords;
    }

    public void addTargetWord(List<String> targetWords) {
        this.targetWords.addAll(targetWords);
    }

    public void addTargetWord(String targetWord) {
        this.targetWords.add(targetWord);
    }

    public Language getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(Language targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public WiktionaryPage getPage()
    {
        return this.page;
    }
}

