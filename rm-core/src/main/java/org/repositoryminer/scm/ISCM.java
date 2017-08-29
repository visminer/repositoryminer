package org.repositoryminer.scm;

import java.util.List;

import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;

/**
 * Interface that define what is needed to support a repository.
 */
public interface ISCM {

	/**
	 * @return The supported version control system name.
	 */
	public String getSCM();

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
	 * @param hash
	 *            defines the end point.
	 * @return the commits until some point.
	 */
	public List<Commit> getCommits(int skip, int maxCount, String hash);

	/**
	 * @param hash
	 *            defines the end point.
	 * @return the commits names until some point.
	 */
	public List<String> getCommitsNames(String hash);

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