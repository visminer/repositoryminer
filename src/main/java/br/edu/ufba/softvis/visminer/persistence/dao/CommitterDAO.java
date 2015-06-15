package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.CommitterDB;

public interface CommitterDAO extends DAO<CommitterDB, Integer>{

	public CommitterDB findByEmail(String email);

	public List<CommitterDB> findByRepository(int repositoryId);
	
}
