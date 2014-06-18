package org.visminer.main;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.visminer.constants.Metrics;
import org.visminer.git.local.AnalyzeRepository;
import org.visminer.model.Metric;
import org.visminer.model.Repository;
import org.visminer.persistence.Connection;
import org.visminer.persistence.MetricDAO;
import org.visminer.persistence.RepositoryDAO;

public class Initializer {

	public static Repository init(VisMiner visminer) throws IOException, GitAPIException{
		
		Connection.setDataBaseInfo(visminer.getDb_cfg());
		verifyMetrics();
		
		RepositoryDAO repoDAO = new RepositoryDAO();
		
		String idGit = visminer.getVisminer_cfg_local().get(VisMiner.LOCAL_REPOSITORY_OWNER)+ "/"+
				visminer.getVisminer_cfg_local().get(VisMiner.LOCAL_REPOSITORY_NAME);
		
		Repository repository = repoDAO.getByIdGit(idGit);
		
		if(repository == null){
			
			AnalyzeRepository analyzer = new AnalyzeRepository(
					visminer.getVisminer_cfg_local().get(VisMiner.LOCAL_REPOSITORY_PATH), idGit, visminer);
			
			visminer.updateIssuesMilestones(analyzer.getRepository());

			analyzer.run();

			repository = analyzer.getRepository();
			
		}
				
		return repository;
		
	}
	
	
	private static void verifyMetrics(){
		
		MetricDAO metricDAO = new MetricDAO();
		for(Metrics m : Metrics.values()){
			
			if(metricDAO.getOne(m.getValue()) == null){
				Metric metric = new Metric();
				metric.setIdMetric(m.getValue());
				metric.setName(m.toString());
				metric.setDescription(m.getDescription());
				metricDAO.save(metric);
			}
			
		}
		
	}
	
}
