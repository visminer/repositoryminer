package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.Repository;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;

public class RepositoryDAOImpl implements RepositoryDAO {

	@Override
	public void save(Repository repo) {
		Database.getInstance().insert("repositories", repo.toDocument());
	}

	@Override
	public Repository find(String id) {
		return null;
	}

	@Override
	public List<Repository> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRepository(String repositoryPath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Repository findByPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

}
