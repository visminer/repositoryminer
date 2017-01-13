package org.repositoryminer.effort.listener;

import org.repositoryminer.postprocessing.IPostMiningListener;

/**
 * <h1>Default implementation of IPostMiningListener</h1>
 * <p>
 * It provides a default instance of
 * {@link org.repositoryminer.postprocessing.IPostMiningListener} to output mining steps
 * to console. This default instance can be replaced by injecting a customized
 * one into
 * {@link org.repositoryminer.mining.RepositoryMiner#setPostMiningListener(IPostMiningListener)}
 */
public class PostMiningListener implements IPostMiningListener {

	public static IPostMiningListener getDefault() {
		return new PostMiningListener();
	}
	
	@Override
	public void initPostMining() {
		System.out.println("Initiating execution of post-mining tasks");
	}

	@Override
	public void initPostMiningTaskProgress(String postMiningTaskName) {
		System.out.println("Executing " + postMiningTaskName + " now");
	}

	@Override
	public void postMiningTaskProgressChange(String taskStepName, int taskStepIndex, int numberOfTaskSteps) {
		System.out.println("Processing " + taskStepIndex + " of " + numberOfTaskSteps + " steps in " + taskStepName);
	}

}