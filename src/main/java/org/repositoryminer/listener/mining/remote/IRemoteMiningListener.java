package org.repositoryminer.listener.mining.remote;

public interface IRemoteMiningListener {

	void notifyServiceMiningStart(String name);

	void notifyServiceMiningEnd(String name);

	void notifyContributorsMiningStart(int totalContributors);

	void notifyContributorsMiningProgress(String name, int index, int totalContributors);

	void notifyContributorsMiningEnd(int totalContributors);

	void notifyMilestonesMiningStart(int totalMilestones);

	void notifyMilestonesMiningProgress(int number, String title, int index, int totalMilestones);
	
	void notifyMilestonesMiningEnd(int totalMilestones);

	void notifyIssuesMiningStart(int totalIssues);

	void notifyIssuesMiningProgress(int number, String title, int index, int totalIssues);

	void notifyIssuesMiningEnd(int totalIssues);
	
}