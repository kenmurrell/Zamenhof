package com.aegaeon.zamenhof.parser.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WordType implements IWordType
{
	private static final Logger logger = Logger.getLogger(WordType.class.getName());

	private static Map<String,IWordType> allowedIndex;

	private static boolean initialized;

	public static IWordType UKNOWN = get("UNKNOWN");

	private String name;

	private WordType(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	private static IWordType get(String name)
	{
		initialize();
		return name==null? null : allowedIndex.get(name);
	}

	public static IWordType getByName(final String name)
	{
		return get(name);
	}

	public static void initialize()
	{
		if(initialized)
		{
			return;
		}
		allowedIndex = new TreeMap<>();
		try{
			InputStreamReader inreader = new InputStreamReader(Language.class.getResourceAsStream("/com/aegaeon/zamenhof/resources/allowedwordtypes.txt"), StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(inreader);
			String line;
			while((line = reader.readLine()) !=null)
			{
				IWordType wordtype = new WordType(line);
				allowedIndex.put(line,wordtype);
			}
			initialized = true;
		}
		catch (IOException ex)
		{
			logger.log(Level.SEVERE, "garbel shit is happening");
		}

	}
}
