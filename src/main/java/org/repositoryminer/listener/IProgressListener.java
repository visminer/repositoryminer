package org.repositoryminer.listener;

public interface IProgressListener {
	
	public void initCommitsProcessingProgress(int numberOfCommits);
	public void commitProgressChange(int commitIndex, int numberOfCommits);
	
	public void initTimeFramesProcessingProgress();
	
	public void initSourceAnalysisProcessingProgress();
	
	public void endOfProcessingProgress();
}
