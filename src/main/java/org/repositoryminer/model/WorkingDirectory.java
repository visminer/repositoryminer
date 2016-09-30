package org.repositoryminer.model;

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
import org.bson.types.ObjectId;

public class WorkingDirectory {

	private String id;
	private String repository;
	private Map<String, String> files;
	
	public WorkingDirectory() {}
	
	public WorkingDirectory(String repository) {
		this.repository = repository;
		this.files = new HashMap<String, String>();
	}
	
	public void processDiff(List<Diff> diffs) {
		for (Diff d : diffs) {
			if (d.getType() == ADD || d.getType() == MODIFY || d.getType() == COPY) {
				files.put(d.getPath(), id);
			} else if(d.getType() == DELETE) {
				files.remove(d.getPath());
			} else { // d.getType() == RENAME
				files.remove(d.getOldPath());
				files.put(d.getPath(), id);
			}
		}
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("_id", id).append("repository", new ObjectId(repository));
		
		List<Document> filesDoc = new ArrayList<Document>();
		for (Entry<String, String> f : files.entrySet()) {
			Document doc2 = new Document();
			doc2.append("file", f.getKey()).append("checkout", f.getValue());
			filesDoc.add(doc2);
		}
		
		doc.append("files", filesDoc);
		return doc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public Map<String, String> getFiles() {
		return files;
	}

	public void setFiles(Map<String, String> files) {
		this.files = files;
	}
	
}