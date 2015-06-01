package br.edu.ufba.softvis.visminer.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the repository database table.
 * 
 */
@Entity
@Table(name="repository")
@NamedQuery(name="RepositoryDB.findAll", query="SELECT r FROM RepositoryDB r")
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

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRemoteUrl() {
		return this.remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public List<CommitterRoleDB> getCommitterRoles() {
		return this.committerRoles;
	}

	public void setCommitterRoles(List<CommitterRoleDB> committerRoles) {
		this.committerRoles = committerRoles;
	}

	public CommitterRoleDB addCommitterRole(CommitterRoleDB committerRole) {
		getCommitterRoles().add(committerRole);
		committerRole.setRepository(this);

		return committerRole;
	}

	public CommitterRoleDB removeCommitterRole(CommitterRoleDB committerRole) {
		getCommitterRoles().remove(committerRole);
		committerRole.setRepository(null);

		return committerRole;
	}

	public List<IssueDB> getIssues() {
		return this.issues;
	}

	public void setIssues(List<IssueDB> issues) {
		this.issues = issues;
	}

	public IssueDB addIssue(IssueDB issue) {
		getIssues().add(issue);
		issue.setRepository(this);

		return issue;
	}

	public IssueDB removeIssue(IssueDB issue) {
		getIssues().remove(issue);
		issue.setRepository(null);

		return issue;
	}

	public List<MilestoneDB> getMilestones() {
		return this.milestones;
	}

	public void setMilestones(List<MilestoneDB> milestones) {
		this.milestones = milestones;
	}

	public MilestoneDB addMilestone(MilestoneDB milestone) {
		getMilestones().add(milestone);
		milestone.setRepository(this);

		return milestone;
	}

	public MilestoneDB removeMilestone(MilestoneDB milestone) {
		getMilestones().remove(milestone);
		milestone.setRepository(null);

		return milestone;
	}

	public List<SoftwareUnitDB> getSoftwareUnits() {
		return this.softwareUnits;
	}

	public void setSoftwareUnits(List<SoftwareUnitDB> softwareUnits) {
		this.softwareUnits = softwareUnits;
	}

	public SoftwareUnitDB addSoftwareUnit(SoftwareUnitDB softwareUnit) {
		getSoftwareUnits().add(softwareUnit);
		softwareUnit.setRepository(this);

		return softwareUnit;
	}

	public SoftwareUnitDB removeSoftwareUnit(SoftwareUnitDB softwareUnit) {
		getSoftwareUnits().remove(softwareUnit);
		softwareUnit.setRepository(null);

		return softwareUnit;
	}

	public List<TreeDB> getTrees() {
		return this.trees;
	}

	public void setTrees(List<TreeDB> trees) {
		this.trees = trees;
	}

	public TreeDB addTree(TreeDB tree) {
		getTrees().add(tree);
		tree.setRepository(this);

		return tree;
	}

	public TreeDB removeTree(TreeDB tree) {
		getTrees().remove(tree);
		tree.setRepository(null);

		return tree;
	}

}