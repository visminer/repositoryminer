package org.repositoryminer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;

/**
 * This class represents the "personIdent" object in the database. This class
 * represents a person who contributes to the repository.
 */
public class Contributor {

	private String name;
	private String email;
	private String login;
	private String avatarUrl;
	private boolean collaborator;

	public static List<Contributor> parseDocuments(List<Document> docs) {
		List<Contributor> contributors = new ArrayList<Contributor>();
		for (Document doc : docs) {
			Contributor c = new Contributor(doc.getString("name"), doc.getString("email"));
			contributors.add(c);
		}
		return contributors;
	}
	
	public static Contributor parseDocument(Document doc) {
		Contributor c = new Contributor(doc.getString("name"), doc.getString("email"));
		return c;
	}
	
	public Contributor() {
	}

	public Contributor(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public Contributor(String name, String login, String avatarUrl, boolean collaborator) {
		super();
		this.name = name;
		this.login = login;
		this.avatarUrl = avatarUrl;
		this.collaborator = collaborator;
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("name", name).append("email", email);
		return doc;
	}

	public static List<Document> toDocumentList(Collection<Contributor> contributors) {
		List<Document> list = new ArrayList<Document>();
		for (Contributor c : contributors) {
			list.add(c.toDocument());
		}
		return list;
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public boolean isCollaborator() {
		return collaborator;
	}

	public void setCollaborator(boolean collaborator) {
		this.collaborator = collaborator;
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