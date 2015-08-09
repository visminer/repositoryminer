package br.edu.ufba.softvis.visminer.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.Committer;
import br.edu.ufba.softvis.visminer.model.business.File;
import br.edu.ufba.softvis.visminer.model.business.FileState;
import br.edu.ufba.softvis.visminer.model.business.Metric;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;
import br.edu.ufba.softvis.visminer.model.business.Tree;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.MetricDB;
import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitterDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.FileXCommitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricValueDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.TreeDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitterDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.FileXCommitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.MetricDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.MetricValueDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.RepositoryDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.TreeDAOImpl;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Persistence interface for user access.
 */
public class PersistenceInterface {

	private EntityManager entityManager;
	
	public PersistenceInterface(){
		// Opens database connection.
		this.entityManager = Database.getInstance().getEntityManager();
	}
	
	/**
	 * Closes database connection.
	 */
	public void close(){
		this.entityManager.close();
	}
	
	/**
	 * 
	 * @param repositoryPath
	 * @return Repository with given path
	 */
	public Repository findRepository(String repositoryPath) {
		
		RepositoryDAO repositoryDao = new RepositoryDAOImpl();
		repositoryDao.setEntityManager(entityManager);
		
		String path = repositoryPath.replace("\\", "/");
		
		String uid = StringUtils.sha1(path);
		RepositoryDB repoDb = repositoryDao.findByUid(uid);
	
		if(repoDb == null)
			return null;
		
		Repository repository = new Repository(repoDb.getId(), repoDb.getDescription(), repoDb.getName(),
				repoDb.getPath(), repoDb.getRemoteUrl(), repoDb.getType(),repoDb.getServiceType(),
				repoDb.getUid(), repoDb.getCharset());
		
		return repository;
		
	}

	/**
	 * 
	 * @param repositoryId
	 * @return All repository committers (contributors and not contributors).
	 */
	public List<Committer> findCommitters(int repositoryId){
		
		CommitterDAO committerDao = new CommitterDAOImpl();
		committerDao.setEntityManager(entityManager);
		
		List<CommitterDB> committersDb = committerDao.findByRepository(repositoryId);
		List<Committer> committers = new ArrayList<Committer>();
		
		for(CommitterDB committerDb : committersDb){
			
			Committer committer = new Committer(committerDb.getId(), committerDb.getEmail(), committerDb.getName());
			committers.add(committer);
		}

		return committers;
		
	}
	
	/**
	 * 
	 * @param repositoryId
	 * @return All repository trees.
	 */
	public List<Tree> findTrees(int repositoryId){
		
		TreeDAO treeDao = new TreeDAOImpl();
		treeDao.setEntityManager(entityManager);
		
		List<TreeDB> treesDb = treeDao.findByRepository(repositoryId);
		
		List<Tree> trees = new ArrayList<Tree>();
		for(TreeDB treeDb : treesDb){
			Tree tree = new Tree(treeDb.getId(), treeDb.getLastUpdate(), treeDb.getName(), treeDb.getFullName(), treeDb.getType());
			trees.add(tree);
		}
		
		return trees;
		
	}
	
	/**
	 * @param treeId
	 * @return All commits related with given tree.
	 */
	public List<Commit> findCommitsByTree(int treeId) {
		
		CommitDAO commitDao = new CommitDAOImpl();
		commitDao.setEntityManager(entityManager);
		
		FileXCommitDAO fxcDao = new FileXCommitDAOImpl();
		fxcDao.setEntityManager(entityManager);
		
		List<CommitDB> commitsDb = commitDao.findByTree(treeId);
		List<Commit> commits = new ArrayList<Commit>();
		
		for(CommitDB commitDb : commitsDb){
			
			Commit commit = new Commit(commitDb.getId(), commitDb.getDate(), commitDb.getMessage(),
					commitDb.getName());
			
			CommitterDB c = commitDb.getCommitter();
			Committer committer = new Committer(c.getId(), c.getEmail(), c.getName());
			
			List<File> files = new ArrayList<File>();
			
			for(FileXCommitDB fxc : fxcDao.findByCommit(commitDb.getId())){
				FileState fileState = new FileState(fxc.getLinesAdded(), fxc.getLinesRemoved(), fxc.isRemoved());
				FileDB fileDb = fxc.getFile();
				File file = new File(fileDb.getId(), fileDb.getPath(), fileDb.getUid());
				file.setFileState(fileState);
				files.add(file);
			}

			commit.setCommitedFiles(files);
			commit.setCommitter(committer);
			commits.add(commit);
			
		}
		
		return commits;
		
	}

