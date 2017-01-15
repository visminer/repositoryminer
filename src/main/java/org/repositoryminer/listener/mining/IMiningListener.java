package org.repositoryminer.listener.mining;

import org.repositoryminer.scm.ReferenceType;

public interface IMiningListener {

	public void notifyMiningStart(String repositoryName);
	
	public void notifyMiningEnd(String repositoryName);
	
	public void notifyCommitsMiningStart(int totalCommits);
	
	public void notifyCommitsMiningProgress(String commit, int commitIndex, int totalCommits);
	
	public void notifyCommitsMiningEnd(int totalCommits);
	
	public void notifyWorkingDirectoriesMiningStart(int totalCommits);
	
	public void notifyWorkingDirectoriesMiningProgress(String commit, int commitIndex, int totalCommits);
	
	public void notifyWorkingDirectoriesMiningEnd(int totalCommits);
	
	public void notifyReferencesMiningStart(int totalReferences);
	
	public void notifyReferencesMiningProgress(String name, ReferenceType type, int referenceIndex, int totalReferences);
	
	public void notifyReferencesMiningEnd(int totalReferences);

}