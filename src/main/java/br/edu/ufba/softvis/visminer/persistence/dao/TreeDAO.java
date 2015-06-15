package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.TreeDB;

public interface TreeDAO extends DAO<TreeDB, Integer>{

	public List<TreeDB> findByRepository(int id);

}
