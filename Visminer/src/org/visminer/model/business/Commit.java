package org.visminer.model.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.visminer.persistence.PersistenceFacade;

public class Commit {

	private int id;
	private String name;
	private String message;
	private Date date;
	private List<File> files;
	
	public Commit(int id, String name, String message, Date date) {
		super();
		this.id = id;
		this.name = name;
		this.message = message;
		this.date = date;
		
		PersistenceFacade persistence = new PersistenceFacade();
		
		List<org.visminer.model.database.File> filesDb = persistence.getFilesByCommit(id);
		files = new ArrayList<File>(filesDb.size());
		
		for(org.visminer.model.database.File f : filesDb){
			File file = new File(f.getId(), f.getPath(), f.getSha(), f.getFileXCommits().get(0).isDeleted());
			files.add(file);
			file.setSoftwareEntitiesFromDB(persistence.getSoftwareEntityByCommitAndFile(this.id, file.getId()));
		}
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

}
