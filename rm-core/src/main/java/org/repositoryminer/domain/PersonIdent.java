package org.repositoryminer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;

/**
 * This class represents a person identity who contributes to the repository.
 */
public class PersonIdent {

	private String name;
	private String email;

	/**
	 * Converts documents to persons identities.
	 * @param documents a list of documents.
	 * @return a list of person identities.
	 */
	public static List<PersonIdent> parseDocuments(List<Document> documents) {
		if (documents == null) {
			return new ArrayList<PersonIdent>();
		}
		
		List<PersonIdent> personIdents = new ArrayList<PersonIdent>();
		for (Document doc : documents) {
			PersonIdent c = parseDocument(doc);
			personIdents.add(c);
		}
		return personIdents;
	}
	
	/**
	 * Converts a document to a person identity.
	 * @param document the document.
	 * @return a document.
	 */
	public static PersonIdent parseDocument(Document document) {
		if (document == null) {
			return null;
		}
		
		PersonIdent c = new PersonIdent(document.getString("name"), document.getString("email"));
		return c;
	}

	/**
	 * Converts the person identity to a document.
	 * @return a document.
	 */
	public Document toDocument() {
		Document doc = new Document();
		doc.append("name", name).append("email", email);
		return doc;
	}

	/**
	 * Converts a list of person identities to a documents.
	 * @param personIdents the person identities.
	 * @return a list of documents.
	 */
	public static List<Document> toDocumentList(Collection<PersonIdent> personIdents) {
		if (personIdents == null) {
			return new ArrayList<Document>();
		}
		List<Document> list = new ArrayList<Document>();
		for (PersonIdent c : personIdents) {
			list.add(c.toDocument());
		}
		return list;
	}
	
	public PersonIdent() {
	}

	public PersonIdent(String name, String email) {
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
		PersonIdent other = (PersonIdent) obj;
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