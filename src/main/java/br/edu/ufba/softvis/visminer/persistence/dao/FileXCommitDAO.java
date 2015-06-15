package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitPK;

public interface FileXCommitDAO extends DAO<FileXCommitDB, FileXCommitPK> {

	List<FileXCommitDB> findByCommit(int commitId);

}
