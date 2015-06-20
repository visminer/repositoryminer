package br.edu.ufba.softvis.visminer.model.business;

import java.util.List;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * User friendly committer bean class.
 * This class will be used for user interface.
 */

public class Committer {

	private int id;
	private String email;
	private String name;
	private boolean contribuitor;
	private List<Commit> commits;
	
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

	/**
	 * @return the commits
	 */
	public List<Commit> getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
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