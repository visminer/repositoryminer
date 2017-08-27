package org.repositoryminer.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.scm.ReferenceType;

/**
 * This class represents the "reference" object in the database. This class
 * represents a reference.
 */
public class Reference {

	private String id;
	private String repository;
	private String name;
	private String path;
	private ReferenceType type;
	private List<String> commits;

	@SuppressWarnings("unchecked")
	public static List<Reference> parseDocuments(List<Document> refsDocs, ReferenceType type) {
		List<Reference> refs = new ArrayList<Reference>();
		for (Document doc : refsDocs) {
			if (ReferenceType.valueOf(doc.getString("type")) == type) {
				List<String> commits = new ArrayList<String>();
				
				for (String o : (List<String>) doc.get("commits")) {
					commits.add(o);
				}
				
				Reference r = new Reference(doc.get("_id").toString(), doc.get("repository").toString(), doc.getString("name"),
						doc.getString("path"), type, commits);
				refs.add(r);
			}
		}
		return refs;
	}

	public static List<Reference> parseDocuments(List<Document> refsDocs) {
		List<Reference> refs = new ArrayList<Reference>();
		for (Document doc : refsDocs) {
			refs.add(parseDocument(doc));
		}
		return refs;
	}

	@SuppressWarnings("unchecked")
	public static Reference parseDocument(Document refDoc) {
		List<String> commits = new ArrayList<String>();
		for (String o : (List<String>) refDoc.get("commits")) {
			commits.add(o);
		}
		Reference r = new Reference(refDoc.get("_id").toString(), refDoc.get("repository").toString(), refDoc.getString("name"),
				refDoc.getString("path"), ReferenceType.valueOf(refDoc.getString("type")), commits);
		
		return r;
	}
	
	public Document toDocument() {
		Document doc = new Document();
		doc.append("repository", new ObjectId(repository)).append("name", name).append("path", path)
				.append("type", type.toString()).append("commits", commits);
		return doc;
	}

	public Reference() {
	}

	public Reference(String id, String repository, String name, String path, ReferenceType type,
			List<String> commits) {
		super();
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