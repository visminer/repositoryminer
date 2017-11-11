package org.repositoryminer.pmd.cpd;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.repositoryminer.pmd.cpd.model.Language;

public class CPDConfig {

	private int tokensThreshold = 100;
	private Set<Language> languages = new HashSet<>(Arrays.asList(Language.JAVA));

	public CPDConfig() {}
	
	public CPDConfig(int tokensThreshold, Set<Language> languages) {
		this.tokensThreshold = tokensThreshold;
		this.languages = languages;
	}

	public int getTokensThreshold() {
		return tokensThreshold;
	}

	public void setTokensThreshold(int tokensThreshold) {
		this.tokensThreshold = tokensThreshold;
	}

	public Set<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Set<Language> languages) {
		this.languages = languages;
	}

}