package com.aegaeon.zamenhof.parser;

import com.aegaeon.zamenhof.parser.utils.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WiktionaryEntryParser {

    private static final Pattern SENSE_PATTERN = Pattern.compile("\\{\\{trans\\-top\\|(.*)\\}\\}");

    private List<PageObject> translations;

    public WiktionaryEntryParser() {
        translations = new ArrayList<>();
    }

    public void parse(final WiktionaryPage page, String text) {
        Iterator<String> iter = Arrays.asList(text.split("\n")).iterator();
        ILanguage currentLanguage = null;
        String currentWordtype = "";
        String currentSubheader1 = "";
        String currentSubheader2 = "";
        String currentSense = "";
        //TODO: this is a really shitty parser....we can do better
        while (iter.hasNext()) {
            String line = iter.next().trim();
            if (isHead(line)) {
                if (2 == getLevel(line, 0)) {
                    currentLanguage = Language.getByName(StringTools.remove(line,'=').trim().toUpperCase());
                    currentWordtype = "";
                    currentSubheader1 = "";
                    currentSubheader2 = "";
                } else if (3 == getLevel(line, 0)) {
                    currentWordtype = StringTools.remove(line,'=').trim().toUpperCase();
                    currentSubheader1 = "";
                } else if (4 == getLevel(line, 0)) {
                    currentSubheader1 = StringTools.remove(line,'=').trim().toUpperCase();
                    currentSubheader2 = "";
                } else if (5 == getLevel(line, 0)) {
                    currentSubheader2 = StringTools.remove(line,'=').trim().toUpperCase();
                }
            }
            else if (!line.isEmpty() && isTranslationSection(currentLanguage, currentSubheader1,currentSubheader2))
            {
                Matcher senseMatch = SENSE_PATTERN.matcher(line);
                if (senseMatch.find()) {
                    currentSense = senseMatch.group(1);
                } else {
                    List<Template> templates = Template.createAll(line);
                    for (Template template : templates) {
                        if (isTranslation(template)) {
                            ILanguage targetLang = Language.getByCode(template.getNumberedParameter(1));
                            if(targetLang!=null) {
                                String targetWord = template.getNumberedParameter(2);
                                String sourceWord = page.getTitle();
                                IWordType wordType = Optional.ofNullable(WordType.getByName(currentWordtype)).orElse(WordType.getByName(currentSubheader1));
                                WiktionaryTranslation translation = WiktionaryTranslation.create(page, currentLanguage, sourceWord, targetLang, targetWord,wordType);
                                translation.setSense(currentSense);
                                this.translations.add(translation);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isTranslation(Template template) {
        return Arrays.asList("t", "t+").contains(template.getNumberedParameter(0)) && template.numberedParameterCount() >= 3;
    }

    private int getLevel(String line, int level) {
        return line.startsWith("=") ? getLevel(line.substring(1), level + 1) : level;
    }

    private boolean isHead(String line) {
        return line.startsWith("=") && line.endsWith("=");
    }

    private boolean isTranslationSection(ILanguage language, String subheader1, String subheader2) {
        return Objects.equals(Language.ENGLISH, language) && (subheader1.equals("TRANSLATIONS") || subheader2.equals("TRANSLATIONS"));
    }

    public List<PageObject> getPageObjects()
    {
        return this.translations;
    }

}
