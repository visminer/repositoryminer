package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.Map;

import br.edu.ufba.softvis.visminer.model.database.IssueDB;

/**
 * Issue table DAO interface.
 */
public interface IssueDAO extends DAO<IssueDB, Integer> {

	/**
	 * @param repositoryId
	 * @return A map that contains the number(key) and id(value);
	 */
	public Map<Integer, Integer> minimalFindByRepository(int repositoryId);
	
}
