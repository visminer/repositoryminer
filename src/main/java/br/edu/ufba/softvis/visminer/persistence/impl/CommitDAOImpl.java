package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.Commit;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;

public class CommitDAOImpl implements CommitDAO {

	@Override
	public void save(Commit commit) {
		Database.getInstance().insert("commits", commit.toDocument());
	}

	@Override
	public Commit find(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Commit> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Commit> findByTree(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Commit> findByRepository(String uid) {
		// TODO Auto-generated method stub
		return null;
	}
}
