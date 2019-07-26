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

    private static Map<String,ILanguage> codelanguageIndex;

    private static Map<String,ILanguage> nameLanguageIndex;

    private static boolean initialized;

    private String code;

    private String name;

    public static final ILanguage ENGLISH = get("en");

    public static final ILanguage CZECH = get("cs");

    private static ILanguage get(final String code) {
        initialize();
        return code == null ? null : codelanguageIndex.get(code);
    }

    public static ILanguage getByCode(String code)
    {
        initialize();
        return code == null ? null : codelanguageIndex.get(code);
    }

    public static ILanguage getByName(String name)
    {
        initialize();
        return name ==null ? null : nameLanguageIndex.get(name);
    }

    public static void initialize()
    {
        if(initialized)
        {
            return;
        }
        codelanguageIndex = new TreeMap<>();
        nameLanguageIndex = new TreeMap<>();
        try{
            InputStreamReader inreader = new InputStreamReader(Language.class.getResourceAsStream("/language_codes.txt"),StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inreader);
            String line;
            while((line = reader.readLine()) !=null)
            {
                String[] fields = line.split("\\s\\s");
                String code = fields[0];
                String name = fields[1];
                ILanguage language = new Language(code, name);
                codelanguageIndex.put(code,language);
                nameLanguageIndex.put(name.toUpperCase(),language);
            }
            initialized = true;
        }
        catch (IOException ex)
        {
            logger.log(Level.SEVERE, "babel shit is happening");
        }
    }

    private Language(String code, String name)
    {
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
        }
        else
        {
            return code.equals(((ILanguage)other).getCode());
        }
    }

    @Override
    public int compareTo(ILanguage other) {
        return this.equals(other) ? 0 : this.code.compareTo(other.getCode());
    }

}

