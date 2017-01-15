package org.repositoryminer.listener.mining;

import org.repositoryminer.scm.ReferenceType;

public class NullMiningListener implements IMiningListener {

	@Override
	public void notifyMiningStart(String repositoryName) {
	}

	@Override
	public void notifyMiningEnd(String repositoryName) {
	}

	@Override
	public void notifyCommitsMiningStart(int totalCommits) {
	}

	@Override
	public void notifyCommitsMiningProgress(String commit, int commitIndex, int totalCommits) {
	}

	@Override
	public void notifyCommitsMiningEnd(int totalCommits) {
	}

	@Override
	public void notifyWorkingDirectoriesMiningStart(int totalCommits) {
	}

	@Override
	public void notifyWorkingDirectoriesMiningProgress(String commit, int commitIndex, int totalCommits) {
	}

	@Override
	public void notifyWorkingDirectoriesMiningEnd(int totalCommits) {
	}

	@Override
	public void notifyReferencesMiningStart(int totalReferences) {
	}

	@Override
	public void notifyReferencesMiningProgress(String name, ReferenceType type, int referenceIndex,
			int totalReferences) {
	}

	@Override
	public void notifyReferencesMiningEnd(int totalReferences) {
	}

}