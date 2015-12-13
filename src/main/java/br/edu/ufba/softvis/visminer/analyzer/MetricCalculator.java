package br.edu.ufba.softvis.visminer.analyzer;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.MySQLConnection;
import br.edu.ufba.softvis.visminer.analyzer.scm.SCM;
import br.edu.ufba.softvis.visminer.annotations.ASTGeneratorAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.generator.ASTGeneratorFactory;
import br.edu.ufba.softvis.visminer.ast.generator.IASTGenerator;
import br.edu.ufba.softvis.visminer.config.MetricConfig;
import br.edu.ufba.softvis.visminer.constant.ChangeType;
import br.edu.ufba.softvis.visminer.constant.LanguageType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.File;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;
import br.edu.ufba.softvis.visminer.persistence.ProcessAST;
import br.edu.ufba.softvis.visminer.persistence.dao.FileDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.TreeDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.FileDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.TreeDAOImpl;
import br.edu.ufba.softvis.visminer.retriever.CommitRetriever;

/**
 * Manages all the process to calculate the metrics.
 */
public class MetricCalculator{

  private Map<MetricUid, IMetric> commitMetrics;
  private Map<MetricUid, IMetric> snapshotMetrics;
  private ProcessAST processAST;
  private SCM repoSys;
  private Map<String, IASTGenerator> astGenerators;
  private Set<String> analyzedCommits;
  private EntityManager entityManager;
  private List<String> sourceFolders;
  private MetricPersistance persistence;

  /*
   *  this is used to solve a bug in eclipselink, records from file table that has null path are 
   *  re fetched and their id and path are put in the map.
   */
  private Map<Integer, File> tempFilesPath;

  /**
   * @param metrics
   * @param repoSys
   * @param repoDb
   * @param languages
   * 
   * Calculates the metrics for the target languages.
   */
  public void calculate(List<MetricUid> metrics, SCM repoSys, 
      RepositoryDB repoDb, List<LanguageType> languages){

    entityManager = Database.getInstance().getEntityManager();
    tempFilesPath = new HashMap<Integer, File>();

    TreeDAO treeDao = new TreeDAOImpl();
    treeDao.setEntityManager(entityManager);

    List<TreeDB> treesDb = treeDao.findByRepository(repoDb.getId());

    if(treesDb == null){
      entityManager.close();
      return;
    }

    createASTGenerators(languages);

    this.repoSys = repoSys;
    this.sourceFolders = new ArrayList<String>();
    this.analyzedCommits = new HashSet<String>();
    this.processAST = new ProcessAST(repoDb, entityManager);
    CommitRetriever commitRetriever = new CommitRetriever();

    this.commitMetrics = new LinkedHashMap<MetricUid, IMetric>();
    this.snapshotMetrics = new LinkedHashMap<MetricUid, IMetric>();
    this.persistence = new MetricPersistance(entityManager, 
        repoDb.getUid());

    MetricConfig.getImplementations(metrics, commitMetrics, 
        snapshotMetrics);

    for(TreeDB treeDb : treesDb){
      List<Commit> commits = commitRetriever.retrieveByTree(
          treeDb.getId());
      calculateMetrics(commits);
    }

    entityManager.clear();
    entityManager.close();

  }

  /* This is the main method in metric calculation, it manages all 
	   other process. */
  private void calculateMetrics(List<Commit> commits) {

    List<String> snapshotFilesUids = new ArrayList<String>();
    List<File> commitFiles = new ArrayList<File>();

    int nextCommit = getNextCommitToAnalysis(commits, commitFiles, 
        snapshotFilesUids);

    // No commits to analyze
    if(nextCommit == commits.size()){
      return;
    }

    FileDAO dao = new FileDAOImpl();
    dao.setEntityManager(entityManager);
    List<FileDB> snapshotFiles = dao.getFilesByUids(snapshotFilesUids);

    Map<String, AST> commitASTs = new LinkedHashMap<String, AST>();
    Map<String, AST> snapshotASTs = new LinkedHashMap<String, AST>();

    repoSys.checkout(commits.get(nextCommit).getName());
    initASTs(commitASTs, snapshotASTs, commitFiles, snapshotFiles, 
        commits.get(nextCommit).getName());

    processAST.flushSoftwareUnits(commits.get(nextCommit).getId());
    calculationMetricsHelper(commitMetrics, snapshotMetrics, 
        commits.subList(0, nextCommit+1),
        commitASTs, snapshotASTs);

    for(int i = nextCommit+1; i < commits.size(); i++){

      commitASTs.clear();
      analyzedCommits.add(commits.get(i).getName());

      Commit commit = commits.get(i);
      repoSys.checkout(commit.getName());
      sourceFolders = new ArrayList<String>();
      getSourceFolders(repoSys.getAbsolutePath());

      for(File file : commit.getCommitedFiles()){

        if(file.getFileState().getChange() == ChangeType.DELETE){

          AST ast = snapshotASTs.remove(file.getPath());
          if(ast != null)
            processAST.process(file, ast, true);

        }else{

          verifyPath(file);
          AST ast = createAST(file, commit.getName());
          if(ast != null){
            snapshotASTs.put(file.getPath(), ast);
            commitASTs.put(file.getPath(), ast);
          }

        }

      }

      processAST.flushSoftwareUnits(commit.getId());
      calculationMetricsHelper(commitMetrics, snapshotMetrics, 
          commits.subList(0, i+1), commitASTs, snapshotASTs);
      persistence.flushAllMetricValues();

    }

  }	

