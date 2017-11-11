package org.repositoryminer.domain;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Represents a reference to a commit (e.g. branches and tags).
 */
public class Reference {

	private ObjectId id;
	private ObjectId repository;
	private String name;
	private String path;
	private ReferenceType type;
	private List<String> commits;

	/**
	 * Converts a list of documents to references.
	 * 
	 * @param documents
	 * 
	 * @return a list of references.
	 */
	public static List<Reference> parseDocuments(List<Document> documents) {
		List<Reference> refs = new ArrayList<Reference>();
		for (Document doc : documents) {
			refs.add(parseDocument(doc));
		}
		return refs;
	}

	/**
	 * Converts a document to a reference.
	 * 
	 * @param document
	 * 
	 * @return a reference.
	 */
	@SuppressWarnings("unchecked")
	public static Reference parseDocument(Document document) {
		Reference r = new Reference(document.getObjectId("_id"),
				document.getObjectId("repository"),
				document.getString("name"), document.getString("path"),
				ReferenceType.valueOf(document.getString("type")), document.get("commits", List.class));

		return r;
	}

	/**
	 * Converts a reference to a document.
	 * 
	 * @return a document.
	 */
	public Document toDocument() {
		Document doc = new Document();
		doc.append("repository", repository).append("name", name).append("path", path)
		.append("type", type.toString()).append("commits", commits);
		return doc;
	}

	public Reference() {}

	public Reference(ObjectId id, ObjectId repository, String name, String path, ReferenceType type,
			List<String> commits) {
		this.id = id;
		this.repository = repository;
		this.name = name;
		this.path = path;
		this.type = type;
		this.commits = commits;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getRepository() {
		return repository;
	}

	public void setRepository(ObjectId repository) {
		this.repository = repository;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ReferenceType getType() {
		return type;
	}

	public void setType(ReferenceType type) {
		this.type = type;
	}

	public List<String> getCommits() {
		return commits;
	}

	public void setCommits(List<String> commits) {
		this.commits = commits;
	}

}