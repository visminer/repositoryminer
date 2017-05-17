package org.repositoryminer.listener.mining;

import org.repositoryminer.scm.ReferenceType;

public interface IMiningListener {

	public void notifyMiningStart(String repositoryName);

	public void notifyMiningEnd(String repositoryName);

	public void notifyReferencesMiningStart(int referencesQtd);

	public void notifyReferencesMiningProgress(String referenceName, ReferenceType referenceType);

	public void notifyReferencesMiningEnd(int referencesQtd);

	public void notifyCommitsMiningStart(String referenceName, ReferenceType referenceType, int commitsQtd);

	public void notifyCommitsMiningProgress(String referenceName, ReferenceType referenceType, String commit);

	public void notifyCommitsMiningEnd(String referenceName, ReferenceType referenceType, int commitsQtd);

	public void notifyWorkingDirectoriesMiningStart(String referenceName, ReferenceType referenceType, int commitsQtd);

	public void notifyWorkingDirectoriesMiningProgress(String referenceName, ReferenceType referenceType, String commit);

	public void notifyWorkingDirectoriesMiningEnd(String referenceName, ReferenceType referenceType, int commitsQtd);
	
	public void notifyDirectCodeAnalysisStart(int totalCommits);
	
	public void notifyDirectCodeAnalysisProgress(String commit, int index, int totalCommits);
	
	public void notifyDirectCodeAnalysisEnd(int totalCommits);

	public void notifyIndirectCodeAnalysisStart(int totalSnapshots);
	
	public void notifyIndirectCodeAnalysisProgress(String snapshot, int index, int totalSnapshots);
	
	public void notifyIndirectCodeAnalysisEnd(int totalSnapshots);
	
}