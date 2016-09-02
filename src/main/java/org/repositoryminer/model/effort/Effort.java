package org.repositoryminer.model.effort;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Effort implements Comparable<Effort> {

	private String file;
	private int commits = 0;
	private int modifications = 0;
	private List<String> codeSmells = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	public static List<Effort> parseDocuments(List<Document> effortDocs) {
		List<Effort> efforts = new ArrayList<Effort>();
		for (Document doc : effortDocs) {
			Effort effort = new Effort();
			effort.setFile(doc.getString("file"));
			effort.setCommits(doc.getInteger("noc", 0));
			effort.setModifications(doc.getInteger("nom", 0));
			effort.setCodeSmells((List<String>) doc.get("codesmells"));

			efforts.add(effort);
		}
		return efforts;
	}

	public Document toDocument() {
		Document doc = new Document();

		return doc.append("file", file).append("noc", commits).append("nom", modifications).append("codesmells",
				codeSmells);
	}

	public static List<Document> toDocumentList(List<Effort> efforts) {
		List<Document> list = new ArrayList<Document>();
		for (Effort effort : efforts) {
			list.add(effort.toDocument());
		}

		return list;
	}

	public Effort() {
	}

	public Effort(String element, int commits, int modifications, List<String> codeSmells) {
		this.file = element;
		this.commits = commits;
		this.modifications = modifications;
		this.codeSmells = codeSmells;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getCommits() {
		return commits;
	}

	public void setCommits(int commits) {
		this.commits = commits;
	}

	public int getModifications() {
		return modifications;
	}

	public void setModifications(int modifications) {
		this.modifications = modifications;
	}

	public List<String> getCodeSmells() {
		return codeSmells;
	}

	public void setCodeSmells(List<String> codeSmells) {
		this.codeSmells = codeSmells;
	}

	public double calculateOverallEffort() {
		return modifications / commits;
	}

	public Effort incCommits() {
		commits++;

		return this;
	}

	public Effort incModifications(int modifications) {
		this.modifications += modifications;

		return this;
	}

	@Override
	public int compareTo(Effort otherEffort) {
		int effort = (int) calculateOverallEffort();
		int comparedEffort = (int) otherEffort.calculateOverallEffort();

		return effort - comparedEffort;
	}

}
