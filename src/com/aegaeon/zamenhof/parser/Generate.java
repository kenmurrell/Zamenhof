package com.aegaeon.zamenhof.parser;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Generate {

    private static final Logger logger = Logger.getLogger(WiktionaryDumpParser.class.getName());

    public static void main(String[] args) {
        String lang = args[0];
        String dumpFile = args[1];
        PageObjectCollector collector = new PageObjectCollector();
        WiktionaryDumpParser dumpParser = new WiktionaryDumpParser(WiktionaryPageParser.create(lang,collector));
        dumpParser.parse(new File(dumpFile));
        logger.log(Level.SEVERE, String.valueOf(collector.getCollection().size()));
    }
}
