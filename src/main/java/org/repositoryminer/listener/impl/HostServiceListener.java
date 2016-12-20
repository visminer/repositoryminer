package org.repositoryminer.listener.impl;

import org.repositoryminer.listener.IHostServiceListener;

/**
 * <h1>Default implementation of IHostServiceListener</h1>
 * <p>
 * It provides a default instance of
 * {@link org.repositoryminer.listener.IHostServiceListener} to output mining steps
 * to console. This default instance can be replaced by injecting a customized
 * one into
 * {@link org.repositoryminer.mining.hosting.HostingServiceMiner#setListener(IHostServiceListener)}
 */
public class HostServiceListener implements IHostServiceListener {

	public static IHostServiceListener getDefault() {
		return new HostServiceListener();
	}

	@Override
	public void initContributorsProcessing() {
		System.out.println("Retrieving contributors");
	}

	@Override
	public void contributorsProgressChange(int contributorIndex, int numberOfContributors) {
		System.out.println(
				"Processing contributor " + contributorIndex + " of " + numberOfContributors + " contributors");
	}

	@Override
	public void initMilestonesIssuesConnection() {
		System.out.println("Connecting issues to milestones");
	}

	@Override
	public void initMilestonesProcessing() {
		System.out.println("Retrieving milestones");
	}

	@Override
	public void milestonesProgressChange(int milestoneIndex, int numberOfMilestones) {
		System.out.println("Processing milestone " + milestoneIndex + " of " + numberOfMilestones + " milestones");
	}

	@Override
	public void initIssuesProcessing() {
		System.out.println("Retrieving issues");
	}

	@Override
	public void issuesProgressChange(int issuesIndex, int numberOfIssues) {
		System.out.println("Processing issue " + issuesIndex + " of " + numberOfIssues + " issues");
	}

}
