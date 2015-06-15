package br.edu.ufba.softvis.visminer.model.business;

import java.util.List;

public class Committer {

	private int id;
	private String email;
	private String name;
	private boolean contribuitor;
	private List<Commit> commits;
	
	public Committer(){}
	
	public Committer(int id, String email, String name, boolean contribuitor) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.contribuitor = contribuitor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isContribuitor() {
		return contribuitor;
	}

	public void setContribuitor(boolean contribuitor) {
		this.contribuitor = contribuitor;
	}

	public List<Commit> getCommits() {
		return commits;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
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
		Committer other = (Committer) obj;
		if (id != other.id)
			return false;
		return true;
	}


}