package com.github.kenmurrell.zamenhof.model;

import com.github.kenmurrell.zamenhof.utils.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class PTWiktionaryEntryParser implements IWiktionaryEntryParser
{
	private static final Set<String> TRANSLATION_TEMPLATES = new HashSet<>(Arrays.asList("trad", "t"));

	private List<PageObject> translations;

	public PTWiktionaryEntryParser()
	{
		translations = new ArrayList<>();
	}

	@Override
	public void parse(final WiktionaryPage page, String text)
	{
		Iterator<String> iter = Arrays.asList(StringUtils.splitByWholeSeparator(text, "\n")).iterator();
		ILanguage currentLanguage = null;
		IWordType currentWordtype = null;
		String currentSubheader = "";
		String currentSense = "";
		while (iter.hasNext()) {
			String line = iter.next().trim();
			if (isHeader(line)) {
				int level = getLevel(line, 0);
				if (1 == level) {
					currentLanguage = fetchHeaderLanguage(line);
					currentWordtype = null;
					currentSubheader = "";
				}
				else if (2 == level) {
					currentWordtype = fetchWordTypeHeader(line);
					currentSubheader = "";
				}
				else if (3 == level) {
					currentSubheader = fetchSubHeader(line);
				}
			}
			else if (!line.isEmpty() && isTranslationSection(currentLanguage, currentWordtype, currentSubheader)) {
				List<Template> templates = Template.createAll(line);
				if (templates.size() == 1 && templates.get(0).getNumberedParameter(0).isPresent() && templates.get(0).getNumberedParameter(0).get().equals("tradini")) {
					currentSense = templates.get(0).getNumberedParameter(1).orElse("");
				}
				else {
					for (Template template : templates) {
						if (isTranslationEntry(template)) {
							ILanguage targetLang = template.getNumberedParameter(1).map(Language::getByCode).orElse(null);
							if (targetLang != null) {
								String targetWord = template.getNumberedParameter(2).map(this::cleanWord).get();
								WiktionaryTranslation translation = WiktionaryTranslation.create(page, currentLanguage, page.getTitle(), targetLang, targetWord, currentWordtype);
								translation.setSense(currentSense);
								this.translations.add(translation);
							}
						}
					}
				}
			}
		}
	}

	private boolean isHeader(String line)
	{
		return line.startsWith("=") && line.endsWith("=");
	}

	private ILanguage fetchHeaderLanguage(String line)
	{
		return Language.getByCode(StringUtils.strip(line, "{}=-"));
	}

	private IWordType fetchWordTypeHeader(String line)
	{
		return WordType.getByPTName(StringUtils.remove(line, '=').toUpperCase());
	}

	private String fetchSubHeader(String line)
	{
		return StringUtils.remove(line, '=');
	}

	private boolean isTranslationSection(ILanguage language, IWordType wordType, String subheader)
	{
		return Objects.equals(Language.PORTUGUESE, language) && wordType != null && subheader.equals("Tradução");
	}

	private boolean isTranslationEntry(Template template)
	{
		Optional<String> name = template.getNumberedParameter(0);
		return template.numberedParameterCount() >= 3 && name.isPresent() && TRANSLATION_TEMPLATES.contains(name.get());
	}

	private int getLevel(String line, int level)
	{
		return line.startsWith("=") ? getLevel(line.substring(1), level + 1) : level;
	}

	private String cleanWord(String dirtyStr)
	{
		return StringTools.removePunc(dirtyStr);
	}

	@Override
	public List<PageObject> getPageObjects()
	{
		return this.translations;
	}

}
