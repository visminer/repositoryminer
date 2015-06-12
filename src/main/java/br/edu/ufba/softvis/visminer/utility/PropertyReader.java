package br.edu.ufba.softvis.visminer.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

	private Properties properties;
	
	public PropertyReader(String propertiesPath){
		
		try {
			this.properties = new Properties();
			InputStream inputStream = new FileInputStream(new File(propertiesPath));
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getProperty(String property){
		return properties.getProperty(property);
	}
	
}
