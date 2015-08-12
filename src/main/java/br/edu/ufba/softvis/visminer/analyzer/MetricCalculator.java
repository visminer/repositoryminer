package br.edu.ufba.softvis.visminer.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IRepositorySystem;
import br.edu.ufba.softvis.visminer.annotations.ASTGeneratorAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.generator.ASTGeneratorFactory;
import br.edu.ufba.softvis.visminer.ast.generator.IASTGenerator;
import br.edu.ufba.softvis.visminer.config.MetricConfig;
import br.edu.ufba.softvis.visminer.constant.Language;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.TreeType;
import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.File;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;
import br.edu.ufba.softvis.visminer.persistence.SaveAST;
import br.edu.ufba.softvis.visminer.persistence.dao.TreeDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.TreeDAOImpl;
import br.edu.ufba.softvis.visminer.retriever.CommitRetriever;
import br.edu.ufba.softvis.visminer.retriever.FileRetriever;

/**
 * @version 0.9
 * Manages all the process to calculate the metrics.
 */
public class MetricCalculator{

	private Map<MetricUid, IMetric> commitMetrics;
	private Map<MetricUid, IMetric> snapshotMetrics;
	private SaveAST saveAST;
	private IRepositorySystem repoSys;
	private Map<String, IASTGenerator> astGenerators;
	private List<String> analyzedCommits;
	
	private void createASTGenerators(List<Language> languages){
		astGenerators = new Hashtable<String, IASTGenerator>();
		for(Language lang : languages){
			IASTGenerator astGen = ASTGeneratorFactory.create(lang);
			ASTGeneratorAnnotation annotation = astGen.getClass().getAnnotation(ASTGeneratorAnnotation.class);
			for(String str : annotation.extensions()){
				astGenerators.put(str, astGen);
			}
		}
	}

	public void calculate(List<MetricUid> metrics, IRepositorySystem repoSys, RepositoryDB repoDb, List<Language> languages){
		
		EntityManager entityManager = Database.getInstance().getEntityManager();
		
		TreeDAO treeDao = new TreeDAOImpl();
		treeDao.setEntityManager(entityManager);
		List<TreeDB> treesDb = treeDao.findByRepository(repoDb.getId());
		
		if(treesDb == null){
			entityManager.close();
			return;
		}
	
		createASTGenerators(languages);
		this.repoSys = repoSys;
		
		this.analyzedCommits = new ArrayList<String>();
		this.saveAST = new SaveAST(repoDb, entityManager);
		CommitRetriever commitRetriever = new CommitRetriever();
		
		this.commitMetrics = new LinkedHashMap<MetricUid, IMetric>();
		this.snapshotMetrics = new LinkedHashMap<MetricUid, IMetric>();
		MetricConfig.getImplementations(metrics, commitMetrics, snapshotMetrics);
		
		for(TreeDB treeDb : treesDb){
			if(treeDb.getType() == TreeType.BRANCH){
				List<Commit> commits = commitRetriever.retrieveByTree(treeDb.getId());
				calculateMetrics(commits, entityManager);
			}
		}
		
	}

	private int getNextCommitToAnalysis(List<Commit> commits, List<AST> commitASTs, List<AST> snapshotASTs){
		
		int i = 0;
		for(; i < commits.size(); i++){
			Commit c = commits.get(i);
			if( !analyzedCommits.contains(c.getName()) ){
				analyzedCommits.add(c.getName());
				break;
			}
		}
		
		List<String> commitFiles = new ArrayList<String>();
		List<String> snapshotFiles = this.repoSys.getSnapshotFilesNames(commits.get(i).getName());
		
		for(File file : commits.get(i).getCommitedFiles()){
			commitFiles.add(file.getUid());
		}
		
		for(String uid : snapshotFiles){
			
		}
		
		return i;
		
	}
	
	private void calculateMetrics(List<Commit> commits, EntityManager entityManager) {
		
		FileRetriever fileRetriever = new FileRetriever();
		
		MetricPersistance persistence = new MetricPersistance(entityManager);
		persistence.initBatchPersistence();
		
		Map<File, AST> snapshotFiles = new HashMap<File, AST>();
		Map<File, AST> commitFiles = new HashMap<File, AST>();
		
		
		
	}
	
}