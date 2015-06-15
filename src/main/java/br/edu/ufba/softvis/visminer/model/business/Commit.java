package br.edu.ufba.softvis.visminer.model.business;

import java.util.Date;
import java.util.List;

public class Commit {

	private int id;
	private Date date;
	private String message;
	private String name;
	private List<File> commitedFiles;
	private Committer committer;
	
	public Commit(){}
	
	public Commit(int id, Date date, String message, String name) {
		super();
		this.id = id;
		this.date = date;
		this.message = message;
		this.name = name;
	}

	public Commit(Date date, String message, String name) {
		super();
		this.date = date;
		this.message = message;
		this.name = name;
	}	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<File> getCommitedFiles() {
		return commitedFiles;
	}

	public void setCommitedFiles(List<File> commitedFiles) {
		this.commitedFiles = commitedFiles;
	}

	public Committer getCommitter() {
		return committer;
	}

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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Commit other = (Commit) obj;
		if (id != other.id)
			return false;
		return true;
	}

}