package org.repositoryminer.scm;

import java.util.List;

import org.repositoryminer.domain.Commit;
import org.repositoryminer.domain.Reference;
import org.repositoryminer.domain.SCMType;

/**
 * Interface that define what is needed to support a repository.
 */
public interface ISCM {

	/**
	 * @return The supported version control system type.
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
	 * @return all references (e.g. tags and branches).
	 */
	public List<Reference> getReferences();

	/**
	 * @return all commits.
	 */
	public List<Commit> getCommits();

	/**
	 * @param reference
	 *            the reference.
	 * @return all commits associated to the reference.
	 */
	public List<String> getCommitsNames(Reference reference);

	/**
	 * Does a checkout to a given point.
	 * 
	 * @param point
	 *            the checkout point.
	 */
	public void checkout(String point);

	/**
	 * Terminates the analysis and free resources.
	 */
	public void close();

}