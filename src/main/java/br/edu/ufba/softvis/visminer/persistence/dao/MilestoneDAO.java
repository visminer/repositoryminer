package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.Map;

import br.edu.ufba.softvis.visminer.model.database.MilestoneDB;

/**
 * Milestone table DAO interface.
 */
public interface MilestoneDAO extends DAO<MilestoneDB, Integer>{

	public Map<Integer, Integer> minimalFindByRepository(int repositoryId);
	
}
