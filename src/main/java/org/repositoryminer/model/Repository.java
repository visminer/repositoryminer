package org.repositoryminer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.scm.SCMType;

public class Repository {

	private String id;
	private String name;
	private String description;
	private String path;
	private SCMType scm;

	private Reference currentReference;
	private List<Commit> commits;
	private List<Reference> branches;
	private List<Reference> tags;
	private List<Contributor> contributors;
	private Map<String, String> workingDirectory;

	private int currentCommit;

	@SuppressWarnings("unchecked")
	public static Map<String, String> parseWorkingDirectory(Document doc) {
		Map<String, String> wd = new HashMap<String, String>();
		for (Document d : (List<Document>) doc.get("files")) {
			wd.put(d.getString("file"), d.getString("checkout"));
		}
		return wd;
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("_id", id).append("name", name).append("description", description).append("path", path)
				.append("scm", scm.toString()).append("contributors", Contributor.toDocumentList(contributors));
		return doc;
	}

	public Repository(org.repositoryminer.mining.RepositoryMiner repo) {
		super();
		this.name = repo.getName();
		this.description = repo.getDescription();
		this.path = repo.getPath();
		this.scm = repo.getScm();
	}

	public Repository(String path) {
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public SCMType getScm() {
		return scm;
	}

	public void setScm(SCMType scm) {
		this.scm = scm;
	}

	public List<Commit> getCommits() {
		return commits;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	public List<Reference> getBranches() {
		return branches;
	}

	public void setBranches(List<Reference> branches) {
		this.branches = branches;
	}

	public List<Reference> getTags() {
		return tags;
	}

	public void setTags(List<Reference> tags) {
		this.tags = tags;
	}

	public List<Contributor> getContributors() {
		return contributors;
	}

	public void setContributors(List<Contributor> contributors) {
		this.contributors = contributors;
	}

	public Map<String, String> getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(Map<String, String> workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	public Commit getCurrentCommit() {
		return commits.get(currentCommit);
	}

	public Reference getCurrentReference() {
		return currentReference;
	}

}