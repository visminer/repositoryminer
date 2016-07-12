package org.repositoryminer.codesmell.snapshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.SLOCMetric;
import org.repositoryminer.parser.Parser;

import net.sourceforge.pmd.cpd.CPD;
import net.sourceforge.pmd.cpd.CPDConfiguration;
import net.sourceforge.pmd.cpd.JavaLanguage;
import net.sourceforge.pmd.cpd.Language;
import net.sourceforge.pmd.cpd.Mark;
import net.sourceforge.pmd.cpd.Match;

public class DuplicatedCode implements ISnapshotCodeSmell {

	private static final int TOKENS_THRESHOLD = 100;

	@Override
	public void detect(List<Parser> parsers, String repositoryPath, Document document) {
		document.append(CodeSmellId.DUPLICATED_CODE, calculate(parsers, repositoryPath));
	}

	public Document calculate(List<Parser> parsers, String repositoryPath) {
		List<Document> docs = new ArrayList<Document>();

		for (Parser parser : parsers) {
			CPDConfiguration config = new CPDConfiguration();
			config.setEncoding(parser.getCharset());
			config.setLanguage(languageFactory(parser.getLanguage()));
			config.setSkipLexicalErrors(true);
			config.setSourceEncoding(parser.getCharset());
			config.setMinimumTileSize(TOKENS_THRESHOLD);
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
				Document auxDoc = new Document();
				auxDoc.append("line_count", m.getLineCount());
				auxDoc.append("token_count", m.getTokenCount());
				auxDoc.append("source_code_slice", m.getSourceCodeSlice());
				auxDoc.append("language", parser.getLanguage());
				
				List<Document> filesDoc = new ArrayList<Document>();
				for (Mark mark : m.getMarkSet()) {
					Document fileDoc = new Document();
					fileDoc.append("begin_line", mark.getBeginLine());
					fileDoc.append("end_line", mark.getEndLine());
					fileDoc.append("file_name", mark.getFilename());
					fileDoc.append("percentage", getDuplicatedPercentage(mark.getFilename(), m.getLineCount()));
					filesDoc.add(fileDoc);
				}
				
				auxDoc.append("files", filesDoc);
				docs.add(auxDoc);
			}
		}
		
		return new Document("occurrences", docs);
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

	private float getDuplicatedPercentage(String filename, int lineCount) {
		try {
			String source = new String(Files.readAllBytes(Paths.get(filename)));
			SLOCMetric slocMetric = new SLOCMetric();
			
			int sloc = slocMetric.calculate(source);
			float percentage = (float) lineCount / sloc;
			return Math.round(percentage);
		} catch (IOException e) {
			return 0;
		}
	}
	
}