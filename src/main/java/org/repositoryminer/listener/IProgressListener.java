package org.repositoryminer.listener;

public interface IProgressListener {

	public void initMining(String name);
	
	public void commitsProgressChange(int commitIndex, int numberOfCommits);

	public void initTimeFramesProcessingProgress();
	
	public void timeFramesProgressChange(int timeFrameIndex, int numberOfTimeFrames);

	public void initSourceAnalysisProcessingProgress();
	
	public void tagsProgressChange(int tagIndex, int numberOfTags);

	public void endOfMining();
}
