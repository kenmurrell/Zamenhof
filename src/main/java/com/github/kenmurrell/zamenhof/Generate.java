package com.github.kenmurrell.zamenhof;

import com.github.kenmurrell.zamenhof.model.WiktionaryDumpParser;
import com.github.kenmurrell.zamenhof.utils.IPageObjectFilter;
import com.github.kenmurrell.zamenhof.utils.LanguageFilter;
import com.github.kenmurrell.zamenhof.utils.NullFilter;
import com.github.kenmurrell.zamenhof.utils.PageObjectCollector;
import com.github.kenmurrell.zamenhof.writers.CSVWriter;
import com.github.kenmurrell.zamenhof.writers.IWriter;
import com.github.kenmurrell.zamenhof.writers.JSONWriter;
import com.github.kenmurrell.zamenhof.writers.XMLWriter;
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
		IPageObjectFilter filter = getFilter(cmd);

		PageObjectCollector collector = new PageObjectCollector(filter);
		WiktionaryDumpParser dumpParser = new WiktionaryDumpParser(collector);
		dumpParser.parse(new File(dumpFile));

		writers.forEach(w -> w.write(collector.getCollection(), outfile));
		logger.log(Level.INFO, "===COMPLETE===");
	}

	private static IPageObjectFilter getFilter(CommandLine cmd)
	{
		if(cmd.hasOption('l'))
		{
			return new LanguageFilter(cmd.getOptionValue('l'));
		}
		return new NullFilter();
	}

}
