package com.aegaeon.zamenhof.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class CSVWriter {

    public static void write(HashSet<PageObject> objects)
    {
        File file = new File("test.csv");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
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
