package org.repositoryminer.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

/**
 * This class represents the "personIdent" object in the database. This class
 * represents a person who contributes to the repository.
 */
public class Contributor {

	private String name;
	private String email;

	public Contributor() {
	}

	public Contributor(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("name", name).append("email", "email");
		return doc;
	}

	public static List<Document> toDocumentList(List<Contributor> contributors) {
		List<Document> list = new ArrayList<Document>();
		for (Contributor c : contributors) {
			list.add(c.toDocument());
		}
		return list;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		Contributor other = (Contributor) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

}