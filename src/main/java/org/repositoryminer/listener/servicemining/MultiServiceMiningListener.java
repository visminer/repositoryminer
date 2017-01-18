package org.repositoryminer.listener.servicemining;

import java.util.ArrayList;
import java.util.List;

public class MultiServiceMiningListener implements IServiceMiningListener {

	private List<IServiceMiningListener> listeners = new ArrayList<IServiceMiningListener>();
	
	public List<IServiceMiningListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<IServiceMiningListener> listeners) {
		this.listeners = listeners;
	}

	public void addListener(IServiceMiningListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void notifyServiceMiningStart(String name) {
		for (IServiceMiningListener listener : listeners) {
			listener.notifyServiceMiningStart(name);
		}
	}

	@Override
	public void notifyServiceMiningEnd(String name) {
		for (IServiceMiningListener listener : listeners) {
			listener.notifyServiceMiningEnd(name);
		}
	}

	@Override
	public void notifyContributorsMiningStart(int totalContributors) {
		for (IServiceMiningListener listener : listeners) {
			listener.notifyContributorsMiningStart(totalContributors);
		}
	}

	@Override
	public void notifyContributorsMiningProgress(String name, int index, int totalContributors) {
		for (IServiceMiningListener listener : listeners) {
			listener.notifyContributorsMiningProgress(name, index, totalContributors);
		}
	}

	@Override
	public void notifyContributorsMiningEnd(int totalContributors) {
		for (IServiceMiningListener listener : listeners) {
			listener.notifyContributorsMiningEnd(totalContributors);
		}
	}

	@Override
	public void notifyMilestonesMiningStart(int totalMilestones) {
		for (IServiceMiningListener listener : listeners) {
			listener.notifyMilestonesMiningStart(totalMilestones);
		}
	}

	@Override
	public void notifyMilestonesMiningProgress(int number, String title, int index, int totalMilestones) {
		for (IServiceMiningListener listener : listeners) {
			listener.notifyMilestonesMiningProgress(number, title, index, totalMilestones);
		}
	}

	@Override
	public void notifyMilestonesMiningEnd(int totalMilestones) {
		for (IServiceMiningListener listener : listeners) {
			listener.notifyMilestonesMiningEnd(totalMilestones);
		}
	}

	@Override
	public void notifyIssuesMiningStart(int totalIssues) {
		for (IServiceMiningListener listener : listeners) {
			listener.notifyIssuesMiningStart(totalIssues);
		}
	}

	@Override
	public void notifyIssuesMiningProgress(int number, String title, int index, int totalIssues) {
		for (IServiceMiningListener listener : listeners) {
			listener.notifyIssuesMiningProgress(number, title, index, totalIssues);
		}
	}

	@Override
	public void notifyIssuesMiningEnd(int totalIssues) {
		for (IServiceMiningListener listener : listeners) {
			listener.notifyIssuesMiningEnd(totalIssues);
		}
	}

}