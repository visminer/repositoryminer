package br.edu.ufba.softvis.visminer.model.business;

import java.util.List;

import br.edu.ufba.softvis.visminer.constant.RepositoryServiceType;
import br.edu.ufba.softvis.visminer.constant.RepositoryType;

public class Repository {

	private int id;
	private String description;
	private String name;
	private String path;
	private String remoteUrl;
	private RepositoryType type;
	private RepositoryServiceType serviceType;
	private String uid;
	
	private List<Committer> committers;
	private List<Tree> trees;
	private Project project;
	
	public Repository(){}
	
	public Repository(int id, String description, String name, String path,
			String remoteUrl, RepositoryType type, RepositoryServiceType serviceType, String uid) {
		
		super();
		this.id = id;
		this.description = description;
		this.name = name;
		this.path = path;
		this.remoteUrl = remoteUrl;
		this.type = type;
		this.serviceType = serviceType;
		this.uid = uid;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public RepositoryType getType() {
		return type;
	}

	public RepositoryServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(RepositoryServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public void setType(RepositoryType type) {
		this.type = type;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public List<Committer> getCommitters() {
		return committers;
	}

	public void setCommitters(List<Committer> committers) {
		this.committers = committers;
	}

	public List<Tree> getTrees() {
		return trees;
	}

	public void setTrees(List<Tree> trees) {
		this.trees = trees;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Repository other = (Repository) obj;
		if (id != other.id)
			return false;
		return true;
	}	
	
}
