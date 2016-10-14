package org.repositoryminer.listener;

/**
 * <h1>IHostServiceListener is dedicated to notify mining steps performed by
 * {@link org.repositoryminer.mining.HostingServiceMiner}}</h1>
 * <p>
 * Since HostingServiceMiner is <b>NOT</b> intended to be executed directly,
 * having the actual purpose of serving as an API for other applications it is
 * important to provide a general contract for listeners/observers.
 * <p>
 * Front-end applications can implement the listener to meet specific monitoring
 * needs, such as animating progress bars and/or activate routines in response
 * to progress changes.
 * <p>
 * Listeners can be injected into
 * {@link org.repositoryminer.mining.HostingServiceMiner#setListener(IHostServiceListener)}
 * in order to be activated.
 * <p>
 */
public interface IHostServiceListener {

	/**
	 * Notifies the initiation of mining
	 */
	public void initContributorsProcessing();

	/**
	 * Notifies the progress of contributors progress
	 * 
	 * @param contributorIndex
	 *            current index of the contributor being processed
	 * @param numberOfContributors
	 *            the total amount of contributors
	 */
	public void contributorsProgressChange(int contributorIndex, int numberOfContributors);

	/**
	 * Notifies that the current step is that of connecting milestones to its
	 * related issues
	 */
	public void initMilestonesIssuesConnection();

	/**
	 * Notifies that milestones are going to be persisted
	 */
	public void initMilestonesProcessing();

	/**
	 * Notifies the progress of milestones processing
	 * 
	 * @param milestoneIndex
	 *            index of the current milestone being processed
	 * @param numberOfMilestones
	 *            total amount of milestone to be processed
	 */
	public void milestonesProgressChange(int milestoneIndex, int numberOfMilestones);

	/**
	 * Notifies the initiation of issues processing
	 */
	public void initIssuesProcessing();

	/**
	 * Notifies the progress of issues processing
	 * 
	 * @param issuesIndex
	 *            current index of the issue being processed
	 * @param numberOfIssues
	 *            the total amount of issues to be processed
	 */
	public void issuesProgressChange(int issuesIndex, int numberOfIssues);

}
