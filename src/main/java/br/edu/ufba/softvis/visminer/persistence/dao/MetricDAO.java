package br.edu.ufba.softvis.visminer.persistence.dao;

import br.edu.ufba.softvis.visminer.model.MetricDB;

public interface MetricDAO extends DAO<MetricDB, Integer>{

	public MetricDB findByName(String name);
	
}
