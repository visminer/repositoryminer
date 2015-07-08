package br.edu.ufba.softvis.visminer.analyzer;

import java.util.List;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IRepositorySystem;
import br.edu.ufba.softvis.visminer.analyzer.local.SupportedRepository;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.RepositoryDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitDAOImpl;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * @version 0.9
 * @see CommitAnalyzer
 * @see CommitterAnalyzer
 * @see FileAnalyzer
 * @see IssueAnalyzer
 * @see MilestoneAnalyzer
 * @see TreeAnalyzer
 * @see IAnalyzer
 * 
 * Defines how to save or to increment informations about the repository in database.
 */
public class RepositoryAnalyzer implements IAnalyzer<Void>{

	/*
	 * This class is responsible for join all the analyzers and make the
	 * repository analyzis.
	*/
	
	@SuppressWarnings("unchecked")
	@Override
	public Void persist(Object... objects) {
		
		Repository repoBusi = (Repository) objects[0];
		List<MetricUid> metrics = (List<MetricUid>) objects[1];
		
		IRepositorySystem repoSys = SupportedRepository.getRepository(repoBusi.getPath(), repoBusi.getType());
		
		EntityManager entityManager = Database.getInstance().getEntityManager();
		
		RepositoryDAO repositoryDao = new RepositoryDAOImpl();
		repositoryDao.setEntityManager(entityManager);
		
		SoftwareUnitDAO softwareUnitDao = new SoftwareUnitDAOImpl();
		softwareUnitDao.setEntityManager(entityManager);
		
		String path = repoSys.getAbsolutePath();
		String uid = StringUtils.sha1(path);
		
		if(repositoryDao.findByUid(uid) != null){
			throw new KeyAlreadyExistsException("Repository already exists in database");
		}
		
		RepositoryDB repositoryDb = new RepositoryDB(0, repoBusi.getDescription(), repoBusi.getName(),
				path, repoBusi.getRemoteUrl(), repoBusi.getType(), repoBusi.getServiceType(), uid);
		
		repositoryDao.save(repositoryDb);
		List<CommitterDB> committersDb = new CommitterAnalyzer().persist(repositoryDb, repoSys, entityManager);
		List<CommitDB> commitsDB = new CommitAnalyzer().persist(committersDb, repoSys, entityManager);
		
		new TreeAnalyzer().persist(commitsDB, repositoryDb, repoSys, entityManager);
		new FileAnalyzer().persist(commitsDB, repoSys, entityManager);

		// set null in the things that will have no more use for save memory
		committersDb = null;
		commitsDB = null;
		
		if(metrics != null && metrics.size() > 0){
			MetricCalculator.calculate(metrics, repoSys, repositoryDb, entityManager);
		}
		
		SoftwareUnitDB softUnitDb = new SoftwareUnitDB(0, repositoryDb.getName(), SoftwareUnitType.PROJECT, repositoryDb.getUid());
		softwareUnitDao.save(softUnitDb);
		
		entityManager.close();
		repoSys.close();
		return null;
		
	}

	@Override
	public Void increment(Object... objects) {
		return null;
	}

	/**
	 * @param repositoryPath
	 * @param metrics
	 * Does not analyze the repository, only calculates a list of metrics from beginning of the repository.
	 */
	public void recalculateMetrics(String repositoryPath, List<MetricUid> metrics) {
		
		EntityManager entityManager = Database.getInstance().getEntityManager();
		RepositoryDAO repositoryDao = new RepositoryDAOImpl();
		repositoryDao.setEntityManager(entityManager);
		
		RepositoryDB repoDb = repositoryDao.findByUid(StringUtils.sha1(repositoryPath));
		if(repoDb == null || metrics == null || metrics.size() == 0){
			return;
		}
		
		IRepositorySystem repoSys = SupportedRepository.getRepository(repoDb.getPath(), repoDb.getType());
		
		MetricCalculator.calculate(metrics, repoSys, repoDb, entityManager);
		
		repoSys.close();
		entityManager.close();
		
	}

}
