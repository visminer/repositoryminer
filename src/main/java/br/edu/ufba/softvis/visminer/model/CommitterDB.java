package br.edu.ufba.softvis.visminer.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the committer database table.
 * 
 */
@Entity
@Table(name="committer")
@NamedQuery(name="CommitterDB.findAll", query="SELECT c FROM CommitterDB c")
public class CommitterDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="COMMITTER_ID_GENERATOR", sequenceName="COMMITTER_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMMITTER_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=256)
	private String email;

	@Column(nullable=false, length=100)
	private String name;

	//bi-directional many-to-one association to CommitDB
	@OneToMany(mappedBy="committer")
	private List<CommitDB> commits;

	//bi-directional many-to-one association to CommitterRoleDB
	@OneToMany(mappedBy="committer")
	private List<CommitterRoleDB> committerRoles;

	public CommitterDB() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CommitDB> getCommits() {
		return this.commits;
	}

	public void setCommits(List<CommitDB> commits) {
		this.commits = commits;
	}

	public CommitDB addCommit(CommitDB commit) {
		getCommits().add(commit);
		commit.setCommitter(this);

		return commit;
	}

	public CommitDB removeCommit(CommitDB commit) {
		getCommits().remove(commit);
		commit.setCommitter(null);

		return commit;
	}

	public List<CommitterRoleDB> getCommitterRoles() {
		return this.committerRoles;
	}

	public void setCommitterRoles(List<CommitterRoleDB> committerRoles) {
		this.committerRoles = committerRoles;
	}

	public CommitterRoleDB addCommitterRole(CommitterRoleDB committerRole) {
		getCommitterRoles().add(committerRole);
		committerRole.setCommitter(this);

		return committerRole;
	}

	public CommitterRoleDB removeCommitterRole(CommitterRoleDB committerRole) {
		getCommitterRoles().remove(committerRole);
		committerRole.setCommitter(null);

		return committerRole;
	}

}