package org.repositoryminer.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.mining.RepositoryExplorer;
import org.repositoryminer.scm.ReferenceType;
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

	@SuppressWarnings("unchecked")
	public static void initRepository(Document doc, Repository repository) {
		repository.id = doc.getString("_id");
		repository.name = doc.getString("name");
		repository.description = doc.getString("description");
		repository.path = doc.getString("path");
		repository.scm = SCMType.valueOf(doc.getString("scm"));
		repository.contributors = Contributor.parseDocuments((List<Document>) doc.get("contributors"));
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

	public Document getMetrics(String file, String commit) {
		return RepositoryExplorer.getMetricMeasures(file, commit);
	}

	public Document getCodeSmells(String file, String commit) {
		return RepositoryExplorer.getCodeSmellsMeasures(file, commit);
	}

	public Document getTechnicalDebtss(String file, String commit) {
		return RepositoryExplorer.getTechnicalDebtsMeasures(file, commit);
	}

	public Document getMeasures(String file, String commit) {
		return RepositoryExplorer.getAllMeasures(file, commit);
	}

	public void lastCommit() {
		currentCommit = commits.size() - 1;
		RepositoryExplorer.mineCurrentCommit(this);
	}

	public void firstCommit() {
		currentCommit = 0;
		RepositoryExplorer.mineCurrentCommit(this);
	}

	public boolean previousCommit() {
		if (currentCommit == 0) {
			return false;
		}
		currentCommit -= 1;
		RepositoryExplorer.mineCurrentCommit(this);
		return true;
	}

	public boolean nextCommit() {
		if (currentCommit == commits.size() - 1) {
			return false;
		}
		currentCommit += 1;
		RepositoryExplorer.mineCurrentCommit(this);
		return true;
	}

	/** TODO: Implement support for new reference type **/
	public void setCurrentReference(ReferenceType type, String name) {
		List<Reference> refs = null;
		switch (type) {
		case BRANCH:
			refs = branches;
			break;
		case TAG:
			refs = tags;
			break;
		default:
			break;
		}

		for (Reference r : refs) {
			if (r.getName().equals(name)) {
				setCurrentReference(r);
				;
				break;
			}
		}
	}

	public List<Commit> getCommitsFromAuthor(Contributor contributor) {
		List<Commit> commitsAux = new ArrayList<Commit>();
		for (Commit c : commits) {
			if (c.getAuthor().equals(contributor)) {
				commitsAux.add(c);
			}
		}
		return commitsAux;
	}

	public List<Commit> getCommitsFromCommitter(Contributor contributor) {
		List<Commit> commitsAux = new ArrayList<Commit>();
		for (Commit c : commits) {
			if (c.getCommitter().equals(contributor)) {
				commitsAux.add(c);
			}
		}
		return commitsAux;
	}

	public List<Contributor> getCommittersUntilCommit(Commit commit) {
		int index = commits.indexOf(commit);
		if (index == -1) {
			return new ArrayList<Contributor>();
		}

		Set<Contributor> contribsSet = new HashSet<Contributor>();
		for (int i = 0; i <= index; i++) {
			contribsSet.add(commits.get(i).getCommitter());
		}
		return new ArrayList<Contributor>(contribsSet);
	}

	public List<Contributor> getAuthorsUntilCommit(Commit commit) {
		int index = commits.indexOf(commit);
		if (index == -1) {
			return new ArrayList<Contributor>();
		}

		Set<Contributor> contribsSet = new HashSet<Contributor>();
		for (int i = 0; i <= index; i++) {
			contribsSet.add(commits.get(i).getAuthor());
		}
		return new ArrayList<Contributor>(contribsSet);
	}

	public boolean setCurrentCommit(Commit currentCommit) {
		if (commits != null) {
			int i = commits.indexOf(currentCommit);
			if (i >= 0) {
				this.currentCommit = i;
				return true;
			}
			return false;
		}
		return false;
	}

	public void setCurrentReference(Reference currentReference) {
		this.currentReference = currentReference;
		RepositoryExplorer.mineCurrentReference(this);
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

	public void mine() throws IOException {
		RepositoryExplorer.mineRepository(this);
	}


}