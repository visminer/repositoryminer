package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.Collection;
import java.util.List;

import br.edu.ufba.softvis.visminer.model.CommitDB;

public interface CommitDAO extends DAO<CommitDB, Integer> {

	public Collection<CommitDB> findByNames(List<String> names);
	
}
