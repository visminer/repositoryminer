package br.edu.ufba.softvis.visminer.analyzer.scm;

import java.util.List;

import br.edu.ufba.softvis.visminer.constant.TreeType;
import br.edu.ufba.softvis.visminer.model.Commit;
import br.edu.ufba.softvis.visminer.model.File;
import br.edu.ufba.softvis.visminer.model.Tree;

/**
 * Interface that define what is needed to support a repository.
 */

public interface SCM {

	/* 
	 * All methods that return commits should return the committer inside of each commit.
	 * getCommitedFiles(String commitUid) should return File with FileState.
	 */
	
	/**
	 * @param repositoryPath
	 */
	public void open(String repositoryPath);
	
	/**
	 * @return Repository absolute path.
	 */
	public String getAbsolutePath();

	/**
	 * @return List of trees (tags and branchs).
	 */
	public List<Tree> getTrees();

	/**
	 * @return List of all commits made.
	 */
	public List<Commit> getCommits();
	
	/**
	 * @param treeFullName
	 * @return List of commits made in a tree.
	 */
	public List<Commit> getCommitsByTree(String treeFullName, TreeType type);

	/**
	 * @param commitUid
	 * @param filePath
	 * @return File data as bytes.
	 */
	public String getSource(String commitUid, String filePath);

	/**
	 * @param commitUid
	 * @return List of modified files (with modifications) in a commit.
	 */
	public List<File> getCommitedFiles(Commit commit);

	/**
	 * @param commitUid
	 * @return List of files uids in certain snapshot.
	 */
	public List<String> getRepositoryFiles(String hash);

	/**
	 * @param treeName
	 * Makes a checkout to a give tree.
	 */
	public void checkout(String hash);
	
	/**
	 * Closes the repository and frees memory and resources.
	 */
	public void close();
		
	/**
	 * Returns repository to master branch
	 */
	public void reset();
	
}