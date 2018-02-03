package org.repositoryminer.excomment.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Pattern {

	private String name;
	private double score;
	private String clazz;
	private String theme;
	private String tdType;

	public static List<Document> toDocumentList(List<Pattern> patterns) {
		List<Document> docs = new ArrayList<Document>();
		if (patterns != null) {
			for (Pattern pattern : patterns) {
				docs.add(pattern.toDocument());
			}
		}
		return docs;
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("name", name).
			append("score", score).
			append("class", clazz).
			append("theme", theme).
			append("tdtype", tdType);
		return doc;
	}

	public Pattern() { }

	public Pattern(String name, double score, String clazz, String theme, String tdType) {
		this.name = name;
		this.score = score;
		this.clazz = clazz;
		this.theme = theme;
		this.tdType = tdType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getTdType() {
		return tdType;
	}

	public void setTdType(String tdType) {
		this.tdType = tdType;
	}

}
