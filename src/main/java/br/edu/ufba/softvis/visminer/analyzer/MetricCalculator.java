package br.edu.ufba.softvis.visminer.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IRepositorySystem;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.Document;
import br.edu.ufba.softvis.visminer.ast.generator.JavaASTGenerator;
import br.edu.ufba.softvis.visminer.config.MetricConfig;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.TreeType;
import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.model.bean.FileState;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;
import br.edu.ufba.softvis.visminer.persistence.SaveAST;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.FileDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.TreeDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.FileDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.TreeDAOImpl;

/**
 * @version 0.9
 * Manages all the process to calculate the metrics.
 */
public class MetricCalculator{

	private static final Set<String> sourceExtensions = initSourceExtensions();
	private static final Set<String> acceptedExtensions = initAcceptedExtensions();
	private static final Set<String> avoidedExtensions = initAvoidedExtensions();

	//List of file files extension that should have their AST calculated.
	private static Set<String> initSourceExtensions(){

		Set<String> set = new HashSet<String>();

		// set here the files that should have their ast calculated
		set.add("java");

		return set;
	}

	// List of files extensions that will have their content accessed, but they won't have any AST calculated.
	private static Set<String> initAcceptedExtensions(){
		
		Set<String> set = new HashSet<String>();
		set.add("txt");
		set.add("md");
		set.add("xml");
		set.add("json");
		set.add("html");
		set.add("css");
		
		return set;
	}
	
	//List of files extensions that should be avoided.
	private static Set<String> initAvoidedExtensions(){
		
		Set<String> set = new HashSet<String>();

		// set here the files that should have be avoided
		set.add("jar");
		set.add("mwb");
		set.add("bak");
		set.add("jpg");
		set.add("jpeg");
		set.add("png");
		
		return set;
		
	}
	
	// True if AST must be calculated or false otherwise.
	private static boolean isASTCalculable(String filePath){
		int index = filePath.lastIndexOf(".") + 1;
		return sourceExtensions.contains(filePath.substring(index));
	}

	// True if the file should be processed or false otherwise.
	private static boolean isProcessable(String filePath){
		int index = filePath.lastIndexOf(".") + 1;
		return !avoidedExtensions.contains(filePath.substring(index));
	}
	
	// True if the file should be processed or false otherwise.
	private static boolean isAcceptable(String filePath){
		int index = filePath.lastIndexOf(".") + 1;
		return acceptedExtensions.contains(filePath.substring(index));
	}	
	
	/**
	 * 
	 * @param metricsId
	 * @param repoSys
	 * @param repositoryDb
	 * @param entityManager
	 * 
	 * Calculates the metrics for all commits in all trees, except tags.
	 */
	public static void calculate(List<MetricUid> metricsId, IRepositorySystem repoSys,
			RepositoryDB repositoryDb, EntityManager entityManager){
		
		TreeDAO treeDao = new TreeDAOImpl();
		treeDao.setEntityManager(entityManager);
		
		CommitDAO commitDao = new CommitDAOImpl();
		commitDao.setEntityManager(entityManager);
		
		Map<MetricType, Map<MetricUid, IMetric>> metricsMap = MetricConfig.getImplementations(metricsId);
		
		List<TreeDB> treesDb = treeDao.findByRepository(repositoryDb.getId());
		for(TreeDB treeDb : treesDb){
			if(treeDb.getType() == TreeType.BRANCH){
				List<CommitDB> commitsDb = commitDao.findByTree(treeDb.getId());
				calculateMetrics(commitsDb, metricsMap, repoSys, repositoryDb, entityManager);
			}
		}
		
	}
	
