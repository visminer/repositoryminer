package org.repositoryminer.model;

public enum DiffType {

	ADD("ADD"), COPY("COPY"), MODIFY("MODIFY"), RENAME("RENAME"), DELETE("DELETE");

	private String key;

	private DiffType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public static DiffType parse(String key) {
		for (DiffType dt : DiffType.values()) {
			if (dt.getKey().equals(key))
				return dt;
		}
		return null;
	}

}