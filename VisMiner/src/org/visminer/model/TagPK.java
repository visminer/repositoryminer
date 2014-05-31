package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The primary key class for the tag database table.
 * 
 */
@Embeddable
public class TagPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="name", length=255)
	private String name;

	@Column(name="repository_id_git", insertable=false, updatable=false)
	private String repositoryIdGit;

	public TagPK() {
	}
	
	public TagPK(String name, String repositoryIdGit) {
		super();
		this.name = name;
		this.repositoryIdGit = repositoryIdGit;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRepositoryIdGit() {
		return this.repositoryIdGit;
	}
	public void setRepositoryIdGit(String repositoryIdGit) {
		this.repositoryIdGit = repositoryIdGit;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TagPK)) {
			return false;
		}
		TagPK castOther = (TagPK)other;
		return 
			this.name.equals(castOther.name)
			&& this.repositoryIdGit.equals(castOther.repositoryIdGit);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.name.hashCode();
		hash = hash * prime + this.repositoryIdGit.hashCode();
		
		return hash;
	}
}