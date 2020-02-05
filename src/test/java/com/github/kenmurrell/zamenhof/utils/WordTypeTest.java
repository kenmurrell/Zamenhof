package com.github.kenmurrell.zamenhof.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WordTypeTest
{
	@Test
	public void testWordTypePronoun()
	{
		IWordType type = WordType.getByENName("noun".toUpperCase());
		assertNotNull(type);
		assertEquals("NOUN", type.getName());
	}

	@Test
	public void testWordTypeBanned()
	{
		IWordType type = WordType.getByENName("death".toUpperCase());
		assertNull(type);
	}

	@Test
	public void testWordTypeUnknown()
	{
		IWordType type = WordType.UNKNOWN;
		assertNotNull(type);
		assertEquals("UNKNOWN", type.getName());
	}

	@Test
	public void testFrenchVerb()
	{
		IWordType type = WordType.getByFRName("verbe".toUpperCase());
		assertNotNull(type);
		assertEquals("VERB", type.getName());
	}
}
