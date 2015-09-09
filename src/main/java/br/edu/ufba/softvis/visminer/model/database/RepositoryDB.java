package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.edu.ufba.softvis.visminer.constant.VersioningSystemType;
import br.edu.ufba.softvis.visminer.constant.WebServiceType;
import br.edu.ufba.softvis.visminer.model.business.Repository;

/**
 * The persistent class for the repository database table.
 */
@Entity
@Table(name = "repository")
@NamedQueries({
	@NamedQuery(name = "RepositoryDB.findByUid", query = "select r from RepositoryDB r where r.uid = :uid"),
	@NamedQuery(name = "RepositoryDB.countByUid", query = "select count(r) from RepositoryDB r where r.uid = :uid")})
public class RepositoryDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "REPOSITORY_ID_GENERATOR", sequenceName = "REPOSITORY_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPOSITORY_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	private int id;

	@Lob
	private String description;

	@Column(nullable = true, length = 100)
	private String name;

	@Column(nullable = true, length = 100)
	private String owner;
	
	@Column(nullable = false, length = 1024)
	private String path;

	@Column(nullable = false)
	private int type;

	@Column(name = "service_type", nullable = false)
	private int serviceType;

	@Column(unique = true, nullable = false, length = 40)
	private String uid;

	@Column(nullable = false, length = 20)
	private String charset;

	@ManyToMany(mappedBy = "repositories")
	private List<CommitterDB> committers;

	// bi-directional many-to-one association to IssueDB
	@OneToMany(mappedBy = "repository")
	private List<IssueDB> issues;

	// bi-directional many-to-one association to MilestoneDB
	@OneToMany(mappedBy = "repository")
	private List<MilestoneDB> milestones;

	// bi-directional many-to-one association to SoftwareUnitDB
	@OneToMany(mappedBy = "repository")
	private List<SoftwareUnitDB> softwareUnits;

	// bi-directional many-to-one association to TreeDB
	@OneToMany(mappedBy = "repository")
	private List<TreeDB> trees;

	public RepositoryDB() {
	}

	public RepositoryDB(int id, String description, String name, String owner,
			String path, VersioningSystemType type,	WebServiceType serviceType,
			String uid, String charset) {
		
		super();
		this.id = id;
		this.description = description;
		this.name = name;
		this.owner = owner;
		this.path = path;
		this.type = type != null ? type.getId() : 0;
		this.serviceType = serviceType != null ? serviceType.getId() : 0;
		this.uid = uid;
		this.charset = charset;
		
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 * the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the type
	 */
	public VersioningSystemType getType() {
		return VersioningSystemType.parse(type);
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(VersioningSystemType type) {
		this.type = type != null ? type.getId() : 0;
	}

	/**
	 * @return the serviceType
	 */
	public WebServiceType getServiceType() {
		return WebServiceType.parse(serviceType);
	}

	/**
	 * @param serviceType
	 *            the serviceType to set
	 */
	public void setServiceType(WebServiceType serviceType) {
		this.serviceType = serviceType != null ? serviceType.getId() : 0;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset
	 *            the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return the committers
	 */
	public List<CommitterDB> getCommitters() {
		return committers;
	}

	/**
	 * @param committers
	 *            the committers to set
	 */
	public void setCommitters(List<CommitterDB> committers) {
		this.committers = committers;
	}

	/**
	 * @return the issues
	 */
	public List<IssueDB> getIssues() {
		return issues;
	}

	/**
	 * @param issues
	 *            the issues to set
	 */
	public void setIssues(List<IssueDB> issues) {
		this.issues = issues;
	}

	/**
	 * @return the milestones
	 */
	public List<MilestoneDB> getMilestones() {
		return milestones;
	}

	/**
	 * @param milestones
	 *            the milestones to set
	 */
	public void setMilestones(List<MilestoneDB> milestones) {
		this.milestones = milestones;
	}

	/**
	 * @return the softwareUnits
	 */
	public List<SoftwareUnitDB> getSoftwareUnits() {
		return softwareUnits;
	}

	/**
	 * @param softwareUnits
	 *            the softwareUnits to set
	 */
	public void setSoftwareUnits(List<SoftwareUnitDB> softwareUnits) {
		this.softwareUnits = softwareUnits;
	}

	/**
	 * @return the trees
	 */
	public List<TreeDB> getTrees() {
		return trees;
	}

	/**
	 * @param trees
	 *            the trees to set
	 */
	public void setTrees(List<TreeDB> trees) {
		this.trees = trees;
	}

	/**
	 * @return the bizz representation of Repository
	 */
	public Repository toBusiness() {
		Repository repository = new Repository(this.getId(),
				this.getDescription(), this.getName(), this.getPath(),
				this.getOwner(), this.getType(), this.getServiceType(),
				this.getUid(), this.getCharset());

		return repository;
	}

	/**
	 * Converts from DB beans to Bizz beans
	 * 
	 * @param repositories
	 *            collection of RepositoryDB instances
	 * @return collection of "Business" repositories
	 */
	public static List<Repository> toBusiness(List<RepositoryDB> repositories) {
		List<Repository> bizzRepos = new ArrayList<Repository>();

		for (RepositoryDB repo : repositories) {
			Repository repository = new Repository(repo.getId(),
					repo.getDescription(), repo.getName(), repo.getPath(),
					repo.getOwner(), repo.getType(), repo.getServiceType(),
					repo.getUid(), repo.getCharset());

			bizzRepos.add(repository);
		}

		return bizzRepos;
	}

}