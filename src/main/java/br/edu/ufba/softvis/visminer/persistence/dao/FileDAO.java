package br.edu.ufba.softvis.visminer.persistence.dao;

import br.edu.ufba.softvis.visminer.model.database.FileDB;

public interface FileDAO extends DAO<FileDB, Integer> {

	public Integer findIdByCode(String code);
	
}
