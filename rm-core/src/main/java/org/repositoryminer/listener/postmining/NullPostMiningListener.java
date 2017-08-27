package org.repositoryminer.listener.postmining;

public class NullPostMiningListener implements IPostMiningListener {

	@Override
	public void notifyTaskStart(String postMiningTaskName) {
	}

	@Override
	public void notifyTaskProgress(String taskStepName, int taskStepIndex, int numberOfTaskSteps) {
	}

	@Override
	public void notifyTaskEnd(String postMiningTaskName) {
	}

}