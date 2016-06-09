package org.repositoryminer.persistence.model;

import static org.repositoryminer.scm.DiffType.ADD;
import static org.repositoryminer.scm.DiffType.COPY;
import static org.repositoryminer.scm.DiffType.DELETE;
import static org.repositoryminer.scm.DiffType.MODIFY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

public class WorkingDirectory {

	private String id;
	private String repository;
	private String checkout;
	private Map<String, String> files;
	
	public WorkingDirectory() {}
	
	public WorkingDirectory(String repository) {
		this.repository = repository;
		this.files = new HashMap<String, String>();
	}
	
	public void processDiff(List<Diff> diffs) {
		for (Diff d : diffs) {
			if (d.getType() == ADD || d.getType() == MODIFY || d.getType() == COPY) {
				files.put(d.getPath(), checkout);
			} else if(d.getType() == DELETE) {
				files.remove(d.getPath());
			} else { // d.getType() == RENAME
				files.remove(d.getOldPath());
				files.put(d.getPath(), checkout);
			}
		}
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("repository", repository).append("checkout", checkout);
		
		List<Document> filesDoc = new ArrayList<Document>();
		for (Entry<String, String> f : files.entrySet()) {
			Document doc2 = new Document();
			doc2.append("file", f.getKey()).append("checkout", f.getValue());
			filesDoc.add(doc2);
		}
		
		doc.append("files", filesDoc);
		return doc;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the repository
	 */
	public String getRepository() {
		return repository;
	}
	/**
	 * @param repository the repository to set
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}
	/**
	 * @return the checkout
	 */
	public String getCheckout() {
		return checkout;
	}
	/**
	 * @param checkout the checkout to set
	 */
	public void setCheckout(String checkout) {
		this.checkout = checkout;
	}
	/**
	 * @return the files
	 */
	public Map<String, String> getFiles() {
		return files;
	}
	/**
	 * @param files the files to set
	 */
	public void setFiles(Map<String, String> files) {
		this.files = files;
	}
	
}