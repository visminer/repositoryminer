package org.repositoryminer.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * This class represents a reference (e.g. branches and tags).
 */
public class Reference {

	private String id;
	private String repository;
	private String name;
	private String path;
	private ReferenceType type;
	private List<String> commits;

	/**
	 * Converts a list of documents to references.
	 * 
	 * @param documents
	 *            the documents.
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
	 *            the document.
	 * @return a reference.
	 */
	@SuppressWarnings("unchecked")
	public static Reference parseDocument(Document document) {
		Reference r = new Reference(document.get("_id").toString(), document.get("repository").toString(),
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
		doc.append("repository", new ObjectId(repository)).append("name", name).append("path", path)
		.append("type", type.toString()).append("commits", commits);
		return doc;
	}

	public Reference() {
	}

	public Reference(String id, String repository, String name, String path, ReferenceType type, List<String> commits) {
		this.id = id;
		this.repository = repository;
		this.name = name;
		this.path = path;
		this.type = type;
		this.commits = commits;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		Reference other = (Reference) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

}