package com.aegaeon.zamenhof.parser;

import com.aegaeon.zamenhof.parser.model.WiktionaryDumpParser;
import com.aegaeon.zamenhof.parser.utils.PageObjectCollector;
import com.aegaeon.zamenhof.parser.writers.CSVWriter;
import com.aegaeon.zamenhof.parser.writers.IWriter;
import com.aegaeon.zamenhof.parser.writers.JSONWriter;
import com.aegaeon.zamenhof.parser.writers.XMLWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Generate {

    private static final Logger logger = Logger.getLogger(WiktionaryDumpParser.class.getName());

    private static final List<IWriter> writers = new ArrayList<IWriter>(){{
        add(new CSVWriter());
        add(new XMLWriter());
        add(new JSONWriter());
    }};

    public static void main(String[] args) {
        String dumpFile = args[0];
        String tempdir = args[1];
        String tempfile = tempdir+"/test";

        PageObjectCollector collector = new PageObjectCollector();
        WiktionaryDumpParser dumpParser = new WiktionaryDumpParser(collector);
        dumpParser.parse(new File(dumpFile));

        writers.forEach(w->w.write(collector.getCollection(),tempfile));
        logger.log(Level.INFO,"===COMPLETE===");
    }
}
