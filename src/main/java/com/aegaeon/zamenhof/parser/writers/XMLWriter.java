package com.aegaeon.zamenhof.parser.writers;

import com.aegaeon.zamenhof.parser.model.PageObject;
import com.aegaeon.zamenhof.parser.model.WiktionaryTranslation;
import com.aegaeon.zamenhof.parser.utils.FlatMapXmlWriter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLWriter implements IWriter
{
	private static final Logger logger = Logger.getLogger(XMLWriter.class.getName());

	@Override
	public void write(Collection<PageObject> objects, String basename)
	{
		try (FlatMapXmlWriter writer = new FlatMapXmlWriter(basename + ".xml")) {
			writer.createDocument("UTF-8", "1.0");
			Map<String, String> lang = new HashMap<>();
			lang.put("lang", "EN");
			writer.createRoot("translations", lang);
			int ctr = 0;
			for (PageObject object : objects) {
				if (object instanceof WiktionaryTranslation) {
					Map<String, String> id = new HashMap<>();
					id.put("id", String.valueOf(ctr));
					boolean aight = writer.addEntry("translation", id, object.attributes());
					ctr = ctr + (aight ? 1 : 0);
				}
			}
			writer.endRoot();
			writer.endDocument();
			logger.log(Level.INFO, "Wrote " + ctr + " objects");
		}
		catch (IOException e) {

			logger.log(Level.SEVERE, "Writing error " + e.getMessage());
		}
	}


}
