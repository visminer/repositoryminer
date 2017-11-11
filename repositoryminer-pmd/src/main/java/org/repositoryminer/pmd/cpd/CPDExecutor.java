package org.repositoryminer.pmd.cpd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.repositoryminer.pmd.cpd.model.FileInfo;
import org.repositoryminer.pmd.cpd.model.Language;
import org.repositoryminer.pmd.cpd.model.Occurrence;
import org.repositoryminer.util.StringUtils;

import net.sourceforge.pmd.cpd.CPD;
import net.sourceforge.pmd.cpd.CPDConfiguration;
import net.sourceforge.pmd.cpd.JavaLanguage;
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
	private Set<Language> languages;
	
	private Pattern pattern = Pattern.compile("(\r\n)|(\n)|(\r)");
	
	public CPDExecutor(String repositoryFolder) {
		this.repositoryFolder = repositoryFolder;
	}
	
	public void setMinTokens(int minTokens) {
		this.minTokens = minTokens;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setLanguages(Set<Language> languages) {
		this.languages = languages;
	}

	public List<Occurrence> execute() throws IOException {
		List<Occurrence> occurrences = new ArrayList<Occurrence>();

		for (Language lang : languages) {
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
				occurrence.setSourceCodeSlice(m.getSourceCodeSlice().trim());
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
					fileInfo.setFilehash(StringUtils.encodeToCRC32(filePath));
					fileInfo.setDuplicationPercentage(getDuplicatedPercentage(mark.getFilename(), m.getLineCount()));
					filesInfo.add(fileInfo);
				}
				
				occurrence.setFilesInfo(filesInfo);
				occurrences.add(occurrence);
			}
		}

		return occurrences;
	}

	private net.sourceforge.pmd.cpd.Language languageFactory(Language lang) {
		switch (lang) {
		case JAVA:
			return new JavaLanguage();
		default:
			return new JavaLanguage();
		}
	}

	private float getDuplicatedPercentage(String filename, int lineCount) {
		try {
			String source = new String(Files.readAllBytes(Paths.get(filename)));
			return (lineCount * 1.0f) / calculateLOC(source);
		} catch (IOException e) {
			return 0.0f;
		}
	}

	private int calculateLOC(String source) {
		if (source == null || source.length() == 0) {
			return 0;
		}

		int lines = 1;
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			lines++;
		}

		return lines;
	}
	
}