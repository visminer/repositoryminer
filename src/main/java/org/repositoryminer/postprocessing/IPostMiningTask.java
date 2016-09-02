package org.repositoryminer.postprocessing;

import org.repositoryminer.listener.IProgressListener;
import org.repositoryminer.model.Repository;

/**
 * <h1>Post mining task interface</h1>
 * <p>
 * A collection of routines that must be implemented by each inherited concrete
 * class
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
	 * @param repository
	 *            the repository being mined
	 * @param progressListener
	 *            instance of the listener to track the steps peformed by the
	 *            mining
	 */
	public void execute(Repository repository, IProgressListener progressListener);

}
