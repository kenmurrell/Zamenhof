package com.aegaeon.zamenhof.parser;

import com.aegaeon.zamenhof.parser.utils.ILanguage;
import com.aegaeon.zamenhof.parser.utils.Language;
import com.aegaeon.zamenhof.parser.utils.Template;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WiktionaryEntryParser {

    private static Pattern SENSE_PATTERN = Pattern.compile("\\{\\{trans\\-top\\|(.*)\\}\\}");

    private Logger logger = Logger.getLogger(WiktionaryEntryParser.class.getName());

    private List<PageObject> translations;


    public WiktionaryEntryParser() {
        translations = new ArrayList<>();
    }


    public void parse(final WiktionaryPage page, String text) {
        Iterator<String> lines = Arrays.asList(text.split("\n")).iterator();
        ILanguage currentLanguage = null;
        String currentWordtype = "";
        String currentSubheader = "";
        String currentSense = "";
        //TODO: this is a really shitty parser....we can do better
        while (lines.hasNext()) {
            String line = lines.next().trim();
            if (isHead(line)) {
                if (2 == getLevel(line, 0))
                {
                    currentLanguage = Language.getByName(line.replace("=", "").trim().toUpperCase());
                }
                else if (3 == getLevel(line, 0))
                {
                    currentWordtype = line.replace("=", "").trim().toUpperCase();
                }
                else if (4 == getLevel(line, 0))
                {
                    currentSubheader = line.replace("=", "").trim().toUpperCase();
                }
            }
            else if (isTranslationSection(currentLanguage, currentSubheader))
            {
                Matcher senseMatch = SENSE_PATTERN.matcher(line);
                if (senseMatch.find())
                {
                    currentSense = senseMatch.group(1);
                } else {
                    List<Template> templates = Template.createAll(line);
                    for (Template template : templates) {
                        if (verifyTranslation(template)) {
                            ILanguage targetLang = Language.getByCode(template.getNumberedParameter(1));
                            if(targetLang!=null) {
                                String targetWord = template.getNumberedParameter(2);
                                String sourceWord = page.getTitle();
                                WiktionaryTranslation translation = new WiktionaryTranslation(page, currentLanguage, sourceWord, targetLang, targetWord);
                                translation.setSense(currentSense);
                                translation.setWordType(currentWordtype);
                                this.translations.add(translation);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean verifyTranslation(Template template) {
        return Arrays.asList("t", "t+").contains(template.getNumberedParameter(0)) && template.numberedParameterCount() >= 3;
    }


    private int getLevel(String line, int level) {
        return line.startsWith("=") ? getLevel(line.substring(1), level + 1) : level;
    }

    private boolean isHead(String line) {
        return line.startsWith("=") && line.endsWith("=");
    }

    private boolean isTranslationSection(ILanguage language, String subheader) {
        return Objects.equals(Language.ENGLISH, language) && subheader.equals("TRANSLATIONS");
    }

    public List<PageObject> getPageObjects()
    {
        return this.translations;
    }

}
