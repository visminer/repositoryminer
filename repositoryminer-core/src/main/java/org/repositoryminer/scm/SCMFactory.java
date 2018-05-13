package org.repositoryminer.scm;

import org.repositoryminer.domain.SCMType;

/**
 * Factory class for ISCM.
 */
public class SCMFactory {

	public static ISCM getSCM(SCMType type) {
		switch (type) {
		case GIT:
			return new GitSCM();
		default:
			return null;
		}
	}

}