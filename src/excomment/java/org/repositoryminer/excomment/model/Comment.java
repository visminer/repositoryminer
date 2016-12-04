package org.repositoryminer.excomment.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;

public class Comment {

	private int id;
	private float totalPattern;
	private float totalHeuristic;
	private float totalScore;
	private String comment;
	private String path;
	private String clazz;
	private String method;
	private List<Pattern> patterns;
	private List<Heuristic> heuristics;
	
	public static List<Document> toDocumentList(Collection<Comment> comments) {
		List<Document> docs = new ArrayList<Document>();
		
		if (comments != null) {
			for (Comment comment : comments) {
				docs.add(comment.toDocument());
			}
		}
		
		return docs;
	}
	
	public Document toDocument() {
		Document doc = new Document();
		doc.append("id", id).append("total_pattern", totalPattern).append("total_heuristic", totalHeuristic).
		append("total_score", totalScore).append("comment", comment).append("path", path).append("class", clazz).
		append("method", method).append("patterns", Pattern.toDocumentList(patterns)).append("heuristics", Heuristic.toDocumentList(heuristics));
		
		return doc;
	}
	
	public Comment() {
		patterns = new ArrayList<Pattern>();
		heuristics = new ArrayList<Heuristic>();
	}
	
	public Comment(int id, float totalPattern, float totalHeuristic, float totalScore, String comment, String path,
			String clazz, String method) {
		this();
		this.id = id;
		this.totalPattern = totalPattern;
		this.totalHeuristic = totalHeuristic;
		this.totalScore = totalScore;
		this.comment = comment;
		this.path = path;
		this.clazz = clazz;
		this.method = method;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getTotalPattern() {
		return totalPattern;
	}

	public void setTotalPattern(float totalPattern) {
		this.totalPattern = totalPattern;
	}

	public float getTotalHeuristic() {
		return totalHeuristic;
	}

	public void setTotalHeuristic(float totalHeuristic) {
		this.totalHeuristic = totalHeuristic;
	}

	public float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<Pattern> getPatterns() {
		return patterns;
	}

	public void setPatterns(List<Pattern> patterns) {
		this.patterns = patterns;
	}

	public List<Heuristic> getHeuristics() {
		return heuristics;
	}

	public void setHeuristics(List<Heuristic> heuristics) {
		this.heuristics = heuristics;
	}
	
}