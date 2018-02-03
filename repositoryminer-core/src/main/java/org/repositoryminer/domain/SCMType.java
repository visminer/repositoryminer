package org.repositoryminer.domain;

/**
 * Represents the types of SCM.
 */
public enum SCMType {
	GIT;
	
	public static SCMType parse(String name) {
		if (name == null || name.length() == 0) {
			return null;
		}
		
		for (SCMType scm : values()) {
			if (scm.name().equals(name)) {
				return scm;
			}
		}
		
		return null;
	}
	
}
