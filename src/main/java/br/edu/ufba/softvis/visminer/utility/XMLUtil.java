package br.edu.ufba.softvis.visminer.utility;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class XMLUtil {

	public static Document getXMLDoc(String path){
		
		try {
			// TODO rever
			// InputStream inputStream = MetricConfig.class.getResourceAsStream(path);
			InputStream inputStream = null;
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(inputStream);
			doc.getDocumentElement().normalize();
			
			return doc;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		
	}
	
}
