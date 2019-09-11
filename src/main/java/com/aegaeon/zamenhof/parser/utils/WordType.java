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

	private static Map<String, IWordType> allowedIndex;

	private static Map<String, IWordType> frenchIndex;

	private static Map<String, IWordType> portugueseIndex;

	private static boolean initialized;

	public static final IWordType UNKNOWN = get("UNKNOWN");

	private final String name;

	private WordType(String name)
	{
		this.name = name;
	}

	private static IWordType get(String name)
	{
		initialize();
		return name == null ? null : allowedIndex.get(name);
	}

	public static IWordType getByENName(final String name)
	{
		initialize();
		return name == null ? null : allowedIndex.get(name);
	}

	public static IWordType getByFRName(final String name)
	{
		initialize();
		return name == null ? null : frenchIndex.get(name);
	}

	public static IWordType getByPTName(final String name)
	{
		initialize();
		return name == null ? null : portugueseIndex.get(name);
	}

	private static void initialize()
	{
		if (initialized) {
			return;
		}
		allowedIndex = new HashMap<>();
		frenchIndex = new HashMap<>();
		portugueseIndex = new HashMap<>();
		try {
			InputStream enstream = WordType.class.getResourceAsStream("/en_pos_codes.txt");
			BufferedReader enreader = new BufferedReader(new InputStreamReader(enstream, StandardCharsets.UTF_8));
			String line;
			while ((line = enreader.readLine()) != null) {
				IWordType wordtype = new WordType(line);
				allowedIndex.put(line, wordtype);
			}
			enstream.close();
			enreader.close();

			load("fr_pos_codes.txt", frenchIndex);
			load("pt_pos_codes.txt", portugueseIndex);

			initialized = true;
		}
		catch (IOException ex) {
			logger.log(Level.SEVERE, "garbel shit is happening");
		}
	}

	private static void load(String codefile, Map<String, IWordType> index) throws IOException
	{
		InputStream stream = WordType.class.getResourceAsStream("/" + codefile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] fields = line.split("\\s\\s\\s");
			String name = fields[0];
			IWordType enname = allowedIndex.get(fields[1]);
			index.put(name, enname);
		}
		stream.close();
		reader.close();
	}

	public String getName()
	{
		return this.name;
	}
}
