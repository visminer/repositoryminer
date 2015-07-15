package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import br.edu.ufba.softvis.visminer.constant.RepositoryType;
import br.edu.ufba.softvis.visminer.constant.WebRepositoryType;

import java.util.List;


/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The persistent class for the repository database table.
 */
@Entity
@Table(name="repository")
@NamedQuery(name="RepositoryDB.findByUid", query="select r from RepositoryDB r where r.uid = :uid")

public class RepositoryDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="REPOSITORY_ID_GENERATOR", sequenceName="REPOSITORY_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="REPOSITORY_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Lob
	private String description;

	@Column(nullable=false, length=100)
	private String name;

	@Column(nullable=false, length=1024)
	private String path;

	@Column(name="remote_url", length=256)
	private String remoteUrl;

	@Column(nullable=false)
	private int type;

	@Column(name="service_type", nullable=false)
	private int serviceType;
	
	@Column(nullable=false, length=40)
	private String uid;

	//bi-directional many-to-one association to CommitterRoleDB
	@OneToMany(mappedBy="repository")
	private List<CommitterRoleDB> committerRoles;

	//bi-directional many-to-one association to IssueDB
	@OneToMany(mappedBy="repository")
	private List<IssueDB> issues;

	//bi-directional many-to-one association to MilestoneDB
	@OneToMany(mappedBy="repository")
	private List<MilestoneDB> milestones;

	//bi-directional many-to-one association to SoftwareUnitDB
	@OneToMany(mappedBy="repository")
	private List<SoftwareUnitDB> softwareUnits;

	//bi-directional many-to-one association to TreeDB
	@OneToMany(mappedBy="repository")
	private List<TreeDB> trees;

	public RepositoryDB() {
	}

	/**
	 * @param id
	 * @param description
	 * @param name
	 * @param path
	 * @param remoteUrl
	 * @param type
	 * @param serviceType
	 * @param uid
	 */
	public RepositoryDB(int id, String description, String name, String path,
			String remoteUrl, RepositoryType type, WebRepositoryType serviceType, String uid) {
		super();
		this.id = id;
		this.description = description;
		this.name = name;
		this.path = path;
		this.remoteUrl = remoteUrl;
		this.type = type.getId();
		this.serviceType = serviceType.getId();
		this.uid = uid;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the remoteUrl
	 */
	public String getRemoteUrl() {
		return remoteUrl;
	}

	/**
	 * @param remoteUrl the remoteUrl to set
	 */
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	/**
	 * @return the type
	 */
	public RepositoryType getType() {
		return RepositoryType.parse(type);
	}

	/**
	 * @param type the type to set
	 */
	public void setType(RepositoryType type) {
		this.type = type.getId();
	}

	/**
	 * @return the serviceType
	 */
	public WebRepositoryType getServiceType() {
		return WebRepositoryType.parse(serviceType);
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(WebRepositoryType serviceType) {
		this.serviceType = serviceType.getId();
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the committerRoles
	 */
	public List<CommitterRoleDB> getCommitterRoles() {
		return committerRoles;
	}

	/**
	 * @param committerRoles the committerRoles to set
	 */
	public void setCommitterRoles(List<CommitterRoleDB> committerRoles) {
		this.committerRoles = committerRoles;
	}

	/**
	 * @return the issues
	 */
	public List<IssueDB> getIssues() {
		return issues;
	}

	/**
	 * @param issues the issues to set
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
	 * @param milestones the milestones to set
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
	 * @param softwareUnits the softwareUnits to set
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
	 * @param trees the trees to set
	 */
	public void setTrees(List<TreeDB> trees) {
		this.trees = trees;
	}

}