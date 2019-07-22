package com.aegaeon.zamenhof.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class CSVWriter {

    public static void write(Collection<PageObject> objects, File outputfile)
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputfile));
            for(PageObject object : objects)
            {
                if(object instanceof WiktionaryTranslation) {
                    writer.write(object.toString());
                    writer.newLine();
                }
            }
            writer.flush();
            writer.close();
        }
        catch (IOException e) {

            e.printStackTrace();
        }
    }


}
