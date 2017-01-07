package org.repositoryminer.listener;

import org.repositoryminer.scm.ReferenceType;

public interface IListener {

	public String getName();
	
	public void notifyStart(String name);
	
	public void notifyEnd();
	
	public void notifyStartCommitsMining(int totalCommits);
	
	public void notifyCommitMining(String commit);
	
	public void notifyEndCommitsMining();
	
	public void notifyStartWorkingDirectoriesMining(int totalCommits);
	
	public void notifyWorkingDirectoryMining(String commit);
	
	public void notifyEndWorkingDirectoriesMining();
	
	public void notifyStartReferencesMining(int totalReferences);
	
	public void notifyReferenceMining(String name, ReferenceType type);
	
	public void notifyEndReferencesMining();

}