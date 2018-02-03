package org.repositoryminer.excomment.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Heuristic {

	private String description;
	private int status;
	private double score;
	
	public static List<Document> toDocumentList(List<Heuristic> heuristics) {
		List<Document> docs = new ArrayList<Document>();
		if (heuristics != null) {
			for (Heuristic heuristic : heuristics) {
				docs.add(heuristic.toDocument());
			}
		}
		return docs;
	}
	
	public Document toDocument() {
		Document doc = new Document();
		doc.append("description", description).
			append("status", status).
			append("score", score);
		return doc;
	}
	
	public Heuristic() { }
	
	public Heuristic(String description, int status, double score) {
		this.description = description;
		this.status = status;
		this.score = score;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
}
