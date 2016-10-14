package org.repositoryminer.postprocessing;

import org.repositoryminer.listener.IPostMiningListener;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.model.Repository;

/**
 * <h1>Post mining task interface</h1>
 * <p>
 * A collection of routines that must be implemented by each post mining task.
 * All inherited tasks must provide:
 * <ul>
 * <li>a name for logging/tracing purposes
 * <li>a routine to be executed that performs the mining task
 * </ul>
 */
public interface IPostMiningTask {

	/**
	 * @return the task name. It must be provide an adequate name to provide a
	 *         full idea of the task's purpose.
	 */
	public String getTaskName();

	/**
	 * Executes the post-mining task
	 * 
	 * @param repositoryMiner
	 *            instance of miner
	 * @param repository
	 *            the repository being mined
	 * @param PostMiningListener's
	 *            instance of the listener to track the steps performed by the
	 *            mining
	 */
	public void execute(RepositoryMiner miner, Repository repository, IPostMiningListener listener);

}
