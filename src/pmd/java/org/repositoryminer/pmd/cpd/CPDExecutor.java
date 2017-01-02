package org.repositoryminer.pmd.cpd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.repositoryminer.codemetric.direct.LOC;
import org.repositoryminer.pmd.cpd.model.FileInfo;
import org.repositoryminer.pmd.cpd.model.Occurrence;

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
 * words, the amount of duplication that a portion of code must have to be
 * considered duplicate.
 */
public class CPDExecutor{

	private int minTokens;
	private String charset;
	private String repositoryFolder;
	private List<org.repositoryminer.ast.Language> languages;
	
	private LOC locMetric = new LOC();

	public CPDExecutor(String repositoryFolder) {
		this.repositoryFolder = repositoryFolder;
	}
	
	public void setMinTokens(int minTokens) {
		this.minTokens = minTokens;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setLanguages(List<org.repositoryminer.ast.Language> languages) {
		this.languages = languages;
	}

	public List<Occurrence> execute() throws IOException {
		List<Occurrence> occurrences = new ArrayList<Occurrence>();

		for (org.repositoryminer.ast.Language lang : languages) {
			CPDConfiguration config = new CPDConfiguration();
			config.setEncoding(charset);
			config.setLanguage(languageFactory(lang));
			config.setSkipLexicalErrors(true);
			config.setSourceEncoding(charset);
			config.setNonRecursive(false);
			config.setMinimumTileSize(minTokens);

			CPDConfiguration.setSystemProperties(config);
			CPD cpd = new CPD(config);
			cpd.addRecursively(new File(repositoryFolder));

			cpd.go();
			
			Iterator<Match> ms = cpd.getMatches();
			while (ms.hasNext()) {
				Match m = ms.next();
				
				Occurrence occurrence = new Occurrence();
				occurrence.setLineCount(m.getLineCount());
				occurrence.setTokenCount(m.getTokenCount());
				occurrence.setSourceCodeSlice(m.getSourceCodeSlice());
				occurrence.setLanguage(lang);

				List<FileInfo> filesInfo = new ArrayList<FileInfo>();
				for (Mark mark : m.getMarkSet()) {
					// removes the repository path from file path
					String filePath = FilenameUtils.normalize(mark.getFilename());
					filePath = filePath.substring(repositoryFolder.length()+1);

					FileInfo fileInfo = new FileInfo();
					fileInfo.setBeginLine(mark.getBeginLine());
					fileInfo.setEndLine(mark.getEndLine());
					fileInfo.setFilename(filePath);
					fileInfo.setDuplicationPercentage(getDuplicatedPercentage(mark.getFilename(), m.getLineCount()));
					filesInfo.add(fileInfo);
				}
				
				occurrence.setFilesInfo(filesInfo);
				occurrences.add(occurrence);
			}
		}

		return occurrences;
	}

	private Language languageFactory(org.repositoryminer.ast.Language lang) {
		switch (lang) {
		case JAVA:
			return new JavaLanguage();
		default:
			return null;
		}
	}

	private float getDuplicatedPercentage(String filename, int lineCount) {
		try {
			String source = new String(Files.readAllBytes(Paths.get(filename)));
			return (lineCount * 1.0f) / locMetric.calculate(source);
		} catch (IOException e) {
			return 0.0f;
		}
	}

}