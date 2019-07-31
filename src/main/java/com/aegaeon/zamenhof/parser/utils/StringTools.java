package com.aegaeon.zamenhof.parser.utils;

import java.util.Arrays;
import java.util.List;

public class StringTools
{
	private static final List<String> ALLOWED_ENCODINGS = Arrays.asList("UTF-8");

	public static String remove(String text, final char item)
	{
		final char[] chars = text.toCharArray();
		int p = 0;
		for(int i=0;i<chars.length;i++)
		{
			if(chars[i]!=item)
			{
				chars[p++] = chars[i];
			}
		}
		return new String(chars,0,p);
	}

	public static String replace(String text, final char item, final char replacement)
	{
		final char[] chars = text.toCharArray();
		for (int i=0;i<chars.length;i++)
		{
			if(chars[i]==item)
			{
				chars[i]=replacement;
			}
		}
		return new String(chars);
	}

	public static boolean isNumeric(String text)
	{
		try
		{
			Double.parseDouble(text);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	public static boolean isEncoding(String text)
	{
		return ALLOWED_ENCODINGS.contains(text);
	}

}
