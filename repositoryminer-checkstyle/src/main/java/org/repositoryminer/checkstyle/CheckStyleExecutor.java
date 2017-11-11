package org.repositoryminer.checkstyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.checkstyle.audit.RepositoryMinerAudit;
import org.repositoryminer.checkstyle.model.StyleProblem;

import com.google.common.io.Closeables;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.ModuleFactory;
import com.puppycrawl.tools.checkstyle.PackageObjectFactory;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.RootModule;

public class CheckStyleExecutor {

	private static String[] EXTENSION_FILE_FILTER = { "java" };

	private String propertiesFile;
	private String configFile;
	private String repository;

	public CheckStyleExecutor(String repository) {
		this.repository = repository;
	}

	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public Map<String, List<StyleProblem>> execute() throws CheckstyleException {
		Properties properties;
		if (propertiesFile == null) {
			properties = System.getProperties();
		} else {
			properties = loadProperties(new File(propertiesFile));
		}

		if (configFile == null) {
			configFile = "/google_checks.xml";
		}
		
		// create configurations
		Configuration config = ConfigurationLoader.loadConfiguration(configFile, new PropertiesExpander(properties));

		// create our custom audit listener
		RepositoryMinerAudit listener = new RepositoryMinerAudit();
		listener.setRepositoryPathLength(repository.length());

		ClassLoader moduleClassLoader = Checker.class.getClassLoader();
		RootModule rootModule = getRootModule(config.getName(), moduleClassLoader);

		rootModule.setModuleClassLoader(moduleClassLoader);
		rootModule.configure(config);
		rootModule.addListener(listener);

		// executes checkstyle
		rootModule.process((List<File>) FileUtils.listFiles(new File(repository), EXTENSION_FILE_FILTER, true));
		rootModule.destroy();

		return listener.getFileErrors();
	}

	private Properties loadProperties(File file) {
		Properties properties = new Properties();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);
			properties.load(fis);
		} catch (IOException e) {
			throw new RepositoryMinerException("Can not load properties from " + file.getAbsolutePath(), e);
		} finally {
			Closeables.closeQuietly(fis);
		}

		return properties;
	}

	private RootModule getRootModule(String name, ClassLoader moduleClassLoader) throws CheckstyleException {
		ModuleFactory factory = new PackageObjectFactory(Checker.class.getPackage().getName() + ".", moduleClassLoader);
		return (RootModule) factory.createModule(name);
	}

}