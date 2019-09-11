package com.aegaeon.zamenhof.parser.model;

import com.aegaeon.zamenhof.parser.utils.Language;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WiktionaryEntryParserTest
{

	@Test
	public void expectENTranslations() throws IOException
	{
		WiktionaryPage page = new WiktionaryPage();
		page.setTitle("if looks could kill");
		page.setId("552590");
		page.setRevision("51353509");
		page.setTimestamp("2019-01-25T05:38:10Z");
		IWiktionaryEntryParser entryParser = new ENWiktionaryEntryParser();
		entryParser.parse(page, retrieve(new File("if looks could kill.txt")));
		List<PageObject> objs = entryParser.getPageObjects();

		assertEquals(8, objs.size());
		WiktionaryTranslation obj = (WiktionaryTranslation) objs.get(0);
		assertNotNull(obj);
		assertEquals("if looks could kill", obj.getSourceWord());
		assertEquals("kdyby pohled mohl zabíjet", obj.getTargetWord());
		assertEquals(Language.CZECH, obj.getTargetLanguage());
		assertEquals("used to characterize a look of strong hostility", obj.getSense());
	}

	@Test
	public void expectFRTranslations() throws IOException
	{
		WiktionaryPage page = new WiktionaryPage();
		page.setTitle("truc");
		page.setId("552590");
		page.setRevision("51353509");
		page.setTimestamp("2019-01-25T05:38:10Z");
		IWiktionaryEntryParser entryParser = new FRWiktionaryEntryParser();
		entryParser.parse(page, retrieve(new File("truc.txt")));
		List<PageObject> objs = entryParser.getPageObjects();

		assertEquals(34, objs.size());
		WiktionaryTranslation obj1 = (WiktionaryTranslation) objs.get(4);
		assertNotNull(obj1);
		assertEquals("truc", obj1.getSourceWord());
		assertEquals("twist", obj1.getTargetWord());
		assertEquals(Language.ENGLISH, obj1.getTargetLanguage());
		assertEquals("Astuce", obj1.getSense());

		WiktionaryTranslation obj2 = (WiktionaryTranslation) objs.get(23);
		assertNotNull(obj2);
		assertEquals("truc", obj2.getSourceWord());
		assertEquals("stuff", obj2.getTargetWord());
		assertEquals(Language.ENGLISH, obj2.getTargetLanguage());
		assertEquals("Quelque chose dont on ne connaît pas le nom", obj2.getSense());
	}

	@Test
	public void expectPTTranslations() throws IOException
	{
		WiktionaryPage page = new WiktionaryPage();
		page.setTitle("guitarra");
		page.setId("80322");
		page.setRevision("2475380");
		page.setTimestamp("2018-01-07T16:57:12Z");
		IWiktionaryEntryParser entryParser = new PTWiktionaryEntryParser();
		entryParser.parse(page, retrieve(new File("guitarra.txt")));
		List<PageObject> objs = entryParser.getPageObjects();

		assertEquals(50, objs.size());
		WiktionaryTranslation obj = (WiktionaryTranslation) objs.get(16);
		assertNotNull(obj);
		assertEquals("guitarra", obj.getSourceWord());
		assertEquals("guitare", obj.getTargetWord());
		assertEquals(Language.FRENCH, obj.getTargetLanguage());
		assertTrue(obj.getSense().isEmpty());
	}

	private String retrieve(File testfile) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		FileInputStream fis = new FileInputStream("src/test/resources/" + testfile);
		InputStreamReader isr = new InputStreamReader(fis);
		String line;
		try (BufferedReader reader = new BufferedReader(isr)) {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		}
		catch (IOException e) {
			throw new IOException();
		}
		return sb.toString();
	}
}
