package com.aegaeon.zamenhof.parser.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LanguageTest
{
	@Test
	public void testLanguageFrench()
	{
		ILanguage lang = Language.getByCode("fr");
		assertNotNull(lang);
		assertEquals("French",lang.getName());
	}

	@Test
	public void testLanguageLuxemburgish()
	{
		ILanguage lang = Language.getByCode("lb");
		assertNotNull(lang);
		assertEquals("lb",lang.getCode());
		assertEquals("Luxembourgish",lang.getName());
	}

	@Test
	public void testLanguageArabic()
	{
		ILanguage lang = Language.getByName("Arabic".toUpperCase());
		assertNotNull(lang);
		assertEquals("ar",lang.getCode());
		assertEquals("Arabic",lang.getName());
	}

}
