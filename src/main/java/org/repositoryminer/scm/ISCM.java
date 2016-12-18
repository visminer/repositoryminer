package org.repositoryminer.scm;

import java.util.Collection;
import java.util.List;

import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;

/**
 * Interface that define what is needed to support a repository.
 * <p/>
 * During the analysis the repository will have its HEAD changed many times and
 * references will be reseted. Leave the repository alone during the analysis
 * process.
 * <p/>
 * If you are working in something is highly recommended to commit your work or
 * put in the staged area. Otherwise you will lose all your job.
 * <p/>
 * Do not save your unfinished work or experiment in other branch, this will
 * influence the analysis.
 */

public interface ISCM {

	/**
	 * @param repositoryPath
	 *            Initializes the repository analysis.
	 */
	public void open(String repositoryPath);

	/**
	 * @param repositoryId
	 * @return List all references(e.g. tags and branches).
	 */
	public List<Reference> getReferences();

	/**
	 * @param skip
	 *            defines how many commits should be skipped before the fetch
	 * @param maxCount
	 *            defines how many commits should be fetch
	 * @param reference
	 *            the reference
	 * @param commits
	 *            collection of commits names
	 * @param despise
	 *            defines True if the list of commits in the parameters should
	 *            be skipped or False if only the commits in the list should be
	 *            returned.
	 * @return A list with a set of commits in a reference
	 */
	public List<Commit> getCommits(int skip, int maxCount, Reference reference, Collection<String> commits,
			boolean despise);

	/**
	 * @param fullName
	 * @param type
	 * @return A list of commits names in the reference.
	 */
	public List<String> getReferenceCommits(String fullName, ReferenceType type);

	/**
	 * @param treeName
	 *            Makes a checkout to a given point or reference.
	 */
	public void checkout(String hash);

	/**
	 * Terminates the analysis and free resources.
	 */
	public void close();

	/**
	 * Returns repository to the default state. The default state is the last
	 * commit of master branch.
	 */
	public void reset();

}