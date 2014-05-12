package org.visminer.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.visminer.constants.Metrics;
import org.visminer.git.local.AnalyzeRepository;
import org.visminer.model.Commit;
import org.visminer.model.Committer;
import org.visminer.model.File;
import org.visminer.model.Metric;
import org.visminer.model.Repository;
import org.visminer.model.Version;
import org.visminer.persistence.CommitDAO;
import org.visminer.persistence.CommitterDAO;
import org.visminer.persistence.Connection;
import org.visminer.persistence.FileDAO;
import org.visminer.persistence.MetricDAO;
import org.visminer.persistence.RepositoryDAO;
import org.visminer.persistence.VersionDAO;

/**
 * <p>
 * interface between user and VisMiner. Provide access to API features
 * </p>
 * 
 * @author Felipe
 * @version 1.0
 */
public class VisMiner {

	private Repository repository;
	
	/**
	 * 
	 * @param databaseProperties : database configuration informations
	 * @param repositoryPath : local repository path
	 * @param ownerRepository : owner of remote repository
	 * @param nameRepository : name of remote repository
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public VisMiner(Map<String, String> databaseProperties, String repositoryPath,
		String ownerRepository,	String nameRepository) throws IOException, GitAPIException{
		
		init();

		String idGit = ownerRepository+ "/"+ nameRepository;
		
		RepositoryDAO repoDAO = new RepositoryDAO();
		repository = repoDAO.getByIdGit(idGit);
		
		if(repository == null){
			AnalyzeRepository analyzeRepo = new AnalyzeRepository(repositoryPath, idGit);
			Thread thread = new Thread(analyzeRepo);
			thread.start();
			repository = analyzeRepo.getRepository();
		}
		
	}
	
	//save the default metrics in database
	private void init(){
		
		MetricDAO metricDAO = new MetricDAO();
		for(Metrics m : Metrics.values()){
			
			if(metricDAO.getOne(m.getValue()) == null){
				Metric metric = new Metric();
				metric.setIdmetric(m.getValue());
				metric.setName(m.toString());
				metric.setDescription(m.getDescription());
				metricDAO.save(metric);
			}
			
		}
		
	}
	
	/**
	 * 
	 * @return repository
	 */
	public Repository getRepository(){
		
		return this.repository;
		
	}
	
	/**
	 * 
	 * @return committers in repository
	 */
	public List<Committer> getCommitters(){
		
		CommitterDAO committerDAO = new CommitterDAO();
		return committerDAO.getByRepository(repository);
	}
	
	/**
	 * 
	 * @return versions in repository
	 */
	public List<Version> getVersions(){
		
		VersionDAO versionDAO = new VersionDAO();
		return versionDAO.getByRepository(repository);
		
	}
	
	/**
	 * 
	 * @param version
	 * @param committer
	 * @return commits in repository by version and committer
	 */
	public List<Commit> getCommits(Version version, Committer committer){
		
		CommitDAO commitDAO = new CommitDAO();
		return commitDAO.getByVersionAndCommitter(version, committer);
		
	}
	
	/**
	 * 
	 * @param version
	 * @return commits in repository by version
	 */
	public List<Commit> getCommits(Version version){
		
		CommitDAO commitDAO = new CommitDAO();
		return commitDAO.getByVersion(version);
		
	}	
	
	/**
	 * 
	 * @param committer
	 * @return commits in repository by committer
	 */
	public List<Commit> getCommits(Committer committer){
		
		CommitDAO commitDAO = new CommitDAO();
		return commitDAO.getByCommitter(committer);
		
	}	
	
	/**
	 * 
	 * @param commit
	 * @return files in data
	 */
	public List<File> getFiles(Commit commit){
		
		FileDAO fileDAO = new FileDAO();
		return fileDAO.getByCommit(commit);
		
	}
	
	/**
	 * 
	 * @return all metrics
	 */
	public List<Metric> getMetrics(){
		
		MetricDAO metricDAO = new MetricDAO();
		return metricDAO.getAll();
		
	}
	
}
