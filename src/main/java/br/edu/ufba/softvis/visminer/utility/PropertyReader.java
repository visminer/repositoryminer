package br.edu.ufba.softvis.visminer.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @version 0.9
 * Utility class to retrieve data from a properties file.
 */
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
	
	/**
	 * @param property
	 * @return The value of the property
	 */
	public String getProperty(String property){
		return properties.getProperty(property);
	}
	
}
