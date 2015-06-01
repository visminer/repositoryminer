package br.edu.ufba.softvis.visminer.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the file database table.
 * 
 */
@Entity
@Table(name="file")
@NamedQuery(name="FileDB.findAll", query="SELECT f FROM FileDB f")
public class FileDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FILE_ID_GENERATOR", sequenceName="FILE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FILE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=256)
	private String name;

	@Column(nullable=false, length=1024)
	private String path;

	@Column(nullable=false, length=40)
	private String uid;

	//bi-directional many-to-one association to FileXCommitDB
	@OneToMany(mappedBy="file")
	private List<FileXCommitDB> fileXCommits;

	//bi-directional many-to-one association to SoftwareUnitDB
	@OneToMany(mappedBy="file")
	private List<SoftwareUnitDB> softwareUnits;

	public FileDB() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public List<FileXCommitDB> getFileXCommits() {
		return this.fileXCommits;
	}

	public void setFileXCommits(List<FileXCommitDB> fileXCommits) {
		this.fileXCommits = fileXCommits;
	}

	public FileXCommitDB addFileXCommit(FileXCommitDB fileXCommit) {
		getFileXCommits().add(fileXCommit);
		fileXCommit.setFile(this);

		return fileXCommit;
	}

	public FileXCommitDB removeFileXCommit(FileXCommitDB fileXCommit) {
		getFileXCommits().remove(fileXCommit);
		fileXCommit.setFile(null);

		return fileXCommit;
	}

	public List<SoftwareUnitDB> getSoftwareUnits() {
		return this.softwareUnits;
	}

	public void setSoftwareUnits(List<SoftwareUnitDB> softwareUnits) {
		this.softwareUnits = softwareUnits;
	}

	public SoftwareUnitDB addSoftwareUnit(SoftwareUnitDB softwareUnit) {
		getSoftwareUnits().add(softwareUnit);
		softwareUnit.setFile(this);

		return softwareUnit;
	}

	public SoftwareUnitDB removeSoftwareUnit(SoftwareUnitDB softwareUnit) {
		getSoftwareUnits().remove(softwareUnit);
		softwareUnit.setFile(null);

		return softwareUnit;
	}

}