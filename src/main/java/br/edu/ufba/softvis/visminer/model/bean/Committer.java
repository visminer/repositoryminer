package br.edu.ufba.softvis.visminer.model.bean;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Simple committer bean.
 * This bean is used to simplify interaction between some parts, avoiding coupling and doing smaller core codes.
 */

public class Committer {

	private int id;
	private String email;
	private String name;
	private boolean contribuitor;
	
	public Committer(){}
	
	/**
	 * @param id
	 * @param email
	 * @param name
	 * @param contribuitor
	 */
	public Committer(int id, String email, String name, boolean contribuitor) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.contribuitor = contribuitor;
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

	/**
	 * @return the contribuitor
	 */
	public boolean isContribuitor() {
		return contribuitor;
	}

	/**
	 * @param contribuitor the contribuitor to set
	 */
	public void setContribuitor(boolean contribuitor) {
		this.contribuitor = contribuitor;
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
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		return true;
	}


}