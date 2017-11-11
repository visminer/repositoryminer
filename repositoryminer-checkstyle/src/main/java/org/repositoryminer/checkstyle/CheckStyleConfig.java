package org.repositoryminer.checkstyle;

public class CheckStyleConfig {

	private String configFile;
	private String propertiesFile;
	private boolean insideRepository = true;

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getPropertiesFile() {
		return propertiesFile;
	}

	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}

	public boolean isInsideRepository() {
		return insideRepository;
	}

	public void setInsideRepository(boolean insideRepository) {
		this.insideRepository = insideRepository;
	}

}