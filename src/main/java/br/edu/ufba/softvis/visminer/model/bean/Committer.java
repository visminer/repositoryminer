package br.edu.ufba.softvis.visminer.model.bean;

public class Committer {

	private int id;
	private String email;
	private String name;
	private boolean contribuitor;
	
	public Committer(){}
	
	public Committer(int id, String email, String name, boolean contribuitor) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.contribuitor = contribuitor;
	}

	public Committer(String email, String name, boolean contribuitor) {
		super();
		this.email = email;
		this.name = name;
		this.contribuitor = contribuitor;
	}	
	
	public Committer(String email, String name) {
		super();
		this.email = email;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isContribuitor() {
		return contribuitor;
	}

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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Committer other = (Committer) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

}