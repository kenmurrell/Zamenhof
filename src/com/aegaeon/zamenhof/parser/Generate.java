package com.aegaeon.zamenhof.parser;

import java.io.File;

public class Generate {

    public static void main(String[] args) {
        String lang = args[0];
        String dumpFile = args[1];
        WiktionaryDumpParser dumpParser = new WiktionaryDumpParser(WiktionaryPageParser.create(lang));
        dumpParser.parse(new File(dumpFile));
    }
}
