package com.aegaeon.zamenhof.parser.model;

import com.aegaeon.zamenhof.parser.utils.Language;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

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

		assertEquals(8,objs.size());
		WiktionaryTranslation obj = (WiktionaryTranslation) objs.get(0);
		assertNotNull(obj);
		assertEquals("if looks could kill",obj.getSourceWord());
		assertEquals(Language.CZECH,obj.getTargetLanguage());
		assertEquals("kdyby pohled mohl zabíjet",obj.getTargetWord());
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

		assertEquals(40,objs.size());
		WiktionaryTranslation obj = (WiktionaryTranslation) objs.get(0);
		assertNotNull(obj);
		assertEquals("if looks could kill",obj.getSourceWord());
		assertEquals(Language.CZECH,obj.getTargetLanguage());
		assertEquals("kdyby pohled mohl zabíjet",obj.getTargetWord());

	}

	private String retrieve(File testfile) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		FileInputStream fis = new FileInputStream("src/test/resources/"+testfile);
		InputStreamReader isr = new InputStreamReader(fis);
		String line;
		try (BufferedReader reader = new BufferedReader(isr)) {
			while((line=reader.readLine())!=null)
			{
				sb.append(line);
				sb.append("\n");
			}
		}
		catch (IOException e)
		{
			throw new IOException();
		}
		return sb.toString();
	}
}