	/*
	 * Constructs the repository files state in certain commit.
	 * Prepares AST for source code files.
	 * Calculates the metrics and save their values in database.
	 */
	private static void calculateMetrics(List<CommitDB> commitsDb, Map<MetricType, Map<MetricUid, IMetric>> metricsMap,
			IRepositorySystem repoSys, RepositoryDB repositoryDb, EntityManager entityManager) {

		FileDAO fileDao = new FileDAOImpl();
		fileDao.setEntityManager(entityManager);

		MetricPersistance persistence = new MetricPersistance(entityManager);
		persistence.initBatchPersistence();
		
		Map<File, AST> repositoryFiles = new HashMap<File, AST>();
		Map<File, AST> commitFiles = new HashMap<File, AST>();
		List<Commit> commitsBean = new ArrayList<Commit>(commitsDb.size());
		SaveAST saveAst = new SaveAST(repositoryDb, entityManager);

		for(int i = 0; i < commitsDb.size(); i++){

			commitFiles.clear();
			CommitDB commitDb = commitsDb.get(i);
			Commit commitBean = new Commit(commitDb.getId(), commitDb.getDate(), commitDb.getMessage(), commitDb.getName());
			commitsBean.add(commitBean);

			List<FileDB> filesDbAux = fileDao.findCommitedFiles(commitDb.getId());

			for(FileDB fileDb : filesDbAux){
					
				File fileAux = new File(fileDb.getId(), fileDb.getPath(), fileDb.getUid());
				FileXCommitDB fxcDb = fileDb.getFileXCommits().get(0);
				FileState fs = new FileState(fxcDb.getLinesAdded(), fxcDb.getLinesRemoved(),
						fxcDb.isRemoved());
				fileAux.setFileState(fs);
				
				if(fs.isDeleted()){
					repositoryFiles.remove(fileAux);
					commitFiles.put(fileAux, null);
				}else{
					if(isProcessable(fileAux.getPath())){
						/*
						 *  Absolute path doesn't work correctly, with absolute path JGIT doesn't find the file.
						 *  I save the absolute path, so I need to remove the repository absolute path part.
						 *  index is the start of relative path, transforming "repository_path/file_path" into "file_path".
						 */
						int index = repoSys.getAbsolutePath().length()+1;
						byte[] data = repoSys.getData(commitDb.getName(), fileAux.getPath().substring(index));
						
						if(isASTCalculable(fileAux.getPath())){
							
							AST ast = JavaASTGenerator.generate(fileAux.getPath(), data);
							saveAst.save(fileDb, ast);
							commitFiles.put(fileAux, ast);
							repositoryFiles.put(fileAux, ast);
							
						}else if(isAcceptable(fileAux.getPath())){
							
							Document doc = new Document();
							doc.setName(fileAux.getPath());
							AST ast = new AST();
							
							if(data == null){
								ast.setSourceCode(null);
							}else{
								ast.setSourceCode(new String(data));
							}
							
							ast.setDocument(doc);
							saveAst.save(fileDb, ast);
							commitFiles.put(fileAux, ast);
							repositoryFiles.put(fileAux, ast);
							
						}
						
					}else{
						commitFiles.put(fileAux, null);
						repositoryFiles.put(fileAux, null);
					}

				}// end else
			}// end for(FileDB fileDb : filesDbAux)

			calculationMetricsHelper(metricsMap, commitsBean, commitFiles, repositoryFiles, persistence);
			
		}// end for(CommitDB commitDb : commitsDb)

		
		persistence.flushAllMetricValues();
	}
	
	// Calculates all the selected metrics for a given commit.
	private static void calculationMetricsHelper(Map<MetricType, Map<MetricUid, IMetric>> metricsMap, List<Commit> commits, 
			Map<File, AST> committedFiles, Map<File, AST> repositoryFiles, MetricPersistance persistence){
		
		Commit c = commits.get(commits.size() - 1);
		persistence.setCommit(c);
		
		Map<MetricUid, IMetric> metricMapAux = metricsMap.get(MetricType.SIMPLE);
		for(java.util.Map.Entry<MetricUid, IMetric> entry : metricMapAux.entrySet()){
			persistence.setMetric(entry.getKey());
			entry.getValue().calculate(committedFiles, commits, persistence);
		}
		
		metricMapAux = metricsMap.get(MetricType.COMPLEX);
		for(java.util.Map.Entry<MetricUid, IMetric> entry : metricMapAux.entrySet()){
			persistence.setMetric(entry.getKey());
			entry.getValue().calculate(repositoryFiles, commits, persistence);
		}
		
	}
	
}