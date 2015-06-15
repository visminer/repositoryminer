package br.edu.ufba.softvis.visminer.analyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IRepositorySystem;
import br.edu.ufba.softvis.visminer.config.MetricConfig;
import br.edu.ufba.softvis.visminer.constant.MetricId;
import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.model.bean.FileState;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.MetricDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;
import br.edu.ufba.softvis.visminer.persistence.dao.FileDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.FileDAOImpl;
import br.edu.ufba.softvis.visminer.utility.DetailAST;

public class MetricCalculator{

	private static final Set<String> extensions = initExtensions();
			
	private static Set<String> initExtensions(){
		
		Set<String> set = new HashSet<String>();
		
		// set here the files that should have their ast calculated
		set.add("java");
		
		return set;
	}
	
	private static boolean isASTCalculable(String filePath){
		int index = filePath.lastIndexOf(".") + 1;
		return extensions.contains(filePath.substring(index));
	}
	
	public static void calculate(List<CommitDB> commitsDb, List<MetricId> metricsId, IRepositorySystem repoSys,
			RepositoryDB repositoryDb, EntityManager entityManager) {

		FileDAO fileDao = new FileDAOImpl();
		fileDao.setEntityManager(entityManager);
		
		Map<MetricId, IMetric> metrics = MetricConfig.getImplementations(metricsId);
		
		Map<File, DetailAST> filesMap = new HashMap<File, DetailAST>();
		
		for(int i = 0; i < commitsDb.size(); i++){
			
			CommitDB commitDb = commitsDb.get(i);
			
			List<FileDB> filesDbAux = fileDao.findCommitedFiles(commitDb.getId());
			
			for(FileDB fileDb : filesDbAux){
				
				File fileAux = new File(fileDb);
				if(fileDb.getFileXCommits().get(0).getRemoved()){
					filesMap.remove(fileAux);
				}else{
					
					FileState fs = new FileState(fileDb.getFileXCommits().get(0));
					fileAux.setFileState(fs);
					filesMap.remove(fileAux);
					
					int index = repoSys.getAbsolutePath().length()+1;
					byte[] data = repoSys.getData(commitDb.getName(), fileAux.getPath().substring(index));
					DetailAST detailAST = new DetailAST();
					
					if(isASTCalculable(fileAux.getPath())){
						detailAST.partserFromBytes(data);
					}else{
						detailAST.setData(data);
					}
					
					filesMap.put(fileAux, detailAST);
					
				}// end else
			}// end for(FileDB fileDb : filesDbAux)
			
			Commit commitBeanPrev = null;
			
			if(i > 0){
				commitBeanPrev = commitsDb.get(i-1).retrieveBean(); 
			}
			
			calculateMetrics(commitDb, commitBeanPrev, filesMap, repositoryDb, entityManager, metrics);
			
		}// end for(CommitDB commitDb : commitsDb)
		
	}
	
	
	private static void calculateMetrics(CommitDB commitDb, Commit commitBeanPrev, Map<File, DetailAST> filesMap, 
			RepositoryDB repositoryDb, EntityManager entityManager, Map<MetricId, IMetric> metrics){
		
		MetricPersistance persistence = new MetricPersistance(commitDb, repositoryDb, entityManager);
		MetricDB metricDb = new MetricDB();
		
		for(Entry<MetricId, IMetric> entry : metrics.entrySet()){
			metricDb.setId(entry.getKey());
			persistence.setMetricDB(metricDb);
			entry.getValue().calculate(filesMap, commitBeanPrev, persistence);
		}
		
	}
	
}