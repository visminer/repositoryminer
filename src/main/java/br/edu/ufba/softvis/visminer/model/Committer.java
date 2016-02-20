package br.edu.ufba.softvis.visminer.model;

import org.bson.Document;

/**
 * User friendly committer bean class. This class will be used for user
 * interface.
 */
public class Committer {

	private String email;
	private String name;

	public Committer() {
	}

	/**
	 * @param id
	 * @param email
	 * @param name
	 */
	public Committer(String email, String name) {
		super();
		this.email = email;
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
	 * @return mongo's document format of committer
	 */
	public Document toDocument() {
		Document doc = new Document("email", getEmail()).append("message",
				getName());

		return doc;
	}

	@Override
	public int hashCode() {
		return email.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Committer)) {
			return false;
		}

		Committer other = (Committer) obj;
		return email.equals(other.getEmail());
	}

}