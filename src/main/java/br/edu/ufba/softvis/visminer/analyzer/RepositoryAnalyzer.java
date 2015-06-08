package br.edu.ufba.softvis.visminer.analyzer;

import java.util.List;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IRepositorySystem;
import br.edu.ufba.softvis.visminer.analyzer.local.SupportedRepository;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.impl.RepositoryDAOImpl;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

public class RepositoryAnalyzer implements IAnalyzer<Void>{

	@Override
	public Void persist(Object... objects) {
		
		Repository repoBusi = (Repository) objects[0];
		
		IRepositorySystem repoSys = SupportedRepository.getRepository(repoBusi.getType());
		repoSys.open(repoBusi.getPath());
		
		EntityManager entityManager = Database.getInstance().getEntityManager();
		RepositoryDAOImpl repositoryDao = new RepositoryDAOImpl();
		repositoryDao.setEntityManager(entityManager);
		
		String path = repoSys.getAbsolutePath();
		String uid = StringUtils.sha1(path);
		
		if(repositoryDao.getByUid(uid) != null){
			throw new KeyAlreadyExistsException("Repository already exists in database");
		}
		
		RepositoryDB repositoryDb = new RepositoryDB(repoBusi);
		repositoryDb.setPath(path);
		repositoryDb.setUid(uid);
		
		repositoryDao.save(repositoryDb);
		List<CommitterDB> committersDb = new CommitterAnalyzer().persist(repositoryDb, repoSys, entityManager);
		List<CommitDB> commitsDB = new CommitAnalyzer().persist(committersDb, repoSys, entityManager);
		
		committersDb = null;
		
		new TreeAnalyzer().persist(commitsDB, repositoryDb, repoSys, entityManager);
		new FileAnalyzer().persist(commitsDB, repoSys, entityManager);

		entityManager.close();
		repoSys.close();
		return null;
		
	}

	@Override
	public Void update(Object... objects) {
		return null;
	}

	
}
