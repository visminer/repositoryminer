package org.repositoryminer.mining;

import java.util.List;

import org.repositoryminer.listener.IProgressListener;
import org.repositoryminer.model.Repository;
import org.repositoryminer.postprocessing.IPostMiningTask;

/**
 * <h1>Processor of post mining tasks</h1>
 * <p>
 * PostMiningProcessor is the entry point to the execution of a collection of
 * post mining task. A post mining task represents a collection of routines to
 * be executed after the processing of abstract syntactic trees mined from
 * software's source-code.
 * <p>
 * It is important to provide an instance of
 * {@link org.repositoryminer.model.Repository} to indicate the repository which
 * is up to be post-mined.
 * <p>
 * Reference to {@link RepositoryMiner} is necessary to provide access to the
 * parameters injected by caller routines that are important to the post-mining.
 * That is the case, for instance, with the injected list of tasks (
 * {@link org.repositoryminer.postprocessing.IPostMiningTask}) that must be
 * executed.
 */
public class PostMiningProcessor {

	public PostMiningProcessor() {
	}

	/**
	 * Executes each post-mining task injected into the repository miner class
	 * 
	 * @param repository
	 *            the repository being mined
	 * @param repositoryMiner
	 *            the entry point to the mining parameters
	 */
	public void executeTasks(Repository repository, RepositoryMiner repositoryMiner) {
		if (repositoryMiner.hasPostMiningTasks()) {
			List<IPostMiningTask> tasks = repositoryMiner.getPostMiningTasks();
			IProgressListener progressListener = repositoryMiner.getProgressListener();
			for (IPostMiningTask task : tasks) {
				if (progressListener != null) {
					progressListener.initPostMiningTaskProgress(task.getTaskName());
				}
				task.execute(repository, progressListener);
			}
		}
	}

}
