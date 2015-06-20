package br.edu.ufba.softvis.visminer.analyzer.local;

import java.util.Date;
import java.util.List;

import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.Committer;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.model.bean.Tree;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * @see GitRepository
 * Interface that define what is needed to support a repository.
 */

public interface IRepositorySystem {

	/* 
	 * DevNotes:
	 * All methods that return commits should return the committer inside of each commit.
	 * getCommitedFiles(String commitUid) should return File with FileState.
	 */
	
	/**
	 * @param repositoryPath
	 * Initializes the repository.
	 */
	public void open(String repositoryPath);

	/**
	 * @return Repository absolute path.
	 */
	public String getAbsolutePath();

	/**
	 * @return List of committers (contributors and not contributors).
	 */
	public List<Committer> getCommitters();

	
	/**
	 * @return List of trees (tags and branchs).
	 */
	public List<Tree> getTrees();


	/**
	 * @param treeName
	 * @return Date of last commit made.
	 */
	public Date getLastCommitDate(String treeName);

	
	/**
	 * @return List of all commits made.
	 */
	public List<Commit> getCommits();
	
	/**
	 * @param committerEmail
	 * @return List of commits made by a committer.
	 */
	public List<Commit> getCommitsByCommitter(String committerEmail);

	/**
	 * @param treeFullName
	 * @return List of commits made in a tree.
	 */
	public List<Commit> getCommitsByTree(String treeFullName);

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
	public List<File> getCommitedFiles(String commitUid);

	/**
	 * Closes the repository and frees memory and resources.
	 */
	public void close();
	
}