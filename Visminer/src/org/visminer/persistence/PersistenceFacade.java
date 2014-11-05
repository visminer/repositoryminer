package org.visminer.persistence;

import java.util.Arrays;
import java.util.List;

import org.visminer.model.database.Commit;
import org.visminer.model.database.Committer;
import org.visminer.model.database.File;
import org.visminer.model.database.FileXCommit;
import org.visminer.model.database.FileXCommitPK;
import org.visminer.model.database.Metric;
import org.visminer.model.database.MetricValue;
import org.visminer.model.database.Repository;
import org.visminer.model.database.SoftwareEntity;
import org.visminer.model.database.Tree;
import org.visminer.utility.StringDigest;

public class PersistenceFacade {

	private CommitDAO commitDao;
	private CommitterDAO committerDao;
	private FileDAO fileDao;
	private FileXCommitDAO fileXCommitDao;
	private MetricDAO metricDao;
	private MetricValueDAO metricValueDao;
	private RepositoryDAO repositoryDao;
	private SoftwareEntityDAO softwareEntityDao;
	private TreeDAO treeDao;
	
	public PersistenceFacade(){
		
		commitDao = new CommitDAO();
		committerDao = new CommitterDAO();
		fileDao = new FileDAO();
		fileXCommitDao = new FileXCommitDAO();
		metricDao = new MetricDAO();
		metricValueDao = new MetricValueDAO();
		repositoryDao = new RepositoryDAO();
		softwareEntityDao = new SoftwareEntityDAO();
		treeDao = new TreeDAO();
		
	}
	
	public List<Commit> getCommitsByNames(List<String> names){
		return commitDao.getByNames(names);
	}
	
	public List<Commit> getCommitsByTree(int treeId){
		return commitDao.getByTree(treeId);
	}
	
	public Committer getCommitterByEmailAndName(String name, String email){
		return committerDao.getByNameAndEmail(name, email);
	}
	
	public List<Committer> saveCommitters(List<Committer> committers){
		return committerDao.saveAll(committers);
	}
	
	public List<Commit> getCommitsByRepository(int repositoryId){
		return commitDao.getByRepository(repositoryId);
	}
	
	public List<Committer> getAllCommitters(){
		return committerDao.getAll();
	}
	
	public List<Committer> getCommittersByRepository(int repositoryId){
		return committerDao.getByRepository(repositoryId);
	}
	
	public File getFileBySha(String sha){
		return fileDao.getBySha(sha);
	}
	
	public File saveFile(File file){
		return fileDao.save(file);
	}
	
	public List<File> getFilesByCommit(int commitId){
		List<File> files = fileDao.getByCommit(commitId);
		for(File f : files){
			List<FileXCommit> list = Arrays.asList(getFileXCommit(commitId, f.getId()));
			f.setFileXCommits(list);
		}
		return files;
	}
	
	public void saveFilesXCommits(List<FileXCommit> filesXCommits){
		fileXCommitDao.saveAll(filesXCommits);
	}
	
	public FileXCommit getFileXCommit(int commitId, int fileId){
		FileXCommitPK pk = new FileXCommitPK(fileId, commitId);
		return fileXCommitDao.get(pk);
	}
	
	public Metric saveMetric(Metric metric){
		return metricDao.save(metric);
	}
	
	public List<Metric> getAllMetrics(){
		return metricDao.getAll();
	}
	
	public Metric getMetricByName(String name){
		return metricDao.getByName(name);
	}
	
	public Metric getMetric(int id){
		return metricDao.get(id);
	}
	
	public void saveMetricsValues(List<MetricValue> metricsValues){
		metricValueDao.saveAll(metricsValues);
	}
	
	public Repository saveRepository(Repository repository){
		return repositoryDao.save(repository);
	}
	
	public Repository getRepositoryByPath(String path){
		String param = StringDigest.sha1(path);
		return repositoryDao.getByPath(param);
	}
	
	public List<Repository> getRepositoriesByCommitter(int committerId){
		return repositoryDao.getByCommitter(committerId);
	}
	
	public List<Repository> getAllRepositories(){
		return repositoryDao.getAll();
	}
	
	public void saveAllTrees(List<Tree> trees){
		treeDao.saveAll(trees);
	}
	
	public List<Tree> getTreesByRepository(int repositoryId){
		return treeDao.getByRepository(repositoryId);
	}
	
	public SoftwareEntity saveSoftwareEntity(SoftwareEntity softwareEntity){
		return softwareEntityDao.save(softwareEntity);
	}
	
	public List<SoftwareEntity> getSoftwareEntityByCommitAndFile(int commitId, int fileId){
		return softwareEntityDao.getByCommitAndFile(commitId, fileId);
	}

	public List<MetricValue> getMetricsValuesBySoftwareEntity(int softwareEntityId) {
		return metricValueDao.getBySoftwareEntity(softwareEntityId);
	}
	
}
