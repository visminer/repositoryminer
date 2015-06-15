package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.FileDB;

public interface FileDAO extends DAO<FileDB, Integer> {

	public Integer findIdByCode(String code);

	public List<FileDB> findCommitedFiles(int id);
	
}
