package org.repositoryminer.scm;

import java.util.List;

import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.ReferenceType;

/**
 * Interface that define what is needed to support a repository.
 * <p/>
 * During the analysis the repository will have its HEAD changed many times and references will be reseted.
 * Leave the repository alone during the analysis process.
 * <p/>
 * If you are working in something is highly recommended to commit your work or put in the staged area.
 * Otherwise you will lose all your job.
 * <p/>
 * Do not save your unfinished work or experiment in other branch, this will influence the analysis.
 */

public interface SCM {
	
	/**
	 * @param repositoryPath
	 * @param binaryThreshold
	 * Initializes the repository analysis.
	 */
	public void open(String repositoryPath, int binaryThreshold);
	
	/**
	 * @return The absolute path of the repository.
	 * The \ are changed by /.
	 */
	public String getAbsolutePath();
	
	/**
	 * @param repositoryId
	 * @return List all references(e.g. tags and branches).
	 */
	public List<Reference> getReferences();

	/**
	 * @param skip The number of commits to jump before start the fetch.
	 * @param The max number of commits.
	 * @return A list with <max> commits beginning from <skip>.
	 * This method iterates through all commits.
	 */
	public List<Commit> getCommits(int skip, int maxCount);
	
	/**
	 * @param fullName
	 * @param type
	 * @return A list of commits names in the reference.
	 */
	public List<String> getReferenceCommits(String fullName, ReferenceType type);
	
	/**
	 * @param commit The commit hash.
	 * @param filePath
	 * @return The content of the file.
	 */
	public byte[] getData(String commit, String filePath);

	/**
	 * @param treeName
	 * Makes a checkout to a give reference.
	 */
	public void checkout(String hash);
	
	/**
	 * Terminates the analysis and free resources.
	 */
	public void close();
		
	/**
	 * Returns repository to the default state.
	 * The default state is the last commit of master branch.
	 */
	public void reset();
	
	
}