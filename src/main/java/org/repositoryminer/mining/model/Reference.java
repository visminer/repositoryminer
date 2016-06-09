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
	protected static List<Reference> parserReferences(List<Document> refsDocs, ReferenceType type) {
		List<Reference> refs = new ArrayList<Reference>();
		for (Document doc : refsDocs) {
			if (ReferenceType.valueOf(doc.getString("type")) == type) {
				List<String> commits = new ArrayList<String>();
				for (Document o : (List<Document>) doc.get("commits")) {
					commits.add(o.toString());
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the type
	 */
	public ReferenceType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(ReferenceType type) {
		this.type = type;
	}

	/**
	 * @return the commits
	 */
	public List<String> getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(List<String> commits) {
		this.commits = commits;
	}

}
