package org.repositoryminer.listener;

import org.repositoryminer.scm.ReferenceType;

public class ConsoleListener implements IListener {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifyStart(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyStartCommitsMining(int totalCommits) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyCommitMining(String commit) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyEndCommitsMining() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyStartWorkingDirectoriesMining(int totalCommits) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyWorkingDirectoryMining(String commit) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyEndWorkingDirectoriesMining() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyStartReferencesMining(int totalReferences) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyReferenceMining(String name, ReferenceType type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyEndReferencesMining() {
		// TODO Auto-generated method stub

	}

}
