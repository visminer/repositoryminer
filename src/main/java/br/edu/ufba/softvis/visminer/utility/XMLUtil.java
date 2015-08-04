package br.edu.ufba.softvis.visminer.utility;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import br.edu.ufba.softvis.visminer.config.MetricConfig;

public class XMLUtil {

	public static Document getXMLDoc(String path){
		
		try {
			
			InputStream inputStream = MetricConfig.class.getResourceAsStream(path);
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
