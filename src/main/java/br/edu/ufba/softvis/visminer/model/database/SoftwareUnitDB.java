package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;

import java.util.List;


/**
 * The persistent class for the software_unit database table.
 * 
 */
@Entity
@Table(name="software_unit")
@NamedQuery(name="SoftwareUnitDB.findAll", query="SELECT s FROM SoftwareUnitDB s")
public class SoftwareUnitDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SOFTWARE_UNIT_ID_GENERATOR", sequenceName="SOFTWARE_UNIT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SOFTWARE_UNIT_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="full_name", nullable=false, length=256)
	private String fullName;

	@Column(nullable=false, length=256)
	private String name;

	@Column(nullable=false)
	private int type;

	@Column(nullable=false, length=40)
	private String uid;

	//bi-directional many-to-one association to FileDB
	@ManyToOne
	@JoinColumn(name="file_id")
	private FileDB file;

	//bi-directional many-to-one association to RepositoryDB
	@ManyToOne
	@JoinColumn(name="repository_id", nullable=false)
	private RepositoryDB repository;

	//bi-directional many-to-one association to SoftwareUnitDB
	@ManyToOne
	@JoinColumn(name="parent_id")
	private SoftwareUnitDB softwareUnit;

	//bi-directional many-to-one association to SoftwareUnitDB
	@OneToMany(mappedBy="softwareUnit")
	private List<SoftwareUnitDB> softwareUnits;

	//bi-directional many-to-many association to CommitDB
	@ManyToMany(mappedBy="softwareUnits")
	private List<CommitDB> commits;

	public SoftwareUnitDB() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SoftwareUnitType getType() {
		return SoftwareUnitType.parse(this.type);
	}

	public void setType(SoftwareUnitType type) {
		this.type = type.getId();
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public FileDB getFile() {
		return this.file;
	}

	public void setFile(FileDB file) {
		this.file = file;
	}

	public RepositoryDB getRepository() {
		return this.repository;
	}

	public void setRepository(RepositoryDB repository) {
		this.repository = repository;
	}

	public SoftwareUnitDB getSoftwareUnit() {
		return this.softwareUnit;
	}

	public void setSoftwareUnit(SoftwareUnitDB softwareUnit) {
		this.softwareUnit = softwareUnit;
	}

	public List<SoftwareUnitDB> getSoftwareUnits() {
		return this.softwareUnits;
	}

	public void setSoftwareUnits(List<SoftwareUnitDB> softwareUnits) {
		this.softwareUnits = softwareUnits;
	}

	public SoftwareUnitDB addSoftwareUnit(SoftwareUnitDB softwareUnit) {
		getSoftwareUnits().add(softwareUnit);
		softwareUnit.setSoftwareUnit(this);

		return softwareUnit;
	}

	public SoftwareUnitDB removeSoftwareUnit(SoftwareUnitDB softwareUnit) {
		getSoftwareUnits().remove(softwareUnit);
		softwareUnit.setSoftwareUnit(null);

		return softwareUnit;
	}

	public List<CommitDB> getCommits() {
		return this.commits;
	}

	public void setCommits(List<CommitDB> commits) {
		this.commits = commits;
	}

}