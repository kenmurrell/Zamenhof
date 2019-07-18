package com.aegaeon.zamenhof.parser.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Language implements ILanguage{

    private static final Logger logger = Logger.getLogger(Language.class.getName());

    private static Map<String,ILanguage> languageIndex;

    protected String code;

    protected String name;

    public static final ILanguage ENGLISH = get("en");

    private static ILanguage get(final String code) {
        initialize();
        return code == null ? null : languageIndex.get(code);
    }

    public static ILanguage getByCode(String code)
    {
        return get(code);
    }

    public static void main(String[] args)
    {
        initialize();
    }

    public static void initialize()
    {
        try{
            InputStreamReader inreader = new InputStreamReader(Language.class.getResourceAsStream("/com/aegaeon/zamenhof/resources/language_codes.txt"),StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inreader);
            String line;
            while((line = reader.readLine()) !=null)
            {
                String[] fields = line.split("\\s\\s");
                String code = fields[0];
                String name = fields[1];
                ILanguage language = new Language(code, name);
                languageIndex.put(code,language);
            }
        }
        catch (IOException ex)
        {
            logger.log(Level.SEVERE, "babel shit is happening");
        }

    }

    protected Language(String code, String name)
    {
        languageIndex = new TreeMap<>();
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public  int hashCode()
    {
        return code.hashCode();
    }

    @Override
    public boolean equals(final Object other)
    {
        if(other==null || !(other instanceof ILanguage))
        {
            return false;
        }else
        {
            return code.equals(((ILanguage)other).getCode());
        }
    }

    @Override
    public int compareTo(ILanguage other) {
        return this.equals(other) ? 0 : this.code.compareTo(other.getCode());
    }

}

