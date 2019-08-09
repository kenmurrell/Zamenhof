package com.aegaeon.zamenhof.parser.model;

import com.aegaeon.zamenhof.parser.utils.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FRWiktionaryEntryParser implements IWiktionaryEntryParser
{
	private static final Pattern SENSE_PATTERN = Pattern.compile("\\{\\{trad\\-début\\|(.*)\\}\\}");

	private static final Pattern HEADER_PATTERN = Pattern.compile("\\={2,4}\\s?\\{\\{.*\\}\\}\\s?\\={2,4}");

	private List<PageObject> translations;

	public FRWiktionaryEntryParser() {
		translations = new ArrayList<>();
	}

	@Override
	public void parse(final WiktionaryPage page, String text) {
		Iterator<String> iter = Arrays.asList(StringUtils.splitByWholeSeparator(text,"\n")).iterator();
		ILanguage currentLanguage = null;
		IWordType currentWordtype = null;
		String currentSubheader = "";
		String currentSense = "";
		//TODO: this is a really shitty parser....we can do better
		while (iter.hasNext()) {
			String line = iter.next().trim();
			if (isHeader(line)) {
				int level = getLevel(line,0);
				Optional<Template> headerTemplate = Optional.ofNullable(Template.create(StringTools.remove(line,'=').trim()));
				if (2==level && headerTemplate.isPresent()) {
					currentLanguage = fetchHeaderLanguage(headerTemplate.get());
					currentWordtype = null;
					currentSubheader = "";
				} else if (3==level && headerTemplate.isPresent()) {
					currentWordtype = fetchWordTypeHeader(headerTemplate.get());
					currentSubheader = "";
				} else if (4==level && headerTemplate.isPresent()) {
					currentSubheader = fetchSubHeader(headerTemplate.get());
				}
			}
			else if (!line.isEmpty() && isTranslationSection(currentLanguage, currentWordtype,currentSubheader))
			{
				Matcher senseMatch = SENSE_PATTERN.matcher(line);
				if (senseMatch.find()) {
					currentSense = senseMatch.group(1);
				} else {
					List<Template> templates = Template.createAll(line);
					for (Template template : templates) {
						if (isTranslationEntry(template)) {
							ILanguage targetLang = Language.getByCode(template.getNumberedParameter(1).get());
							if(targetLang!=null) {
								String targetWord = cleanWord(template.getNumberedParameter(2).get());
								WiktionaryTranslation translation = WiktionaryTranslation.create(page, currentLanguage, page.getTitle(), targetLang, targetWord,currentWordtype);
								translation.setSense(currentSense);
								this.translations.add(translation);
							}
						}
					}
				}
			}
		}
	}

	private boolean isHeader(String line) {
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

	private boolean isTranslationSection(ILanguage language, IWordType wordType, String subheader) {
		return Objects.equals(Language.FRENCH, language) && wordType!=null && subheader.equals("traductions");
	}

	private boolean isTranslationEntry(Template template) {
		return template.numberedParameterCount() >= 3 && Arrays.asList("trad","trad+","trad-","trad--").contains(template.getNumberedParameter(0).get());
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
		return StringUtils.upperCase(StringUtils.strip(StringTools.remove(header, '=')));
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
