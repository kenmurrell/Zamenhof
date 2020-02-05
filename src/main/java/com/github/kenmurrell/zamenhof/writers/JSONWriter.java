package com.github.kenmurrell.zamenhof.writers;

import com.github.kenmurrell.zamenhof.model.PageObject;
import com.github.kenmurrell.zamenhof.model.WiktionaryTranslation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONWriter implements IWriter
{

	private static final Logger logger = Logger.getLogger(CSVWriter.class.getName());

	public void write(Collection<PageObject> objects, String basename)
	{
		JSONArray jsonRoot = new JSONArray();
		try {
			if (objects.size() > 1200000) {
				throw new OutOfMemoryError();
			}
			objects.forEach(o -> add(o, jsonRoot));
			FileWriter file = new FileWriter(basename + ".json");
			file.write(jsonRoot.toJSONString());
			file.flush();
			file.close();
			logger.log(Level.INFO, "Wrote " + jsonRoot.size() + " objects");
		}
		catch (OutOfMemoryError e) {
			logger.log(Level.INFO, "Skipping JSON writing, object dump too large");
		}
		catch (IOException e) {
			logger.log(Level.SEVERE, "Writing error " + e.getMessage());
		}
	}

	private void add(PageObject object, JSONArray jsonRoot)
	{
		if (object instanceof WiktionaryTranslation) {
			Map<String, String> attr = object.attributes();
			JSONObject translation = new JSONObject();
			for (Map.Entry<String, String> entry : attr.entrySet()) {
				translation.put(entry.getKey(), entry.getValue());
			}
			jsonRoot.add(translation);
		}
	}


}
