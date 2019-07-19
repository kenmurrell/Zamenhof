package com.aegaeon.zamenhof.parser.utils;

public class StringTools
{
	public static String remove(String text,final char item)
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

}
