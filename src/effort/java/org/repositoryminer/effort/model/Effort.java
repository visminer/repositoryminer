package org.repositoryminer.effort.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

/**
 * <h1>Each effort node to base calculations on</h1>
 * <p>
 * Current effort measurement is file-oriented and has a calculation-base
 * composed of:
 * <ul>
 * <li>Commits -> number of commits that have affected the file
 * <li>Modifications -> the code churn related to the file
 * <li>CodeSmells -> if codesmells affect the file and the impact is a
 * influential value (> 1.0) a correction factor is applied to the overall
 * effort
 * </ul>
 * The Comparable interface is implemented because it is necessary to rank
 * effort collections according to their overall calculated effort. That is the
 * case with
 * {@link org.repositoryminer.effort.postprocessing.EffortCategoriesMiningTask#processEfforts}
 * <p>
 */
public class Effort implements Comparable<Effort> {

	private String file;
	private int commits = 0;
	private int modifications = 0;

	private double smellsImpactFactor = 1.0;
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

	public double getCodeSmellsImpactFactor() {
		return this.smellsImpactFactor;
	}

	public void setCodeSmellsImpactFactor(double smellsImpactFactor) {
		this.smellsImpactFactor = smellsImpactFactor;
	}

	public boolean hasCodeSmells() {
		return (codeSmells != null && !codeSmells.isEmpty());
	}

	public Effort incCommits() {
		commits++;

		return this;
	}

	public Effort incModifications(int modifications) {
		this.modifications += modifications;

		return this;
	}

	public double calculateOverallEffort() {
		return (modifications / commits) * (hasCodeSmells() ? smellsImpactFactor : 1.0);
	}

	@Override
	public int compareTo(Effort otherEffort) {
		int effort = (int) calculateOverallEffort();
		int comparedEffort = (int) otherEffort.calculateOverallEffort();

		return effort - comparedEffort;
	}

}
