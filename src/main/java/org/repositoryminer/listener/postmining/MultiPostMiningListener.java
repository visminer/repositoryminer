package org.repositoryminer.listener.postmining;

import java.util.ArrayList;
import java.util.List;

public class MultiPostMiningListener implements IPostMiningListener {

	private List<IPostMiningListener> listeners = new ArrayList<IPostMiningListener>();
	
	public List<IPostMiningListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<IPostMiningListener> listeners) {
		this.listeners = listeners;
	}

	public void addListener(IPostMiningListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void notifyTaskStart(String postMiningTaskName) {
		for (IPostMiningListener listener : listeners) {
			listener.notifyTaskStart(postMiningTaskName);
		}
	}

	@Override
	public void notifyTaskProgress(String taskStepName, int taskStepIndex, int numberOfTaskSteps) {
		for (IPostMiningListener listener : listeners) {
			listener.notifyTaskProgress(taskStepName, taskStepIndex, numberOfTaskSteps);
		}
	}

	@Override
	public void notifyTaskEnd(String postMiningTaskName) {
		for (IPostMiningListener listener : listeners) {
			listener.notifyTaskEnd(postMiningTaskName);
		}
	}

}