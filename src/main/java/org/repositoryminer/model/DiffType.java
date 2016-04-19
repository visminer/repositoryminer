package org.repositoryminer.model;

public enum DiffType {

	ADD("ADD"), COPY("COPY"), MODIFY("MODIFY"), RENAME("RENAME"), DELETE("DELETE");

	private String id;

	private DiffType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public static DiffType parse(String id) {
		for (DiffType dt : DiffType.values()) {
			if (dt.getId().equals(id))
				return dt;
		}
		return null;
	}

}