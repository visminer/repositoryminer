package org.repositoryminer.listener;

/**
 * <h1>IPostMiningListener is a post mining tasks progress monitoring interface
 * </h1>
 * <p>
 * Since RepositoryMiner is <b>NOT</b> intended to be executed directly, having
 * the actual purpose of serving as an API for other applications it is
 * important to provide a general contract for listeners/observers.
 * <p>
 * Front-end applications can implement the listener to meet specific monitoring
 * needs, such as animating progress bars and/or activate routines in response
 * to progress changes.
 * <p>
 * Listeners can be injected into
 * {@link org.repositoryminer.mining.RepositoryMiner#setPostMiningListener(IPostMiningListener)}
 * in order to be activated.
 * <p>
 */
public interface IPostMiningListener {

	/**
	 * Notifies the initiation of post mining tasks processing
	 */
	public void initPostMining();

	/**
	 * Notifies that the progress of a post mining task has started
	 * 
	 * @param postMiningTaskName
	 *            the name of the task being started
	 */
	public void initPostMiningTaskProgress(String postMiningTaskName);

	/**
	 * Notifies the progress of post mining tasks processing
	 * 
	 * @param taskStepIndex
	 *            the current index of the task's step being processed
	 * @param taskStepName
	 *            the name of the task's step being processed
	 * @param numberOfTaskSteps
	 *            total number of task's steps to be processed
	 */
	public void postMiningTaskProgressChange(String taskStepName, int taskStepIndex, int numberOfTaskSteps);

}
