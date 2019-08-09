package com.aegaeon.zamenhof.parser.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WordType implements IWordType
{
	private static final Logger logger = Logger.getLogger(WordType.class.getName());

	private static Map<String,IWordType> allowedIndex;

	private static Map<String,IWordType> frenchIndex;

	private static boolean initialized;

	public static final IWordType UNKNOWN = get("UNKNOWN");

	private final String name;

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

	public static IWordType getByFRName(final String name)
	{
		initialize();
		return name==null? null : frenchIndex.get(name);
	}

	private static void initialize()
	{
		if(initialized)
		{
			return;
		}
		allowedIndex = new HashMap<>();
		frenchIndex = new HashMap<>();
		try{
			InputStream enstream = WordType.class.getResourceAsStream("/base_word_types.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(enstream, StandardCharsets.UTF_8));
			String line;
			while((line = reader.readLine()) !=null)
			{
				IWordType wordtype = new WordType(line);
				allowedIndex.put(line,wordtype);
			}

			InputStream frstream = WordType.class.getResourceAsStream("/fr_word_types.txt");
			BufferedReader fr_reader = new BufferedReader(new InputStreamReader(frstream, StandardCharsets.UTF_8));
			while((line = fr_reader.readLine()) !=null)
			{
				String[] fields = line.split("\\s\\s\\s");
				String frname = fields[0];
				IWordType enname = allowedIndex.get(fields[1]);
				frenchIndex.put(frname,enname);
			}
			initialized = true;
		}
		catch (IOException ex)
		{
			logger.log(Level.SEVERE, "garbel shit is happening");
		}
	}
}
