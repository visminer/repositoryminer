package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitPK;
/**
 * 
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * File_x_commit table DAO interface.
 * 
 */
public interface FileXCommitDAO extends DAO<FileXCommitDB, FileXCommitPK> {

	/**
	 * 
	 * @param commitId
	 * @return List of FileXCommit by commit.
	 */
	List<FileXCommitDB> findByCommit(int commitId);

}
