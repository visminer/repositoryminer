package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.FileDB;

/**
 * File table DAO interface.
 */
public interface FileDAO extends DAO<FileDB, Integer> {

	/**
	 * @param commitId
	 * @return List of files by commit.
	 */
	public List<FileDB> findCommitedFiles(int commitId);
	
	/**
	 * @param uids
	 * @return List of files by their uids
	 */
	public List<FileDB> getFilesByUids(List<String> uids);
	
}
