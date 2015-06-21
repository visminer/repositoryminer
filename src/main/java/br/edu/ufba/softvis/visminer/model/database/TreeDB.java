package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import br.edu.ufba.softvis.visminer.constant.TreeType;

import java.util.Date;
import java.util.List;


/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The persistent class for the tree database table.
 */
@Entity
@Table(name="tree")
@NamedQuery(name="TreeDB.findByRepository", query="select t from TreeDB t where t.repository.id = :repositoryId")
public class TreeDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TREE_ID_GENERATOR", sequenceName="TREE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TREE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="full_name", nullable=false, length=255)
	private String fullName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_update", nullable=false)
	private Date lastUpdate;

	@Column(nullable=false, length=100)
	private String name;

	@Column(nullable=false)
	private int type;

	//bi-directional many-to-many association to CommitDB
	@ManyToMany
	@JoinTable(
		name="commit_reference_tree"
		, joinColumns={
			@JoinColumn(name="tree_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="commit_id", nullable=false)
			}
		)
	private List<CommitDB> commits;

	//bi-directional many-to-one association to RepositoryDB
	@ManyToOne
	@JoinColumn(name="repository_id", nullable=false)
	private RepositoryDB repository;

	public TreeDB() {
	}

	/**
	 * @param id
	 * @param fullName
	 * @param lastUpdate
	 * @param name
	 * @param type
	 */
	public TreeDB(int id, String fullName, Date lastUpdate, String name,
			TreeType type) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.lastUpdate = lastUpdate;
		this.name = name;
		this.type = type.getId();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
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
	 * @return the type
	 */
	public TreeType getType() {
		return TreeType.parse(type);
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TreeType type) {
		this.type = type.getId();
	}

	/**
	 * @return the commits
	 */
	public List<CommitDB> getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(List<CommitDB> commits) {
		this.commits = commits;
	}

	/**
	 * @return the repository
	 */
	public RepositoryDB getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(RepositoryDB repository) {
		this.repository = repository;
	}

}