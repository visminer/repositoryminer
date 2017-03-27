package org.repositoryminer.model;

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

	public WorkingDirectory() {
	}

	public WorkingDirectory(String repository) {
		this.repository = repository;
		this.files = new HashMap<String, String>();
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

	@SuppressWarnings("unchecked")
	public static WorkingDirectory parseDocument(Document doc) {
		WorkingDirectory wd = new WorkingDirectory();

		wd.setRepository(doc.getObjectId("repository").toString());
		wd.setId(doc.getString("_id"));

		Map<String, String> files = new HashMap<String, String>();
		for (Document d : (List<Document>) doc.get("files")) {
			files.put(d.getString("file"), d.getString("checkout"));
		}

		wd.setFiles(files);
		return wd;
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