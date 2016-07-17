package org.repositoryminer.codesmell.tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.repositoryminer.listener.IDuplicatedCodeDetectionListener;
import org.repositoryminer.listener.ITagCodeSmellDetectionListener;
import org.repositoryminer.parser.Parser;

import net.sourceforge.pmd.cpd.CPD;
import net.sourceforge.pmd.cpd.CPDConfiguration;
import net.sourceforge.pmd.cpd.JavaLanguage;
import net.sourceforge.pmd.cpd.Language;
import net.sourceforge.pmd.cpd.Match;

public class DuplicatedCode implements ITagCodeSmell {

	private int tokensThreshold = 100;
	private boolean skipLexicalErrors = true;
	
	private IDuplicatedCodeDetectionListener listener;

	public DuplicatedCode() {}
	
	public DuplicatedCode(int tokensThreshold, boolean skipLexicalErrors) {
		this.tokensThreshold = tokensThreshold;
		this.skipLexicalErrors = skipLexicalErrors;
	}
	
	@Override
	public void detect(List<Parser> parsers, String repositoryPath, ITagCodeSmellDetectionListener listener) {
		this.listener = (IDuplicatedCodeDetectionListener)listener;
		calculate(parsers, repositoryPath);
	}

	public void calculate(List<Parser> parsers, String repositoryPath) {
		listener.initDuplicationDetection();

		for (Parser parser : parsers) {
			CPDConfiguration config = new CPDConfiguration();
			config.setEncoding(parser.getCharset());
			config.setLanguage(languageFactory(parser.getLanguage()));
			config.setSkipLexicalErrors(skipLexicalErrors);
			config.setSourceEncoding(parser.getCharset());
			config.setMinimumTileSize(tokensThreshold);
			config.setNonRecursive(true);

			ArrayList<File> files = new ArrayList<File>();
			for (String path : parser.getSourceFolders()) {
				files.add(new File(path));
			}
			config.setFiles(files);

			CPDConfiguration.setSystemProperties(config);
			CPD cpd = new CPD(config);

			if (null != config.getFiles() && !config.getFiles().isEmpty()) {
				addSourcesFilesToCPD(config.getFiles(), cpd);
			}
			
			cpd.go();
			Iterator<Match> ms = cpd.getMatches();
			while (ms.hasNext()) {
				Match m = ms.next();
				
				listener.updateDuplicationDetection(m, parser);
			}
		}
		
		listener.endOfDuplicationDetection();
	}

	private static void addSourcesFilesToCPD(List<File> files, CPD cpd) {
		try {
			for (File file : files) {
				cpd.addAllInDirectory(file);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private Language languageFactory(String lang) {
		switch (lang) {
		case "java":
			return new JavaLanguage();
		default:
			return null;
		}
	}

	
}