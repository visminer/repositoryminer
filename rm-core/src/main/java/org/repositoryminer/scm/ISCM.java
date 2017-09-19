package org.repositoryminer.scm;

import java.util.Collection;
import java.util.List;

import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.SCMType;

/**
 * Interface that define what is needed to support a repository.
 */
public interface ISCM {

	/**
	 * @return The supported version control system name.
	 */
	public SCMType getSCM();

	/**
	 * Prepare the repository to analysis.
	 * 
	 * @param path
	 *            the repository path.
	 */
	public void open(String path);

	/**
	 * @return List all references (e.g. tags and branches).
	 */
	public List<Reference> getReferences();

	/**
	 * @param skip
	 *            defines how many commits should be skipped before the fetch.
	 * @param maxCount
	 *            defines how many commits should be fetch.
	 * @param endPoint
	 *            defines the end point.
	 * @param commitsToSkip
	 *            defines which commits should not be processed and returned
	 * @return the commits until some point.
	 */
	public List<Commit> getCommits(int skip, int maxCount, String endPoint, Collection<String> commitsToSkip);

	/**
	 * @param endPoint
	 *            defines the end point.
	 * @return the commits names until some point.
	 */
	public List<String> getCommitsNames(String endPoint);

	/**
	 * Does a checkout to a given point.
	 * 
	 * @param hash
	 *            defines the checkout point.
	 */
	public void checkout(String hash);

	/**
	 * Terminates the analysis and free resources.
	 */
	public void close();

	/**
	 * Resets the current repository state.
	 */
	public void reset();

}