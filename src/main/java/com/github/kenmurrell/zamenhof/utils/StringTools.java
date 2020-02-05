package com.github.kenmurrell.zamenhof.utils;

import java.util.HashSet;
import java.util.Set;

public class StringTools
{
	private static final Set<String> ALLOWED_ENCODINGS = new HashSet<String>()
	{{
		add("UTF-8");
	}};

	private static final Set<Character> BAD_PUNCTUATION = new HashSet<Character>()
	{{
		add('[');
		add(']');
		add(')');
		add('(');
	}};


	//Now that I have Apache Commons StringUtil, i should stop using this...
	public static String remove(String text, final char item)
	{
		final char[] chars = text.toCharArray();
		int p = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != item) {
				chars[p++] = chars[i];
			}
		}
		return new String(chars, 0, p);
	}

	public static String removePunc(String text)
	{
		final char[] chars = text.toCharArray();
		int p = 0;
		for (int i = 0; i < chars.length; i++) {
			if (!BAD_PUNCTUATION.contains(chars[i])) {
				chars[p++] = chars[i];
			}
		}
		return new String(chars, 0, p);
	}

	public static String replace(String text, final char item, final char replacement)
	{
		final char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == item) {
				chars[i] = replacement;
			}
		}
		return new String(chars);
	}

	public static boolean isNumeric(String text)
	{
		try {
			Double.parseDouble(text);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isEncoding(String text)
	{
		return ALLOWED_ENCODINGS.contains(text);
	}

}
