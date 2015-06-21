package br.edu.ufba.softvis.visminer.model.business;

import java.util.Date;
import java.util.List;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * User friendly commit bean class.
 * This class will be used for user interface.
 */

public class Commit {

	private int id;
	private Date date;
	private String message;
	private String name;
	private List<File> commitedFiles;
	private Committer committer;
	
	public Commit(){}

	/**
	 * @param id
	 * @param date
	 * @param message
	 * @param name
	 */
	public Commit(int id, Date date, String message, String name) {
		super();
		this.id = id;
		this.date = date;
		this.message = message;
		this.name = name;
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
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * @return the commitedFiles
	 */
	public List<File> getCommitedFiles() {
		return commitedFiles;
	}

	/**
	 * @param commitedFiles the commitedFiles to set
	 */
	public void setCommitedFiles(List<File> commitedFiles) {
		this.commitedFiles = commitedFiles;
	}

	/**
	 * @return the committer
	 */
	public Committer getCommitter() {
		return committer;
	}

	/**
	 * @param committer the committer to set
	 */
	public void setCommitter(Committer committer) {
		this.committer = committer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Commit)) {
			return false;
		}
		Commit other = (Commit) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}