package br.edu.ufba.softvis.visminer.persistence.dao;

import br.edu.ufba.softvis.visminer.model.CommitterDB;

public interface CommitterDAO extends DAO<CommitterDB, Integer>{

	public CommitterDB findByEmail(String email);
	
}
