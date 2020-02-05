package com.github.kenmurrell.zamenhof.model;

import com.github.kenmurrell.zamenhof.utils.*;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

public class ENWiktionaryEntryParser implements IWiktionaryEntryParser
{

	private static final Pattern TRANSLATION_CATEGORY_PATTERN = Pattern.compile("\\/translations");

	private List<PageObject> translations;

	public ENWiktionaryEntryParser()
	{
		translations = new ArrayList<>();
	}

	@Override
	public void parse(final WiktionaryPage page, String text)
	{
		Iterator<String> iter = Arrays.asList(StringUtils.splitByWholeSeparator(text, "\n")).iterator();
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
				}
				else if (3 == getLevel(line, 0)) {
					currentWordtype = formatHeader(line);
					currentSubheader1 = "";
				}
				else if (4 == getLevel(line, 0)) {
					currentSubheader1 = formatHeader(line);
					currentSubheader2 = "";
				}
				else if (5 == getLevel(line, 0)) {
					currentSubheader2 = formatHeader(line);
				}
			}
			else if (!line.isEmpty() && isTranslationSection(currentLanguage, currentSubheader1, currentSubheader2)) {
				List<Template> templates = Template.createAll(line);
				if (templates.size() == 1 && templates.get(0).getNumberedParameter(0).isPresent() && templates.get(0).getNumberedParameter(0).get().equals("trans-top")) {
					currentSense = templates.get(0).getNumberedParameter(1).orElse("");
				}
				else {
					for (Template template : templates) {
						if (isTranslation(template)) {
							ILanguage targetLang = template.getNumberedParameter(1).map(Language::getByCode).orElse(null);
							if (targetLang != null) {
								//TODO: fix the way template params are handled as optionals here
								String targetWord = template.getNumberedParameter(2).map(this::cleanWord).get();
								//pages marked as "<word>/translations" are only placeholders for the translations of a main page
								String sourceWord = RegExUtils.removeAll(page.getTitle(), TRANSLATION_CATEGORY_PATTERN);
								IWordType wordType = Optional.ofNullable(WordType.getByENName(currentWordtype)).orElse(WordType.getByENName(currentSubheader1));
								WiktionaryTranslation translation = WiktionaryTranslation.create(page, currentLanguage, sourceWord, targetLang, targetWord, wordType);
								translation.setSense(currentSense);
								this.translations.add(translation);
							}
						}
					}
				}
			}
		}
	}

	private boolean isTranslation(Template template)
	{
		Optional<String> name = template.getNumberedParameter(0);
		return name.isPresent() && template.numberedParameterCount() >= 3 && Arrays.asList("t", "t+").contains(name.get());
	}

	private int getLevel(String line, int level)
	{
		return line.startsWith("=") ? getLevel(line.substring(1), level + 1) : level;
	}

	private String cleanWord(String dirtyStr)
	{
		return StringTools.removePunc(dirtyStr);
	}

	private String formatHeader(String header)
	{
		return StringUtils.upperCase(StringUtils.strip(StringTools.remove(header, '=')));
	}

	private boolean isHead(String line)
	{
		return line.startsWith("=") && line.endsWith("=");
	}

	private boolean isTranslationSection(ILanguage language, String subheader1, String subheader2)
	{
		return Objects.equals(Language.ENGLISH, language) && (subheader1.equals("TRANSLATIONS") || subheader2.equals("TRANSLATIONS"));
	}

	@Override
	public List<PageObject> getPageObjects()
	{
		return this.translations;
	}

}
