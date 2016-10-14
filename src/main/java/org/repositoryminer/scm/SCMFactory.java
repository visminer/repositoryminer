package org.repositoryminer.scm;

import org.repositoryminer.exceptions.ErrorMessage;

public class SCMFactory {

	public static ISCM getSCM(SCMType type) {
		switch (type) {
		case GIT:
			return new GitSCM();
		default:
			throw new NullPointerException(ErrorMessage.SCM_NOT_FOUND.toString());
		}
	}
}