	/**
	 * @param repositoryId
	 * @param commitId
	 * @return All software units related with given repository in certain commit.
	 */
	public List<SoftwareUnit> findSoftwareUnitByRepository(int repositoryId, int commitId) {
		
		SoftwareUnitDAO softDao = new SoftwareUnitDAOImpl();
		softDao.setEntityManager(entityManager);
		
		List<SoftwareUnitDB> softUnitsDb = softDao.findByRepository(repositoryId);
		List<SoftwareUnit> softwareUnits = new ArrayList<SoftwareUnit>();
		
		for(SoftwareUnitDB softUnitDb : softUnitsDb){
			
			SoftwareUnit softUnitTemp = new SoftwareUnit(softUnitDb.getId(), softUnitDb.getName(),
					softUnitDb.getUid(), softUnitDb.getType());
			
			if(softUnitDb.getSoftwareUnit() != null){
				SoftwareUnit parentTemp = new SoftwareUnit();
				parentTemp.setId(softUnitDb.getSoftwareUnit().getId());
				softUnitTemp.setParentUnit(parentTemp);
			}
			
			if(softUnitDb.getFile() != null){
				File file = new File();
				file.setId(softUnitDb.getId());
				softUnitTemp.setFile(file);
			}
			
			Map<MetricUid, String> metricsVal = findMetricValue(softUnitTemp.getId(), commitId);
			softUnitTemp.setMetricValues(metricsVal);
			
			softwareUnits.add(softUnitTemp);
			
		}
		
		processListToTree(softwareUnits);
		return softwareUnits;
		
	}

	/**
	 * @param softwareUnitId
	 * @param commitId
	 * @return A map with values of all metrics calculated over given software unit in certain commit.
	 */
	public Map<MetricUid, String> findMetricValue(int softwareUnitId, int commitId){
		
		MetricValueDAO metricValDao = new MetricValueDAOImpl();
		metricValDao.setEntityManager(entityManager);
		
		List<MetricValueDB> metricValsDb = metricValDao.findBySoftwareUnitAndCommit(softwareUnitId, commitId);
		Map<MetricUid, String> metricVal = new HashMap<MetricUid, String>();
		
		for(MetricValueDB elem : metricValsDb){
			metricVal.put(elem.getMetric().getId(), elem.getValue());
		}
		
		return metricVal;
		
	}
	/**
	 * @return All metrics that VisMiner gives support.
	 */
	public List<Metric> findAllMetrics(){
		
		MetricDAO metricDao = new MetricDAOImpl();
		metricDao.setEntityManager(entityManager);

		Collection<MetricDB> metricsDb = metricDao.findAll();
		List<Metric> metrics = new ArrayList<Metric>(metricsDb.size());
		for(MetricDB metricDb : metricsDb){
			Metric m = new Metric(metricDb.getId().getId(), metricDb.getAcronym(),
					metricDb.getDescription(), metricDb.getName(), metricDb.getId(), metricDb.getType());
			metrics.add(m);
		}
		return metrics;
		
	}
	
	// helpers
	
	/*
	 * Process flat softwareUnits list and transforms into a graph.
	 */
	private void processListToTree(List<SoftwareUnit> softwareUnits){
		
		int i = 0;
		while(i < softwareUnits.size()){
			SoftwareUnit elem = softwareUnits.get(i);
			if(elem.getParentUnit() == null){
				i++;
			}else{
				elem = softwareUnits.remove(i);
				setElementInTree(softwareUnits, elem);
			}
		}
		
	}
	
	/**
	 * @param list
	 * @param elem
	 * Puts the element in right place in the graph respecting the software unit hierarchy.
	 */
	private void setElementInTree(List<SoftwareUnit> list, SoftwareUnit elem){
		
		if(list == null){
			return;
		}
		
		if(list.contains(elem.getParentUnit())){
			
			int index = list.indexOf(elem.getParentUnit());
			SoftwareUnit parent = list.get(index);
			if(parent.getChildren() == null){
				parent.setChildren(new ArrayList<SoftwareUnit>());
			}
			elem.setParentUnit(parent);
			parent.getChildren().add(elem);
			
		}else{
			
			for(int i = 0; i < list.size(); i++){
				setElementInTree(list.get(i).getChildren(), elem);
			}
			
		}
		
	}
	
}