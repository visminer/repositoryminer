package org.repositoryminer.mining.model;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.scm.SCMType;

public class Repository {

	private String id;
	private String name;
	private String description;
	private String path;
	private SCMType scm;
	private List<Commit> commits;
	private List<Reference> branches;
	private List<Reference> tags;
	private List<Contributor> contributors;
	private Map<String, String> workingDirectory;
	private int currentCommit;
	private Reference currentReference;

	public Repository() {}

	@SuppressWarnings("unchecked")
	public Repository(Document doc) {
		this.id = doc.getString("_id");
		this.name = doc.getString("name");
		this.description = doc.getString("description");
		this.path = doc.getString("path");
		this.scm = SCMType.valueOf(doc.getString("scm"));
		this.contributors = Contributor.parseDocuments((List<Document>) doc.get("contributors"));
	}

	public void parserReferenceDocs(List<Document> refsDocs) {
		this.branches = Reference.parserReferences(refsDocs, ReferenceType.BRANCH);
		this.tags = Reference.parserReferences(refsDocs, ReferenceType.TAG);
		this.currentReference = Reference.getMaster(this.branches);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the scm
	 */
	public SCMType getScm() {
		return scm;
	}
	/**
	 * @param scm the scm to set
	 */
	public void setScm(SCMType scm) {
		this.scm = scm;
	}
	/**
	 * @return the commits
	 */
	public List<Commit> getCommits() {
		return commits;
	}
	/**
	 * @param commits the commits to set
	 */
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}
	/**
	 * @return the branches
	 */
	public List<Reference> getBranches() {
		return branches;
	}
	/**
	 * @param branches the branches to set
	 */
	public void setBranches(List<Reference> branches) {
		this.branches = branches;
	}
	/**
	 * @return the tags
	 */
	public List<Reference> getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<Reference> tags) {
		this.tags = tags;
	}
	/**
	 * @return the contributors
	 */
	public List<Contributor> getContributors() {
		return contributors;
	}
	/**
	 * @param contributors the contributors to set
	 */
	public void setContributors(List<Contributor> contributors) {
		this.contributors = contributors;
	}
	/**
	 * @return the workingDirectory
	 */
	public Map<String, String> getWorkingDirectory() {
		return workingDirectory;
	}
	/**
	 * @param workingDirectory the workingDirectory to set
	 */
	public void setWorkingDirectory(Map<String, String> workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
	/**
	 * @return the currentCommit
	 */
	public int getCurrentCommit() {
		return currentCommit;
	}
	/**
	 * @param currentCommit the currentCommit to set
	 */
	public void setCurrentCommit(int currentCommit) {
		this.currentCommit = currentCommit;
	}
	/**
	 * @return the currentReference
	 */
	public Reference getCurrentReference() {
		return currentReference;
	}
	/**
	 * @param currentReference the currentReference to set
	 */
	public void setCurrentReference(Reference currentReference) {
		this.currentReference = currentReference;
	}

}
