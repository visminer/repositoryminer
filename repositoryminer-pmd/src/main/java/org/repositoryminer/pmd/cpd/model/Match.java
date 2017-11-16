package org.repositoryminer.pmd.cpd.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Match {

	private int tokenCount;
	private Language language;
	private List<Occurrence> occurrence;

	public Match(int tokenCount, Language language, List<Occurrence> occurrence) {
		this.tokenCount = tokenCount;
		this.language = language;
		this.occurrence = occurrence;
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("token_count", tokenCount).
			append("language", language.name()).
			append("occurrences",Occurrence.toDocumentList(occurrence));

		return doc;
	}

	public static List<Document> toDocumentList(List<Match> occurrences) {
		if (occurrences == null) {
			return new ArrayList<Document>();
		}

		List<Document> docs = new ArrayList<Document>();
		for (Match o : occurrences) {
			docs.add(o.toDocument());
		}

		return docs;
	}

	public int getTokenCount() {
		return tokenCount;
	}

	public void setTokenCount(int tokenCount) {
		this.tokenCount = tokenCount;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public List<Occurrence> getOccurrence() {
		return occurrence;
	}

	public void setOccurrence(List<Occurrence> occurrence) {
		this.occurrence = occurrence;
	}

}