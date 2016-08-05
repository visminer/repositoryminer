package org.repositoryminer.mining.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Contributor {

	private String name;
	private String email;
	
	public Contributor() {}
	
	public Contributor(String name, String email) {
		this.name = name;
		this.email = email;
	}
	
	protected static List<Contributor> parseDocuments(List<Document> docs) {
		List<Contributor> contributors = new ArrayList<Contributor>();
		for (Document doc : docs) {
			Contributor c = new Contributor(doc.getString("name"), doc.getString("email"));
			contributors.add(c);
		}
		return contributors;
	}
	
	protected static Contributor parseDocument(Document doc) {
		Contributor c = new Contributor(doc.getString("name"), doc.getString("email"));
		return c;
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