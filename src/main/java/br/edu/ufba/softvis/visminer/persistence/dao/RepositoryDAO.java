package br.edu.ufba.softvis.visminer.persistence.dao;

import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;

public interface RepositoryDAO extends DAO<RepositoryDB, Integer> {

	public RepositoryDB getByUid(String uid);
	
}
