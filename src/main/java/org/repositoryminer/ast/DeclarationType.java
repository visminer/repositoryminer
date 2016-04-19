package org.repositoryminer.ast;

public enum DeclarationType {

	CLASS_OR_INTERFACE, ENUM, ANNOTATION;

	public static DeclarationType parse(String id) {
		for (DeclarationType dt : DeclarationType.values()) {
			if (dt.toString().equals(id))
				return dt;
		}
		return null;
	}

}
