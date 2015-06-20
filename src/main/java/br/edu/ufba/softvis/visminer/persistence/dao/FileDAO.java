package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.FileDB;

/**
 * 
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * File table DAO interface.
 * 
 */
public interface FileDAO extends DAO<FileDB, Integer> {

	/**
	 * 
	 * @param commitId
	 * @return List of files by commit.
	 */
	public List<FileDB> findCommitedFiles(int commitId);
	
}
