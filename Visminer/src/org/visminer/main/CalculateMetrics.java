package org.visminer.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.visminer.config.MetricAttribute;
import org.visminer.config.MetricsConfig;
import org.visminer.constant.RepositoryType;
import org.visminer.constant.SoftwareEntityType;
import org.visminer.extractor.GitRepository;
import org.visminer.extractor.ILocalRepository;
import org.visminer.model.business.Repository;
import org.visminer.model.database.Commit;
import org.visminer.model.database.File;
import org.visminer.model.business.Metric;
import org.visminer.model.database.MetricValue;
import org.visminer.model.database.MetricValuePK;
import org.visminer.model.database.SoftwareEntity;
import org.visminer.persistence.PersistenceFacade;

public class CalculateMetrics {

	public static void calculate(Repository repository){
		
		ILocalRepository repositoryService = getRepository(repository);
		PersistenceFacade persistence = new PersistenceFacade();
		
		List<Commit> commits = persistence.getCommitsByRepository(repository.getId());
		
		
		for(Commit commit : commits){
			for(File file : persistence.getFilesByCommit(commit.getId())){
				
				if(file.getFileXCommits().get(0).isDeleted()) continue;
				
				List<MetricValue> metricsValues = new ArrayList<MetricValue>();
				
				for(Entry<Metric, MetricAttribute> metric : MetricsConfig.metrics.entrySet()){
					
					if(!verifyExtension(file.getPath(), metric.getValue().getExtension())) continue;
					
					org.visminer.model.database.Metric metricDb = persistence.getMetric(metric.getKey().getId());
					
					MetricValue metricValue = new MetricValue();
					MetricValuePK metricValuePK = new MetricValuePK();
					
					metricValuePK.setMetricId(metricDb.getId());
					metricValue.setId(metricValuePK);
					metricValue.setMetric(metricDb);
						
					byte[] data = repositoryService.getFileState(commit.getName(), file.getPath());
					metricValue.setValue(String.valueOf(metric.getValue().getMetricCls().calculate(data)));
					metricsValues.add(metricValue);
					
				}
				
				if(metricsValues.size() > 0){
					
					SoftwareEntity softwareEntity = new SoftwareEntity();
					softwareEntity.setFileXCommit(persistence.getFileXCommit(commit.getId(), file.getId()));
					softwareEntity.setType(SoftwareEntityType.FILE);
					softwareEntity.setName(file.getPath());
					
					SoftwareEntity softwareEntity2 = persistence.saveSoftwareEntity(softwareEntity);
					
					for(MetricValue m : metricsValues){
						m.setSoftwareEntity(softwareEntity2);
						m.getId().setSoftwareEntityId(softwareEntity2.getId());
					}
					
					persistence.saveMetricsValues(metricsValues);
					
				}
				
			}
		}		
		
	}
	
	private static ILocalRepository getRepository(Repository repository){
		
		ILocalRepository repositoryAux = null;
		
		switch(repository.getType()){
			case RepositoryType.GIT:
				repositoryAux = new GitRepository();
			break;
			default:
				return null;
			
		}
		
		repositoryAux.setLocalPath(repository.getPath());
		return repositoryAux;
	}
	
	private static boolean verifyExtension(String path, List<String> extensions){
		
		for(String ext : extensions){
			if(path.endsWith("."+ext)) return true;
		}
		
		return false;
		
	}
	
}
