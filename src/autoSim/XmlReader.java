package autoSim;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException; 

public class XmlReader{
	private Document doc;
	
	public XmlReader(File xml) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        doc = docBuilder.parse (xml);
        doc.getDocumentElement().normalize ();
	}
	
	public NodeList getNodeList(String tag) {
		return doc.getElementsByTagName(tag);
	}

}

