package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * <p>
 * The persistent class for the committer database table.
 * </p>
 * 
 * @author Felipe
 * @version 1.0
 */
@Entity
@Table(name="committer")
@NamedQuery(name="Committer.findAll", query="SELECT c FROM Committer c")
public class Committer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idcommiter", unique=true, nullable=false)
	private int idcommitter;

	@Column(name="email", nullable=false, length=255)
	private String email;

	@Column(name="name", nullable=false, length=255)
	private String name;

	//bi-directional many-to-one association to Commit
	@OneToMany(mappedBy="committer", fetch=FetchType.LAZY)
	private List<Commit> commits;

	//bi-directional many-to-one association to Repository
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="repository_idGit", nullable=false)
	private Repository repository;

	public Committer() {
	}

	public Committer(String email, String name) {
		super();
		this.email = email;
		this.name = name;
	}

	/**
	 * @return the idcommitter
	 */
	public int getIdcommitter() {
		return idcommitter;
	}

	/**
	 * @param idcommitter the idcommitter to set
	 */
	public void setIdcommitter(int idcommitter) {
		this.idcommitter = idcommitter;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the repository
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	/**
	 * 
	 * @param commit
	 * @return commit added
	 */
	public Commit addCommit(Commit commit) {
		getCommits().add(commit);
		commit.setCommitter(this);

		return commit;
	}

	/**
	 * 
	 * @param commit
	 * @return commit removed
	 */
	public Commit removeCommit(Commit commit) {
		getCommits().remove(commit);
		commit.setCommitter(null);

		return commit;
	}

}