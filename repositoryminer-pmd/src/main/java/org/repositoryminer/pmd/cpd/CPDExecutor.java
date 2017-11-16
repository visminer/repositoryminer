package org.repositoryminer.pmd.cpd;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.repositoryminer.pmd.cpd.model.Language;
import org.repositoryminer.pmd.cpd.model.Match;
import org.repositoryminer.pmd.cpd.model.Occurrence;
import org.repositoryminer.util.StringUtils;

import net.sourceforge.pmd.cpd.CPD;
import net.sourceforge.pmd.cpd.CPDConfiguration;
import net.sourceforge.pmd.cpd.JavaLanguage;
import net.sourceforge.pmd.cpd.Mark;

public class CPDExecutor{

	private int minTokens;
	private String charset;
	private String repositoryFolder;
	private Set<Language> languages;
	
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

	public List<Match> execute() throws IOException {
		List<Match> matches = new ArrayList<Match>();

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
			
			Iterator<net.sourceforge.pmd.cpd.Match> ms = cpd.getMatches();
			while (ms.hasNext()) {
				net.sourceforge.pmd.cpd.Match m = ms.next();
				
				List<Occurrence> occurrences = new ArrayList<Occurrence>();
				for (Mark mark : m.getMarkSet()) {
					// removes the repository path from file path
					String filePath = FilenameUtils.normalize(mark.getFilename());
					filePath = filePath.substring(repositoryFolder.length()+1);

					Occurrence occurrence = new Occurrence();
					occurrence.setFilename(filePath);
					occurrence.setFilehash(StringUtils.encodeToCRC32(filePath));
					occurrence.setBeginLine(mark.getBeginLine());
					occurrence.setLineCount(mark.getLineCount());
					occurrence.setSourceCodeSlice(mark.getSourceCodeSlice());
					occurrence.setDuplicationPercentage(getDuplicatedPercentage(mark.getFilename(),
							occurrence.getSourceCodeSlice().length()));
					occurrences.add(occurrence);
				}
				
				matches.add(new Match(m.getTokenCount(), lang, occurrences));
			}
		}

		return matches;
	}

	private net.sourceforge.pmd.cpd.Language languageFactory(Language lang) {
		switch (lang) {
		case JAVA:
			return new JavaLanguage();
		default:
			return new JavaLanguage();
		}
	}

	private double getDuplicatedPercentage(String filename, int duplicatedSliceLength) {
		try {
			String source = new String(Files.readAllBytes(Paths.get(filename)));
			double value = (duplicatedSliceLength * 1.0) / source.length();
			return new BigDecimal(value * 100).setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
		} catch (IOException e) {
			return 0.0f;
		}
	}

}