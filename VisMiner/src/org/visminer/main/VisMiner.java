package org.visminer.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.visminer.constants.Metrics;
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
import org.visminer.util.AnalyzeRepository;

public class VisMiner {

	private Repository repository;
	
	public VisMiner(Map<String, String> databaseProperties, String repositoryPath) throws IOException, GitAPIException{
		
		Connection.setDataBaseInfo(databaseProperties);
		init();

		RepositoryDAO repoDAO = new RepositoryDAO();
		repository = repoDAO.getByPath(repositoryPath);
		
		if(repository == null){
			AnalyzeRepository analyzeRepo = new AnalyzeRepository(repositoryPath);
			Thread thread = new Thread(analyzeRepo);
			thread.start();
			repository = analyzeRepo.getRepository();
		}
		
	}
	
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
	
	public Repository getRepository(){
		
		return this.repository;
		
	}
	
	public List<Committer> getCommitters(){
		
		CommitterDAO committerDAO = new CommitterDAO();
		return committerDAO.getByRepository(repository);
	}
	
	public List<Version> getVersions(){
		
		VersionDAO versionDAO = new VersionDAO();
		return versionDAO.getByRepository(repository);
		
	}
	
	public List<Commit> getCommits(Version version, Committer committer){
		
		CommitDAO commitDAO = new CommitDAO();
		return commitDAO.getByVersionAndCommitter(version, committer);
		
	}
	
	public List<Commit> getCommits(Version version){
		
		CommitDAO commitDAO = new CommitDAO();
		return commitDAO.getByVersion(version);
		
	}	
	
	public List<Commit> getCommits(Committer committer){
		
		CommitDAO commitDAO = new CommitDAO();
		return commitDAO.getByCommitter(committer);
		
	}	
	
	public List<File> getFiles(Commit commit){
		
		FileDAO fileDAO = new FileDAO();
		return fileDAO.getByCommit(commit);
		
	}
	
	public List<Metric> getMetrics(){
		
		MetricDAO metricDAO = new MetricDAO();
		return metricDAO.getAll();
		
	}
	
}
