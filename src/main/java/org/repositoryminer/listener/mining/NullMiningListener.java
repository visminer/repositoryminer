package org.repositoryminer.listener.mining;

import org.repositoryminer.scm.ReferenceType;

public class NullMiningListener implements IMiningListener {

	@Override
	public void notifyMiningStart(String repositoryName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyMiningEnd(String repositoryName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyReferencesMiningStart(int referencesQtd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyReferencesMiningProgress(String referenceName, ReferenceType referenceType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyReferencesMiningEnd(int referencesQtd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyCommitsMiningStart(String referenceName, ReferenceType referenceType, int commitsQtd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyCommitsMiningProgress(String referenceName, ReferenceType referenceType, String commit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyCommitsMiningEnd(String referenceName, ReferenceType referenceType, int commitsQtd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyWorkingDirectoriesMiningStart(String referenceName, ReferenceType referenceType, int commitsQtd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyWorkingDirectoriesMiningProgress(String referenceName, ReferenceType referenceType,
			String commit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyWorkingDirectoriesMiningEnd(String referenceName, ReferenceType referenceType, int commitsQtd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyDirectCodeAnalysisStart(int totalCommits) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyDirectCodeAnalysisProgress(String commit, int index, int totalCommits) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyDirectCodeAnalysisEnd(int totalCommits) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyIndirectCodeAnalysisStart(int totalSnapshots) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyIndirectCodeAnalysisProgress(String snapshot, int index, int totalSnapshots) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyIndirectCodeAnalysisEnd(int totalSnapshots) {
		// TODO Auto-generated method stub
		
	}

}