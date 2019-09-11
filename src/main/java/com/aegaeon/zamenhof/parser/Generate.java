package com.aegaeon.zamenhof.parser;

import com.aegaeon.zamenhof.parser.model.WiktionaryDumpParser;
import com.aegaeon.zamenhof.parser.utils.IPageObjectFilter;
import com.aegaeon.zamenhof.parser.utils.NullFilter;
import com.aegaeon.zamenhof.parser.utils.PageObjectCollector;
import com.aegaeon.zamenhof.parser.utils.TranslationFilter;
import com.aegaeon.zamenhof.parser.writers.CSVWriter;
import com.aegaeon.zamenhof.parser.writers.IWriter;
import com.aegaeon.zamenhof.parser.writers.JSONWriter;
import com.aegaeon.zamenhof.parser.writers.XMLWriter;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Generate
{

	private static final Logger logger = Logger.getLogger(WiktionaryDumpParser.class.getName());

	private static final List<IWriter> writers = new ArrayList<IWriter>()
	{{
		add(new CSVWriter());
		add(new XMLWriter());
		add(new JSONWriter());
	}};

	public static void main(String[] args)
	{
		Options options = new Options();
		Option dir = Option.builder("p").longOpt("path").hasArg().required().desc("output directory path").build();
		options.addOption(dir);
		Option langpair = Option.builder("l").longOpt("language pair").required(false).hasArg().desc("language pair (separated by \"-\")").build();
		options.addOption(langpair);
		Option dumps = Option.builder("w").longOpt("wiktionary dumps").required().hasArg().desc("list of wiktionary dumps to parse").build();
		options.addOption(dumps);
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		}
		catch (ParseException e) {
			logger.log(Level.SEVERE, e.getMessage());
			new HelpFormatter().printHelp("Generate", options);
			System.exit(1);
		}
		String outfile = cmd.getOptionValue('p') + "/dictionary";
		String dumpFile = cmd.getOptionValue('w');
		IPageObjectFilter filter = cmd.hasOption('l') ? new TranslationFilter(cmd.getOptionValue('l')) : new NullFilter();

		PageObjectCollector collector = new PageObjectCollector(filter);
		WiktionaryDumpParser dumpParser = new WiktionaryDumpParser(collector);
		dumpParser.parse(new File(dumpFile));

		writers.forEach(w -> w.write(collector.getCollection(), outfile));
		logger.log(Level.INFO, "===COMPLETE===");
	}

}
