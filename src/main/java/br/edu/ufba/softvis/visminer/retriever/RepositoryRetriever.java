package br.edu.ufba.softvis.visminer.retriever;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.Repository;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.relational.RepositoryDAOImpl;

public class RepositoryRetriever extends Retriever {

	public List<Repository> retrieveAll() {
		
		RepositoryDAO dao = new RepositoryDAOImpl();
		super.createEntityManager();
		super.shareEntityManager(dao);
		
		List<RepositoryDB> reposDB = dao.findAll();
		super.closeEntityManager();
		return RepositoryDB.toBusiness(reposDB);
		
	}

	public Repository retrieveById(int id) {
		
		RepositoryDAO dao = new RepositoryDAOImpl();
		super.createEntityManager();
		super.shareEntityManager(dao);
		
		RepositoryDB repoDB = dao.find(id);
		super.closeEntityManager();
		return repoDB.toBusiness();
		
	}

	public Repository retrieveByPath(String path) {
		
		RepositoryDAO dao = new RepositoryDAOImpl();
		super.createEntityManager();
		super.shareEntityManager(dao);
		
		RepositoryDB repoDB = dao.findByPath(path);
		super.closeEntityManager();
		return repoDB.toBusiness();
				
	}
	
	public boolean checkIfExists(String repositoryPath){

		RepositoryDAO dao = new RepositoryDAOImpl();
		super.createEntityManager();
		super.shareEntityManager(dao);
		
		boolean result = dao.hasRepository(repositoryPath);
		super.closeEntityManager();
		return result;

	}

}