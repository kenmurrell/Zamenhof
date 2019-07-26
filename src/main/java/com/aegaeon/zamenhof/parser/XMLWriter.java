package com.aegaeon.zamenhof.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLWriter implements IWriter
{
	private static Logger logger = Logger.getLogger(XMLWriter.class.getName());

	@Override
	public void write(Collection<PageObject> objects, String basename)
	{
		File filename = new File(basename+".xml");
		try {
			if(objects.size()>1200000)
				throw new OutOfMemoryError();
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = doc.createElement("translations");
			doc.appendChild(root);
			objects.forEach(o->add(o,root,doc));
			Transformer tr = TransformerFactory.newInstance().newTransformer();
			tr.setOutputProperty(OutputKeys.INDENT,"yes");
			tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","3");
			tr.transform(new DOMSource(doc),new StreamResult(filename));
			logger.log(Level.INFO,"Wrote "+doc.getChildNodes().item(0).getChildNodes().getLength()+" objects");
		}
		catch (ParserConfigurationException | TransformerException e)
		{
			logger.log(Level.SEVERE,"Writing error "+e.getMessage());
		}
		catch (OutOfMemoryError e)
		{
			logger.log(Level.INFO,"Skipping XML writing, object dump too large");
		}
	}

	private void add(PageObject object, Element root, Document doc)
	{
		Map<String,String> attr = object.attributes();
		if(object instanceof WiktionaryTranslation)
		{
			Element t = doc.createElement("translation");
			root.appendChild(t);
			Element sourcelanguage = doc.createElement("sourcelanguage");
			sourcelanguage.appendChild(doc.createTextNode(attr.get("sourcelanguage")));
			t.appendChild(sourcelanguage);
			Element sourceword = doc.createElement("sourceword");
			sourceword.appendChild(doc.createTextNode(attr.get("sourceword")));
			t.appendChild(sourceword);
			Element targetlanguage = doc.createElement("targetlanguage");
			targetlanguage.appendChild(doc.createTextNode(attr.get("targetlanguage")));
			t.appendChild(targetlanguage);
			Element targetword = doc.createElement("targetword");
			targetword.appendChild(doc.createTextNode(attr.get("targetword")));
			t.appendChild(targetword);
			Element wordtype = doc.createElement("wordtype");
			wordtype.appendChild(doc.createTextNode(attr.get("wordtype")));
			t.appendChild(wordtype);
			Element pageid = doc.createElement("pageid");
			pageid.appendChild(doc.createTextNode(attr.get("pageid")));
			t.appendChild(pageid);
			Element sense = doc.createElement("sense");
			sense.appendChild(doc.createTextNode(attr.get("sense")));
			t.appendChild(sense);
		}
	}


}
