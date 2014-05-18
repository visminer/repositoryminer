package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the branch database table.
 * 
 */
@Entity
@Table(name="branch")
@NamedQuery(name="Branch.findAll", query="SELECT b FROM Branch b")
public class Branch implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BranchPK id;

	//bi-directional many-to-one association to Repository
	@ManyToOne
	private Repository repository;

	public Branch() {
	}

	public BranchPK getId() {
		return this.id;
	}

	public void setId(BranchPK id) {
		this.id = id;
	}

	public Repository getRepository() {
		return this.repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}