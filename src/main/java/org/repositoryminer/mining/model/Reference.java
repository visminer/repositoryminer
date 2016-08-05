package org.repositoryminer.mining.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.scm.ReferenceType;

public class Reference {

	private String id;
	private String name;
	private String fullName;
	private ReferenceType type;
	private List<String> commits;

	public Reference() {}
	
	public Reference(String id, String name, String fullName, ReferenceType type, 
			List<String> commits) {
		super();
		this.id = id;
		this.name = name;
		this.fullName = fullName;
		this.type = type;
		this.commits = commits;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Reference> parseDocuments(List<Document> refsDocs, ReferenceType type) {
		List<Reference> refs = new ArrayList<Reference>();
		for (Document doc : refsDocs) {
			if (ReferenceType.valueOf(doc.getString("type")) == type) {
				List<String> commits = new ArrayList<String>();
				for (String o : (List<String>) doc.get("commits")) {
					commits.add(o);
				}
				Reference r = new Reference(doc.getString("_id"), doc.getString("name"), 
						doc.getString("full_name"), type, commits);
				refs.add(r);
			}
		}
		return refs;
	}
	
	public static Reference getMaster(List<Reference> branches) {
		for (Reference r : branches) {
			if (r.getName().equals("master"))
				return r;
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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