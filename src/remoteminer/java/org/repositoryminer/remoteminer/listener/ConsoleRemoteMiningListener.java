package org.repositoryminer.remoteminer.listener;

public class ConsoleRemoteMiningListener implements IRemoteMiningListener {

	@Override
	public void notifyServiceMiningStart(String name) {
	}

	@Override
	public void notifyServiceMiningEnd(String name) {
	}

	@Override
	public void notifyContributorsMiningStart(int totalContributors) {
	}

	@Override
	public void notifyContributorsMiningProgress(String name, int index, int totalContributors) {
	}

	@Override
	public void notifyContributorsMiningEnd(int totalContributors) {
	}

	@Override
	public void notifyMilestonesMiningStart(int totalMilestones) {
	}

	@Override
	public void notifyMilestonesMiningProgress(int number, String title, int index, int totalMilestones) {
	}

	@Override
	public void notifyMilestonesMiningEnd(int totalMilestones) {
	}

	@Override
	public void notifyIssuesMiningStart(int totalIssues) {
	}

	@Override
	public void notifyIssuesMiningProgress(int number, String title, int index, int totalIssues) {
	}

	@Override
	public void notifyIssuesMiningEnd(int totalIssues) {
	}

}
