package org.repositoryminer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;

/**
 * Represents the developer signature. 
 */
public class Developer {

	private String name;
	private String email;

	/**
	 * Converts documents to developers.
	 * 
	 * @param documents
	 * 
	 * @return a list of developers signatures.
	 */
	public static List<Developer> parseDocuments(List<Document> documents) {
		if (documents == null) {
			return new ArrayList<Developer>();
		}
		
		List<Developer> personIdents = new ArrayList<Developer>();
		for (Document doc : documents) {
			Developer c = parseDocument(doc);
			personIdents.add(c);
		}
		return personIdents;
	}
	
	/**
	 * Converts a document to a developer.
	 * 
	 * @param document
	 * 
	 * @return a document.
	 */
	public static Developer parseDocument(Document document) {
		if (document == null) {
			return null;
		}
		
		Developer c = new Developer(document.getString("name"), document.getString("email"));
		return c;
	}

	/**
	 * Converts the person identity to a document.
	 * 
	 * @return a document.
	 */
	public Document toDocument() {
		Document doc = new Document();
		doc.append("name", name).append("email", email);
		return doc;
	}

	/**
	 * Converts a list of developers to documents.
	 * 
	 * @param developers
	 * 
	 * @return a list of documents.
	 */
	public static List<Document> toDocumentList(Collection<Developer> developers) {
		if (developers == null) {
			return new ArrayList<Document>();
		}
		List<Document> list = new ArrayList<Document>();
		for (Developer c : developers) {
			list.add(c.toDocument());
		}
		return list;
	}
	
	public Developer() {
	}

	public Developer(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Developer other = (Developer) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
