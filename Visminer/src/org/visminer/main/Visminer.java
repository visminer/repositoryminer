package org.visminer.main;

import java.util.ArrayList;
import java.util.List;

import org.visminer.config.Configuration;
import org.visminer.config.MetricsConfig;
import org.visminer.model.business.Committer;
import org.visminer.model.business.Metric;
import org.visminer.model.business.Repository;
import org.visminer.persistence.PersistenceFacade;

public class Visminer {

	private Analyzer analyzer;
	private PersistenceFacade persistenceFacade;
	
	public Visminer(){
		this.analyzer = new Analyzer();
		persistenceFacade = new PersistenceFacade();
	}
	
	public void configure(String path){
		Configuration.configure(path);
	}
	
	public void configureMetrics(String path){
		MetricsConfig.configure(path);
	}
	
	public Analyzer getAnalyzer(){
		return this.analyzer;
	}
	
	public void setAnalyzer(Analyzer analyzer){
		this.analyzer = analyzer;
	}
	
	public Repository analyze(){
		Repository repository = this.analyzer.analyzeRepository();
		CalculateMetrics.calculate(repository);
		repository.start();
		return repository;
	}
	
	public List<Committer> getCommitters(){
		
		List<Committer> committers = new ArrayList<Committer>();
		
		for(org.visminer.model.database.Committer c : persistenceFacade.getAllCommitters()){
			Committer committer = new Committer(c.getId(), c.getName(), c.getEmail());
			committers.add(committer);
		}
		return committers;
		
	}
	
	public List<Repository> getRepositories(){
		
		List<Repository> repositories = new ArrayList<Repository>();
		
		for(org.visminer.model.database.Repository repo : persistenceFacade.getAllRepositories()){
			
			Repository repository = new Repository(repo.getId(), repo.getPath(), repo.getName(), repo.getRemoteOwner(),
					repo.getRemoteName(), repo.getType(), repo.getRemoteService());
			
			repositories.add(repository);
		}
		return repositories;
		
	}
	
	public List<Metric> getMetrics(){

		List<Metric> metrics = new ArrayList<Metric>();
		
		for(org.visminer.model.database.Metric m : persistenceFacade.getAllMetrics()){
			Metric metric = new Metric(m.getName(), m.getDescription(), m.getId());
			metrics.add(metric);
		}
		return metrics;
	
	}
	
	public List<Repository> getRepositoriesByCommitter(Committer committer){

		List<Repository> repositories = new ArrayList<Repository>();
		
		for(org.visminer.model.database.Repository repo : persistenceFacade.getRepositoriesByCommitter(committer.getId())){
			Repository repository = new Repository(repo.getId(), repo.getPath(), repo.getName(), repo.getRemoteOwner(),
					repo.getRemoteName(), repo.getType(), repo.getRemoteService());
			
			repositories.add(repository);
		}
		return repositories;
		
	}

	public Repository getRepository(String path){
		
		org.visminer.model.database.Repository repo = persistenceFacade.getRepositoryByPath(path);
		Repository repository = new Repository(repo.getId(), repo.getPath(), repo.getName(), repo.getRemoteOwner(),
				repo.getRemoteName(), repo.getType(), repo.getRemoteService());
		
		return repository;
		
	}
	
}