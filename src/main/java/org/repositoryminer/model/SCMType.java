package org.repositoryminer.model;

public enum SCMType {

	GIT("GIT");

	private String id;

	private SCMType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public static SCMType parse(String id) {
		for (SCMType st : SCMType.values()) {
			if (st.getId().equals(id))
				return st;
		}
		return null;
	}

}
