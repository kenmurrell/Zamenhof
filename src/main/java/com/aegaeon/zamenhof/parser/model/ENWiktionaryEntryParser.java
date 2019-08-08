package com.aegaeon.zamenhof.parser.model;

import com.aegaeon.zamenhof.parser.utils.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ENWiktionaryEntryParser implements IWiktionaryEntryParser{

    private static final Pattern SENSE_PATTERN = Pattern.compile("\\{\\{trans\\-top\\|(.*)\\}\\}");

    private List<PageObject> translations;

    public ENWiktionaryEntryParser() {
        translations = new ArrayList<>();
    }

    @Override
    public void parse(final WiktionaryPage page, String text) {
        Iterator<String> iter = Arrays.asList(StringUtils.splitByWholeSeparator(text,"\n")).iterator();
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
                    currentLanguage = Language.getByName(formatHeader(line));
                    currentWordtype = "";
                    currentSubheader1 = "";
                    currentSubheader2 = "";
                } else if (3 == getLevel(line, 0)) {
                    currentWordtype = formatHeader(line);
                    currentSubheader1 = "";
                } else if (4 == getLevel(line, 0)) {
                    currentSubheader1 = formatHeader(line);
                    currentSubheader2 = "";
                } else if (5 == getLevel(line, 0)) {
                    currentSubheader2 = formatHeader(line);
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
                                String targetWord = cleanWord(template.getNumberedParameter(2));
	                              //pages marked as "<word>/translations" are only placeholders for the translations of a main page
                                String sourceWord = page.getTitle().replaceAll("\\/translations","");
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

    private String cleanWord(String dirtyStr)
    {
        return StringTools.removePunc(dirtyStr);
    }

    private String formatHeader(String header)
    {
        return StringUtils.upperCase(StringUtils.strip(StringTools.remove(header,'=')));
    }

    private boolean isHead(String line) {
        return line.startsWith("=") && line.endsWith("=");
    }

    private boolean isTranslationSection(ILanguage language, String subheader1, String subheader2) {
        return Objects.equals(Language.ENGLISH, language) && (subheader1.equals("TRANSLATIONS") || subheader2.equals("TRANSLATIONS"));
    }

    @Override
    public List<PageObject> getPageObjects()
    {
        return this.translations;
    }

}
