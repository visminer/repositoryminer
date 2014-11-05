package org.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the file_x_commit database table.
 * 
 */
@Entity
@Table(name="file_x_commit")
@NamedQuery(name="FileXCommit.findAll", query="SELECT f FROM FileXCommit f")
public class FileXCommit implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FileXCommitPK id;

	@Column(name="deleted", nullable=false)
	private boolean deleted;
	
	//bi-directional many-to-one association to Commit
	@ManyToOne
	private Commit commit;

	//bi-directional many-to-one association to File
	@ManyToOne
	private File file;

	//bi-directional many-to-one association to SoftwareEntity
	@OneToMany(mappedBy="fileXCommit", cascade=CascadeType.PERSIST)
	private List<SoftwareEntity> softwareEntities;

	public FileXCommit() {
	}

	public FileXCommit(FileXCommitPK fileXCommitPK) {
		this.id = fileXCommitPK;
	}

	public FileXCommitPK getId() {
		return this.id;
	}

	public void setId(FileXCommitPK id) {
		this.id = id;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Commit getCommit() {
		return this.commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<SoftwareEntity> getSoftwareEntities() {
		return this.softwareEntities;
	}

	public void setSoftwareEntities(List<SoftwareEntity> softwareEntities) {
		this.softwareEntities = softwareEntities;
	}

	public SoftwareEntity addSoftwareEntity(SoftwareEntity softwareEntity) {
		getSoftwareEntities().add(softwareEntity);
		softwareEntity.setFileXCommit(this);

		return softwareEntity;
	}

	public SoftwareEntity removeSoftwareEntity(SoftwareEntity softwareEntity) {
		getSoftwareEntities().remove(softwareEntity);
		softwareEntity.setFileXCommit(null);

		return softwareEntity;
	}

}