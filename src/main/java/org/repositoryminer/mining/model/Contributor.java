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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
}