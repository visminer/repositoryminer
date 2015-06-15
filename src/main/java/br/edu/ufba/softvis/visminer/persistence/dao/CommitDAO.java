package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.CommitDB;

public interface CommitDAO extends DAO<CommitDB, Integer> {

	public List<CommitDB> findByTree(int treeId);

	
}
