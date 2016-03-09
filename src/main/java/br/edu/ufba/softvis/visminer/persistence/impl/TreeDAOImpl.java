package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.Tree;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.dao.TreeDAO;

public class TreeDAOImpl implements TreeDAO {

	@Override
	public void save(Tree tree) {
		Database.getInstance().insert("trees", tree.toDocument());
	}

	@Override
	public Tree find(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tree> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tree> findByRepository(String repositoryPath) {
		// TODO Auto-generated method stub
		return null;
	}
}
