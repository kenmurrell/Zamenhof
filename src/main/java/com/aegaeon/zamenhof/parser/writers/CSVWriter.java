package com.aegaeon.zamenhof.parser.writers;

import com.aegaeon.zamenhof.parser.model.PageObject;
import com.aegaeon.zamenhof.parser.model.WiktionaryTranslation;
import com.aegaeon.zamenhof.parser.utils.StringTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVWriter implements IWriter{

    private static final Logger logger = Logger.getLogger(CSVWriter.class.getName());

    public void write(Collection<PageObject> objects, String basename)
    {
        int ctr = 0;
        File filename = new File(basename+".csv");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for(PageObject object : objects)
            {
                writer.write(convert(object));
                writer.newLine();
                ctr++;

            }
            writer.flush();
            writer.close();
            logger.log(Level.INFO,"Wrote "+ctr+" objects");
        }
        catch (IOException e) {

            logger.log(Level.SEVERE,"Writing error "+e.getMessage());
        }
    }

    private String convert(PageObject object)
    {
        StringBuilder sb = new StringBuilder();
        Map<String, String> attr = object.attributes();
        if(object instanceof WiktionaryTranslation) {
            sb.append(attr.get("sourcelanguage")).append(",");
            sb.append(attr.get("sourceword")).append(",");
            sb.append(attr.get("targetlanguage")).append(",");
            sb.append(attr.get("targetword")).append(",");
            sb.append(attr.get("wordtype")).append(",");
            sb.append(attr.get("pageid")).append(",");
            sb.append(StringTools.replace(attr.get("sense"),',',';'));
        }
        else
        {
            sb.append("-");
        }
        return sb.toString();
    }


}
