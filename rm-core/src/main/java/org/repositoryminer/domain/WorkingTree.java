package org.repositoryminer.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * This class represents a working tree.
 */
public class WorkingTree {

	private String id;
	private String repository;
	private Map<String, String> files = new HashMap<String, String>();

	/**
	 * Converts the working tree to a document.
	 * 
	 * @return a document.
	 */
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

	/**
	 * Converts a document to a working tree.
	 * 
	 * @param document
	 *            the document.
	 * @return a working tree.
	 */
	@SuppressWarnings("unchecked")
	public static WorkingTree parseDocument(Document document) {
		WorkingTree wd = new WorkingTree();

		wd.setRepository(document.getObjectId("repository").toString());
		wd.setId(document.getString("_id"));

		Map<String, String> files = new HashMap<String, String>();
		for (Document d : (List<Document>) document.get("files", List.class)) {
			files.put(d.getString("file"), d.getString("checkout"));
		}

		wd.setFiles(files);
		return wd;
	}

	public WorkingTree() {
	}

	public WorkingTree(String repository) {
		this.repository = repository;
	}
	
	public WorkingTree(String id, String repository, Map<String, String> files) {
		this.id = id;
		this.repository = repository;
		this.files = files;
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