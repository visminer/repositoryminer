package org.visminer.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.visminer.constants.Metrics;
import org.visminer.constants.Services;
import org.visminer.git.local.AnalyzeRepository;
import org.visminer.git.remote.ConnectionToRepository;
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

	public static final String REMOTE_REPOSITORY_USER = "user";
	public static final String REMOTE_REPOSITORY_PASSWORD = "password";
	public static final String REMOTE_REPOSITORY_OWNER = "owner";
	public static final String REMOTE_REPOSITORY_NAME = "repository_name";
	public static final String LOCAL_REPOSITORY_PATH = "repository_path";
	public static final String REMOTE_REPOSITORY_SERVICE = "service";
	
	private Repository repository;
	
	/**
	 * @param config : Map containing pair key and value used to configure the API 
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public VisMiner(Map<String, String> visminer_cfg, Map<String, String> db_cfg) throws IOException, GitAPIException{
		
		init();
		Connection.setDataBaseInfo(db_cfg);
		
		if(visminer_cfg.get(VisMiner.LOCAL_REPOSITORY_PATH) != null){
		
			RepositoryDAO repoDAO = new RepositoryDAO();
			Repository repo = repoDAO.getByPath(visminer_cfg.get(VisMiner.LOCAL_REPOSITORY_PATH));
			if(repo == null){
				String idGit = visminer_cfg.get(VisMiner.REMOTE_REPOSITORY_OWNER) + "/" + visminer_cfg.get(VisMiner.REMOTE_REPOSITORY_NAME);
				AnalyzeRepository analyze = new AnalyzeRepository(visminer_cfg.get(VisMiner.LOCAL_REPOSITORY_PATH), idGit);
			}
			
			if(visminer_cfg.get(VisMiner.REMOTE_REPOSITORY_SERVICE) != null){
				ConnectionToRepository connection = new ConnectionToRepository(visminer_cfg.get(VisMiner.REMOTE_REPOSITORY_OWNER),
						visminer_cfg.get(VisMiner.REMOTE_REPOSITORY_NAME), Services.valueOf(visminer_cfg.get(VisMiner.REMOTE_REPOSITORY_NAME)));
				//faz parte do github
			}
			
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
