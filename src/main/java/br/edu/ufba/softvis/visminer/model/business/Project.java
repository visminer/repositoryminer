package br.edu.ufba.softvis.visminer.model.business;

import java.util.List;

public class Project {

	private List<Committer> committers;
	private List<Commit> commits;
	private List<File> files;
	private List<SoftwareUnit> softwareUnits;
	private Tree currentTree;
	private Commit currentCommit;
	
	public List<Committer> getCommitters() {
		return committers;
	}
	public void setCommitters(List<Committer> committers) {
		this.committers = committers;
	}
	public List<Commit> getCommits() {
		return commits;
	}
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}
	public List<File> getFiles() {
		return files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}
	public List<SoftwareUnit> getSoftwareUnits() {
		return softwareUnits;
	}
	public void setSoftwareUnits(List<SoftwareUnit> softwareUnits) {
		this.softwareUnits = softwareUnits;
	}
	public Tree getCurrentTree() {
		return currentTree;
	}
	public void setCurrentTree(Tree currentTree) {
		this.currentTree = currentTree;
	}
	public Commit getCurrentCommit() {
		return currentCommit;
	}
	public void setCurrentCommit(Commit currentCommit) {
		this.currentCommit = currentCommit;
	}
	
}