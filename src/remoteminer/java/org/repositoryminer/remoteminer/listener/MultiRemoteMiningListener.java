package org.repositoryminer.remoteminer.listener;

import java.util.ArrayList;
import java.util.List;

public class MultiRemoteMiningListener implements IRemoteMiningListener {

	private List<IRemoteMiningListener> listeners = new ArrayList<IRemoteMiningListener>();
	
	public List<IRemoteMiningListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<IRemoteMiningListener> listeners) {
		this.listeners = listeners;
	}

	public void addListener(IRemoteMiningListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void notifyServiceMiningStart(String name) {
		for (IRemoteMiningListener listener : listeners) {
			listener.notifyServiceMiningStart(name);
		}
	}

	@Override
	public void notifyServiceMiningEnd(String name) {
		for (IRemoteMiningListener listener : listeners) {
			listener.notifyServiceMiningEnd(name);
		}
	}

	@Override
	public void notifyContributorsMiningStart(int totalContributors) {
		for (IRemoteMiningListener listener : listeners) {
			listener.notifyContributorsMiningStart(totalContributors);
		}
	}

	@Override
	public void notifyContributorsMiningProgress(String name, int index, int totalContributors) {
		for (IRemoteMiningListener listener : listeners) {
			listener.notifyContributorsMiningProgress(name, index, totalContributors);
		}
	}

	@Override
	public void notifyContributorsMiningEnd(int totalContributors) {
		for (IRemoteMiningListener listener : listeners) {
			listener.notifyContributorsMiningEnd(totalContributors);
		}
	}

	@Override
	public void notifyMilestonesMiningStart(int totalMilestones) {
		for (IRemoteMiningListener listener : listeners) {
			listener.notifyMilestonesMiningStart(totalMilestones);
		}
	}

	@Override
	public void notifyMilestonesMiningProgress(int number, String title, int index, int totalMilestones) {
		for (IRemoteMiningListener listener : listeners) {
			listener.notifyMilestonesMiningProgress(number, title, index, totalMilestones);
		}
	}

	@Override
	public void notifyMilestonesMiningEnd(int totalMilestones) {
		for (IRemoteMiningListener listener : listeners) {
			listener.notifyMilestonesMiningEnd(totalMilestones);
		}
	}

	@Override
	public void notifyIssuesMiningStart(int totalIssues) {
		for (IRemoteMiningListener listener : listeners) {
			listener.notifyIssuesMiningStart(totalIssues);
		}
	}

	@Override
	public void notifyIssuesMiningProgress(int number, String title, int index, int totalIssues) {
		for (IRemoteMiningListener listener : listeners) {
			listener.notifyIssuesMiningProgress(number, title, index, totalIssues);
		}
	}

	@Override
	public void notifyIssuesMiningEnd(int totalIssues) {
		for (IRemoteMiningListener listener : listeners) {
			listener.notifyIssuesMiningEnd(totalIssues);
		}
	}

}