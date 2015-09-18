package br.edu.ufba.softvis.visminer.analyzer;

import java.io.IOException;
import java.util.List;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IVersioningSystem;
import br.edu.ufba.softvis.visminer.analyzer.local.VersioningSystemFactory;
import br.edu.ufba.softvis.visminer.constant.LanguageType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.RepositoryDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitDAOImpl;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * Defines how to save or to increment informations about the repository in database.
 */
public class RepositoryAnalyzer{

	private static final String EXCEPTION_MESSAGE = "Repository already exists in database";
	
	/*
	 * This class is responsible for join all the analyzers and make the
	 * repository analyzis.
	*/
	public void persist(Repository repoBusi, List<MetricUid> metrics, List<LanguageType> languages) {
		
		IVersioningSystem repoSys = VersioningSystemFactory.getRepository(repoBusi.getType());
		repoSys.open(repoBusi.getPath());
		
		EntityManager entityManager = Database.getInstance().getEntityManager();
		
		RepositoryDAO repositoryDao = new RepositoryDAOImpl();
		repositoryDao.setEntityManager(entityManager);
		
		SoftwareUnitDAO softwareUnitDao = new SoftwareUnitDAOImpl();
		softwareUnitDao.setEntityManager(entityManager);
		
		String path = repoSys.getAbsolutePath();
		if(repositoryDao.hasRepository(path)){
			throw new KeyAlreadyExistsException(EXCEPTION_MESSAGE);
		}
		
		String uid = StringUtils.sha1(path);
		RepositoryDB repositoryDb = new RepositoryDB(0, repoBusi.getDescription(),
				repoBusi.getName(), repoBusi.getOwner(), path, repoBusi.getType(),
				repoBusi.getServiceType(), uid, repoBusi.getCharset());
		
		if(repoBusi.getCharset() == null){
			repositoryDb.setCharset("UTF-8"); //UTF-8 is used as default charset
		}
		
		repositoryDao.save(repositoryDb);
		List<CommitDB> commitsDB = new CommitAndCommitterAnalyzer().persist(repositoryDb, repoSys, entityManager);
		
		new TreeAnalyzer().persist(commitsDB, repositoryDb, repoSys, entityManager);
		new FileAnalyzer().persist(commitsDB, repoSys, entityManager);

		commitsDB = null;
		
		SoftwareUnitDB softUnitDb = new SoftwareUnitDB(0, repositoryDb.getName(), SoftwareUnitType.PROJECT, repositoryDb.getUid());
		softUnitDb.setRepository(repositoryDb);
		softwareUnitDao.save(softUnitDb);
		
		if(metrics != null && metrics.size() > 0){
			MetricCalculator m = new MetricCalculator();
			m.calculate(metrics, repoSys, repositoryDb, languages);
		}

		entityManager.clear();
		entityManager.close();
		repoSys.close();
		
	}

	/**
	 * @param repositoryPath
	 * @param metrics
	 * Does not analyze the repository, only calculates a list of metrics from beginning of the repository.
	 * @throws IOException 
	 */
	public void recalculateMetrics(String repositoryPath, List<MetricUid> metrics){
		
		EntityManager entityManager = Database.getInstance().getEntityManager();
		RepositoryDAO repositoryDao = new RepositoryDAOImpl();
		repositoryDao.setEntityManager(entityManager);
		
		RepositoryDB repoDb = repositoryDao.findByPath(repositoryPath);
		if(repoDb == null || metrics == null || metrics.size() == 0){
			return;
		}
		
		IVersioningSystem repoSys = VersioningSystemFactory.getRepository(repoDb.getType());
		repoSys.open(repositoryPath);
		
		//MetricCalculator.calculate(metrics, repoSys, repoDb, entityManager);
		
		repoSys.close();
		entityManager.flush();
		entityManager.close();
		
	}

}
