package br.edu.ufba.softvis.visminer.persistence.dao;

import br.edu.ufba.softvis.visminer.model.database.MetricDB;

public interface MetricDAO extends DAO<MetricDB, Integer>{

	public MetricDB findByAcronym(String acronym);
	
}
