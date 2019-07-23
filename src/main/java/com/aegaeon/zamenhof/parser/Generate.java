package com.aegaeon.zamenhof.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Generate {

    private static final Logger logger = Logger.getLogger(WiktionaryDumpParser.class.getName());

    private static List<IWriter> writers = new ArrayList<IWriter>(){{
        add(new CSVWriter());
        add(new XMLWriter());
    }};

    public static void main(String[] args) {
        String dumpFile = args[0];
        String tempdir = args[1];
        String tempfile = tempdir+"/test";

        PageObjectCollector collector = new PageObjectCollector();
        WiktionaryDumpParser dumpParser = new WiktionaryDumpParser(WiktionaryPageParser.create(collector));
        dumpParser.parse(new File(dumpFile));

        writers.forEach(w->w.write(collector.getCollection(),tempfile));
        logger.log(Level.INFO,"===COMPLETE===");
    }
}