  /*
  /*
   * Verifies if field path in file is null, if it's null, get from database set field path and
   * put value into tempFilesPath map.
   */
  // FIXME: This is not good and will be change soon
  private void verifyPath(File file) {

    if(file.getPath() != null) return;

    File f = tempFilesPath.get(file.getId());
    if(f == null){

      FileDB fDb = MySQLConnection.getInstance().findFileById(file.getId());
      
      file.setPath(fDb.getPath());
      file.setUid(fDb.getUid());
      tempFilesPath.put(file.getId(), file);

    }else{
      file.setPath(f.getPath());
      file.setUid(f.getUid());
    }

  }

  /* Initializes astGenerators, the map is filled with extensions and ast generators. */ 
  private void createASTGenerators(List<LanguageType> languages){

    astGenerators = new Hashtable<String, IASTGenerator>();
    for(LanguageType lang : languages){
      
      IASTGenerator astGen = ASTGeneratorFactory.create(lang);
      ASTGeneratorAnnotation annotation = astGen.getClass().
          getAnnotation(ASTGeneratorAnnotation.class);
      
      for(String str : annotation.extensions()){
        astGenerators.put(str, astGen);
      }
      
    }

  }	

  /* Return the next commit that should be processed, commits already 
   * processed are stored to avoid overhead */
  private int getNextCommitToAnalysis(List<Commit> commits, List<File>
  commitFiles, List<String> snapshotFilesUids){

    int i = 0;
    for(; i < commits.size(); i++){
      Commit c = commits.get(i);
      if( !analyzedCommits.contains(c.getName()) ){
        analyzedCommits.add(c.getName());
        break;
      }
    }

    if(i == commits.size()){
      return i;
    }

    commitFiles.addAll(commits.get(i).getCommitedFiles());
    snapshotFilesUids.addAll(repoSys.getRepositoryFiles(
        commits.get(i).getName()));
    return i;

  }

  // Initializes the ASTs maps to next commit that should be processed
  private void initASTs(Map<String, AST> commitASTs, Map<String, AST> 
  snapshotASTs, List<File> commitFiles,
  List<FileDB> snapshotFiles, String commitName){

    for(FileDB fileDb : snapshotFiles){

      File f = fileDb.toBusiness();
      AST ast = createAST(f, commitName);

      if(ast == null)
        continue;

      snapshotASTs.put(f.getPath(), ast);
      if(commitFiles.contains(f))
        commitASTs.put(f.getPath(), ast);

    }
  }

  // Creates the AST for the given file and save their software units
  private AST createAST(File file, String commitName){

    int index = file.getPath().lastIndexOf(".") + 1;
    String ext = file.getPath().substring(index);
    IASTGenerator gen = astGenerators.get(ext);

    if(gen == null){
      return null;
    }

    String source = repoSys.getSource(commitName, file.getPath());
    AST ast = gen.generate(file.getPath(), source, sourceFolders.
        toArray(new String[sourceFolders.size()]));
    processAST.process(file, ast, false);

    return ast;

  }

  // Simple helper to calculate the metrics
  private void calculationMetricsHelper(Map<MetricUid, IMetric> commitMetrics,
      Map<MetricUid, IMetric> snapshotMetrics,
      List<Commit> commits, Map<String, AST> commitASTs, 
      Map<String, AST> snapshotASTs){

    List<AST> commitsASTsList = new ArrayList<AST>(commitASTs.values());
    List<AST> snapshotASTsList = new ArrayList<AST>(snapshotASTs.values());

    Commit c = commits.get(commits.size() - 1);
    persistence.setCommitId(c.getId());

    for(Entry<MetricUid, IMetric> entry : commitMetrics.entrySet()){
      persistence.setMetric(entry.getKey());
      entry.getValue().calculate(commitsASTsList, commits, persistence);
    }

    for(Entry<MetricUid, IMetric> entry : snapshotMetrics.entrySet()){
      persistence.setMetric(entry.getKey());
      entry.getValue().calculate(snapshotASTsList, commits, persistence);
    }

  }	

  private void getSourceFolders(String repoPath){

    java.io.File directory = new java.io.File(repoPath);

    java.io.File[] fList = directory.listFiles(new FileFilter() {
      public boolean accept(java.io.File file) {
        return file.isDirectory() && !file.isHidden();
      }
    });

    for (java.io.File file : fList) {	      
      if(validateSourceFolder(file)){
        sourceFolders.add(file.getAbsolutePath());
        getSourceFolders(file.getAbsolutePath());
      }	        
    }
  }

  private boolean validateSourceFolder(java.io.File f){

    java.io.File[] fList = f.listFiles(new FileFilter() {
      public boolean accept(java.io.File file) {
        return (file.isDirectory() && !file.isHidden()) || 
            file.getName().endsWith(".java");
      }
    });

    if(fList == null)
      return false;

    for(java.io.File f2 : fList){
      if(f2.getName().endsWith(".java")){
        return true;
      }else 			
        if(validateSourceFolder(f2)){
          return true;
        }		
    }	
    return false;
  }

}