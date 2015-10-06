package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;
import java.util.Map;

import br.edu.ufba.softvis.visminer.model.database.IssueDB;

/**
 * Issue table DAO interface.
 */
public interface IssueDAO extends DAO<IssueDB, Integer> {

	public Map<Integer, Integer> minimalFindByRepository(int repositoryId);
	public List<IssueDB> findByRepository(int repositoryId);
	
}
