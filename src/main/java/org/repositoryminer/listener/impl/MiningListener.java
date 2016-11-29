package org.repositoryminer.listener.impl;

import org.repositoryminer.listener.IMiningListener;

/**
 * <h1>Default implementation of IMiningListener</h1>
 * <p>
 * It provides a default instance of
 * {@link org.repositoryminer.listener.IMiningListener} to output mining steps
 * to console. This default instance can be replaced by injecting a customized
 * one into
 * {@link org.repositoryminer.mining.RepositoryMiner#setMiningListener(IMiningListener)}
 */
public class MiningListener implements IMiningListener {

	private String repositoryName = "";

	public static IMiningListener getDefault() {
		return new MiningListener();
	}

	@Override
	public void initMining(String name) {
		this.repositoryName = name;

		System.out.println("Mining of '" + repositoryName + "' initiated");
	}

	@Override
	public void initCommitsMining() {
		System.out.println("Mining commits from " + repositoryName);
	}

	@Override
	public void commitsProgressChange(String reference, String commit, int commitIndex, int numberOfCommits) {
		System.out.println(
				"Processing " + commitIndex + " of " + numberOfCommits + " commits [ Reference = " + reference + "]");
	}

	@Override
	public void workingDirectoryProgressChange(String reference, String commit, int commitIndex, int numberOfCommits) {
		System.out.println(
				"Processing " + commitIndex + " of " + numberOfCommits + " commits [ Reference = " + reference + "]");
	}

	@Override
	public void tagsProgressChange(String tag, int tagIndex, int numberOfTags) {
		System.out.println("Processing " + tagIndex + "[" + tag + "] of " + numberOfTags + " tags");
	}

}
