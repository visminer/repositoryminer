package org.visminer.main;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.visminer.constants.Metrics;
import org.visminer.git.local.AnalyzeRepository;
import org.visminer.model.Metric;
import org.visminer.model.Repository;
import org.visminer.persistence.Connection;
import org.visminer.persistence.MetricDAO;
import org.visminer.persistence.RepositoryDAO;

public class Initializer {

	public static Repository init(Map<String, String> db_cfg, Map<Integer, String> visminer_cfg) throws IOException, GitAPIException{
		
		Connection.setDataBaseInfo(db_cfg);
		verifyMetrics();
		
		RepositoryDAO repoDAO = new RepositoryDAO();
		
		String idGit = visminer_cfg.get(VisMiner.LOCAL_REPOSITORY_OWNER)+"/"+visminer_cfg.get(VisMiner.LOCAL_REPOSITORY_NAME);
		Repository repository = repoDAO.getByIdGit(idGit);
		
		if(repository == null){
			AnalyzeRepository analyzer = new AnalyzeRepository(visminer_cfg.get(VisMiner.LOCAL_REPOSITORY_PATH), idGit);
			analyzer.run();
			repository = analyzer.getRepository();
		}
		
		/* for the future
		 * if(visminer_cfg.get(VisMiner.REMOTE_REPOSITORY_URL) !=null){
		 * connect to remote service
		 * }
		 */
		
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
