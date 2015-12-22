package br.edu.ufba.softvis.visminer.model.business;

/**
 * User friendly committer bean class.
 * This class will be used for user interface.
 */

public class Committer {

	private int id;
	private String email;
	private String name;
	
	public Committer(){}

	/**
	 * @param id
	 * @param email
	 * @param name
	 */
	public Committer(int id, String email, String name) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
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
		if (id != other.id) {
			return false;
		}
		return true;
	}
	
}