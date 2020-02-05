package com.github.kenmurrell.zamenhof.model;

import com.github.kenmurrell.zamenhof.utils.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

public class FRWiktionaryEntryParser implements IWiktionaryEntryParser
{
	private static final Pattern HEADER_PATTERN = Pattern.compile("\\={2,4}\\s?\\{\\{.*\\}\\}\\s?\\={2,4}");

	private static final Set<String> TRANSLATION_TEMPLATES = new HashSet<>(Arrays.asList("trad", "trad+", "trad-", "trad--"));

	private List<PageObject> translations;

	public FRWiktionaryEntryParser()
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
				Optional<Template> headerTemplate = Optional.ofNullable(Template.create(formatHeader(line)));
				if (2 == level && headerTemplate.isPresent()) {
					currentLanguage = fetchHeaderLanguage(headerTemplate.get());
					currentWordtype = null;
					currentSubheader = "";
				}
				else if (3 == level && headerTemplate.isPresent()) {
					currentWordtype = fetchWordTypeHeader(headerTemplate.get());
					currentSubheader = "";
				}
				else if (4 == level && headerTemplate.isPresent()) {
					currentSubheader = fetchSubHeader(headerTemplate.get());
				}
			}
			else if (!line.isEmpty() && isTranslationSection(currentLanguage, currentWordtype, currentSubheader)) {
				List<Template> templates = Template.createAll(line);
				if (templates.size() == 1 && templates.get(0).getNumberedParameter(0).isPresent() && templates.get(0).getNumberedParameter(0).get().equals("trad-début")) {
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
		return HEADER_PATTERN.matcher(line).find();
	}

	private ILanguage fetchHeaderLanguage(Template template)
	{
		Optional<String> lang = template.getNumberedParameter(1);
		return lang.map(Language::getByCode).orElse(null);
	}

	private IWordType fetchWordTypeHeader(Template template)
	{
		Optional<String> wordtype = template.getNumberedParameter(1);
		return wordtype.map(name -> WordType.getByFRName(name.toUpperCase())).orElse(null);
	}

	private String fetchSubHeader(Template template)
	{
		Optional<String> subheader = template.getNumberedParameter(1);
		return subheader.orElse("");
	}

	private boolean isTranslationSection(ILanguage language, IWordType wordType, String subheader)
	{
		return Objects.equals(Language.FRENCH, language) && wordType != null && subheader.equals("traductions");
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

	private String formatHeader(String header)
	{
		return StringUtils.strip(StringTools.remove(header, '='));
	}

	@Override
	public List<PageObject> getPageObjects()
	{
		return this.translations;
	}

}
