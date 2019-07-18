package com.aegaeon.zamenhof.parser;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Generate {

    private static final Logger logger = Logger.getLogger(WiktionaryDumpParser.class.getName());

    public static void main(String[] args) {
        String dumpFile = args[0];
        String tempdir = args[1];
        PageObjectCollector collector = new PageObjectCollector();
        WiktionaryDumpParser dumpParser = new WiktionaryDumpParser(WiktionaryPageParser.create(collector));
        dumpParser.parse(new File(dumpFile));

        logger.log(Level.INFO, "Adding "+ collector.getCollection().size()+" translations to output");
        CSVWriter.write(collector.getCollection(),new File(tempdir+"/test.csv"));
        logger.log(Level.INFO,"===COMPLETE===");
    }
}
