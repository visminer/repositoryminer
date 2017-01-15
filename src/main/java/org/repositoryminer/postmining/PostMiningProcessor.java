package org.repositoryminer.postmining;

import java.util.List;

import org.repositoryminer.listener.postmining.IPostMiningListener;
import org.repositoryminer.listener.postmining.NullPostMiningListener;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.model.Repository;

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
 * {@link org.repositoryminer.postmining.IPostMiningTask}) that must be
 * executed.
 */
public class PostMiningProcessor {

	private List<IPostMiningTask> tasks;
	private IPostMiningListener listener = new NullPostMiningListener();

	public PostMiningProcessor(List<IPostMiningTask> tasks) {
		this.tasks = tasks;
	}
	
	public List<IPostMiningTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<IPostMiningTask> tasks) {
		this.tasks = tasks;
	}

	public IPostMiningListener getListener() {
		return listener;
	}

	public void setListener(IPostMiningListener listener) {
		this.listener = listener;
	}

	/**
	 * Executes each post-mining task injected into the repository miner class
	 * 
	 * @param repository
	 *            the repository being mined
	 * @param repositoryMiner
	 *            the entry point to the mining parameters
	 * @return instance of repository after post processing
	 */
	public Repository executeTasks(Repository repository, RepositoryMiner repositoryMiner) {
		for (IPostMiningTask task : tasks) {
			listener.notifyTaskStart(task.getTaskName());
			task.execute(repositoryMiner, repository, listener);
			listener.notifyTaskEnd(task.getTaskName());
		}

		return repository;
	}

}