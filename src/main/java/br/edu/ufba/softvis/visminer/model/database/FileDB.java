package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.JoinFetch;
import org.eclipse.persistence.annotations.JoinFetchType;

import java.util.List;


/**
 * The persistent class for the file database table.
 * 
 */
@Entity
@Table(name="file")
@NamedQueries({
	@NamedQuery(name="FileDB.findByCode", query="select f.id from FileDB f where f.uid = :uid"),
	@NamedQuery(name="FileDB.findCommitedFiles", query="select f from FileDB f join f.fileXCommits "
			+ "fxc where fxc.id.commitId = :id")
})

public class FileDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FILE_ID_GENERATOR", sequenceName="FILE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FILE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

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

	public FileDB(br.edu.ufba.softvis.visminer.model.bean.File file){
		this.id = file.getId();
		this.path = file.getPath();
		this.uid = file.getUid();
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileDB other = (FileDB) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

}