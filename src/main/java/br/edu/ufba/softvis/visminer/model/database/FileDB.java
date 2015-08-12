package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.edu.ufba.softvis.visminer.model.business.File;


/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The persistent class for the file database table.
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

	@Column(unique=true, nullable=false, length=40)
	private String uid;

	//bi-directional many-to-one association to FileXCommitDB
	@OneToMany(mappedBy="file")
	private List<FileXCommitDB> fileXCommits;

	//bi-directional many-to-one association to SoftwareUnitDB
	@OneToMany(mappedBy="file")
	private List<SoftwareUnitDB> softwareUnits;

	public FileDB() {
	}

	/**
	 * @param id
	 * @param path
	 * @param uid
	 */
	public FileDB(int id, String path, String uid) {
		super();
		this.id = id;
		this.path = path;
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
	 * @return the fileXCommits
	 */
	public List<FileXCommitDB> getFileXCommits() {
		return fileXCommits;
	}

	/**
	 * @param fileXCommits the fileXCommits to set
	 */
	public void setFileXCommits(List<FileXCommitDB> fileXCommits) {
		this.fileXCommits = fileXCommits;
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
	
	/**
	 * @return the bizz representation of File
	 */
	public File toBusiness() {
		File file = new File(this.getId(), this.getPath(),
				this.getUid());
		
		return file;
	}

	/**
	 * Converts from DB beans to Bizz beans
	 * 
	 * @param repositories
	 *            collection of FileDB instances
	 * @return collection of "Business" files
	 */
	public static List<File> toBusiness(List<FileDB> files) {
		List<File> bizzFiles = new ArrayList<File>();

		for (FileDB f : files) {
			File file = new File(f.getId(), f.getPath(),
					f.getUid());

			bizzFiles.add(file);
		}

		return bizzFiles;
	}


}