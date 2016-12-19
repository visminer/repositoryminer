package org.repositoryminer.listener;

/**
 * <h1>IMiningListener is a mining progress monitoring interface</h1>
 * <p>
 * Since RepositoryMiner is <b>NOT</b> intended to be executed directly, having
 * the actual purpose of serving as an API for other applicationsit is important
 * to provide a general contract for listeners/observers.
 * <p>
 * Front-end applications can implement the listener to meet specific monitoring
 * needs, such as animating progress bars and/or activate routines in response
 * to progress changes.
 * <p>
 * Listeners can be injected into
 * {@link org.repositoryminer.miner.RepositoryMiner#setMiningListener(IMiningListener)}
 * in order to be activated.
 * <p>
 */
public interface IMiningListener {
	
	/**
	 * Notifies the initiation of mining
	 * 
	 * @param name
	 *            the name of the project being mined
	 */
	public void initMining(String name);

	/**
	 * Notifies the progress of commits processing
	 * 
	 * @param commitIndex
	 *            the current index of the commit being processed
	 * @param numberOfCommits
	 *            total amount of commits to be processed
	 */
	public void initCommitsMining();

	/**
	 * Notifies the progress of commits processing
	 *
	 * @param reference
	 *            the name of the reference which the commit belongs to
	 * @param commit
	 *            the commit name
	 * @param commitIndex
	 *            the current index of the commit being processed
	 * @param numberOfCommits
	 *            total amount of commits to be processed
	 */
	public void commitsProgressChange(String reference, String commit, int commitIndex, int numberOfCommits);


	/**
	 * Notifies the progress of working directory states processing
	 * 
	 * @param reference
	 *            the name of the reference which the commit belongs to
	 * @param commit
	 *            the commit name being processed
	 * @param commitIndex
	 *            the current index of the commit being processed
	 * @param numberOfCommits
	 *            total amount of commits to be processed
	 */
	public void workingDirectoryProgressChange(String reference, String commit, int commitIndex, int numberOfCommits);

	/**
	 * Notifies the progress of tags processing
	 * 
	 * @param reference
	 *            the name of the tag being processed
	 * @param tagIndex
	 *            the current index of the tag being processed
	 * @param numberOfTags
	 *            total number of tags to be processed
	 */
	public void tagsProgressChange(String tag, int tagIndex, int numberOfTags);
}