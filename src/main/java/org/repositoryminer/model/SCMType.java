package org.repositoryminer.model;

public enum SCMType {

	GIT("GIT");

	private String key;

	private SCMType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public static SCMType parse(String key) {
		for (SCMType st : SCMType.values()) {
			if (st.getKey().equals(key))
				return st;
		}
		return null;
	}

}
