package br.edu.ufba.softvis.visminer.analyzer.local;

import java.util.Date;
import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;


/**
 * @version 0.9
 * @see GitRepository
 * Interface that define what is needed to support a repository.
 */

public interface IRepositorySystem {

	/* 
	 * All methods that return commits should return the committer inside of each commit.
	 * getCommitedFiles(String commitUid) should return File with FileState.
	 */
	

	/**
	 * @return Repository absolute path.
	 */
	public String getAbsolutePath();

	/**
	 * @return List of trees (tags and branchs).
	 */
	public List<TreeDB> getTrees();


	/**
	 * @param treeName
	 * @return Date of last commit made.
	 */
	public Date getLastCommitDate(String treeName);

	
	/**
	 * @return List of all commits made.
	 */
	public List<CommitDB> getCommits();
	
	/**
	 * @param treeFullName
	 * @return List of commits made in a tree.
	 */
	public List<CommitDB> getCommitsByTree(String treeFullName);

	/**
	 * @param commitUid
	 * @param filePath
	 * @return File data as bytes.
	 */
	public byte[] getData(String commitUid, String filePath);

	/**
	 * @param commitUid
	 * @return List of modified files (with modifications) in a commit.
	 */
	public List<FileDB> getCommitedFiles(CommitDB commitDb);

	/**
	 * @param commitUid
	 * @return List of files path in certain snapshot.
	 */
	public List<String> getSnapshotFilesNames(String commitUid);
	
	/**
	 * Closes the repository and frees memory and resources.
	 */
	public void close();
	
}