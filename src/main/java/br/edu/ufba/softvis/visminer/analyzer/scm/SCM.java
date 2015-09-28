package br.edu.ufba.softvis.visminer.analyzer.scm;

import java.util.List;
import br.edu.ufba.softvis.visminer.constant.TreeType;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;

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
	public List<TreeDB> getTrees();

	/**
	 * @return List of all commits made.
	 */
	public List<CommitDB> getCommits();
	
	/**
	 * @param treeFullName
	 * @return List of commits made in a tree.
	 */
	public List<CommitDB> getCommitsByTree(String treeFullName, TreeType type);

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
	public List<FileDB> getCommitedFiles(CommitDB commitDB);

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
	 * Removes the working branch created by visminer to make the mining.
	 */
	public void deleteVMBranch();
	
	/**
	 * Resets repository HEAD to master
	 */
	public void reset();
	
	/**
	 * Closes the repository and frees memory and resources.
	 */
	public void close();
	
}