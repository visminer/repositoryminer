package org.repositoryminer.codesmell.project;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.clazz.LOC;
import org.repositoryminer.parser.Parser;

import net.sourceforge.pmd.cpd.CPD;
import net.sourceforge.pmd.cpd.CPDConfiguration;
import net.sourceforge.pmd.cpd.JavaLanguage;
import net.sourceforge.pmd.cpd.Language;
import net.sourceforge.pmd.cpd.Mark;
import net.sourceforge.pmd.cpd.Match;

/**
 * The detection of duplicated code plays an essential role to detect desing
 * problems. But detect clones might not be relevant if they are too small. The
 * goal of the detection is capture the portions of code that contain
 * significant amount of duplication.
 * <p>
 * The only threshold of this code smell is the number of tokens, in other
 * words, the amount of duplication that a portion of code must have to be considered
 * duplicate.
 */
public class DuplicatedCode implements IProjectCodeSmell {

	private int tokensThreshold = 100;

	public DuplicatedCode() {
	}

	public DuplicatedCode(int tokensThreshold) {
		this.tokensThreshold = tokensThreshold;
	}

	@Override
	public void detect(List<Parser> parsers, String repositoryPath, Document document) {
		document.append("name", CodeSmellId.DUPLICATED_CODE).append("occurrences", calculate(parsers, repositoryPath));
	}

	public List<Document> calculate(List<Parser> parsers, String repositoryPath) {
		List<Document> docs = new ArrayList<Document>();

		for (Parser parser : parsers) {
			CPDConfiguration config = new CPDConfiguration();
			config.setEncoding(parser.getCharset());
			config.setLanguage(languageFactory(parser.getLanguage()));
			config.setSkipLexicalErrors(true);
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

		return docs;
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

	private Double getDuplicatedPercentage(String filename, int lineCount) {
		try {
			String source = new String(Files.readAllBytes(Paths.get(filename)));
			LOC slocMetric = new LOC();

			int sloc = slocMetric.calculate(source);
			double percentage = (double) lineCount / sloc;
			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.CEILING);
			return Double.valueOf(df.format(percentage).replaceAll(",", "."));

		} catch (IOException e) {
			return 0.0;
		}
	}

}