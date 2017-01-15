package org.repositoryminer.listener.postmining;

/**
 * <h1>Default implementation of IPostMiningListener</h1>
 * <p>
 * It provides a default instance of
 * {@link org.repositoryminer.listener.postmining.IPostMiningListener} to output mining steps
 * to console. This default instance can be replaced by injecting a customized
 * one into
 * {@link org.repositoryminer.mining.RepositoryMiner#setPostMiningListener(IPostMiningListener)}
 */
public class ConsolePostMiningListener implements IPostMiningListener {

	@Override
	public void notifyTaskStart(String postMiningTaskName) {
		System.out.println("Starting task " + postMiningTaskName);
	}

	@Override
	public void notifyTaskProgress(String taskStepName, int taskStepIndex, int numberOfTaskSteps) {
		System.out.println("Processing " + taskStepIndex + " of " + numberOfTaskSteps + " steps in " + taskStepName);
	}

	@Override
	public void notifyTaskEnd(String postMiningTaskName) {
		System.out.println("Finishing task " + postMiningTaskName);
	}

}