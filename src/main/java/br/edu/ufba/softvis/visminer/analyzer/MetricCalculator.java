package br.edu.ufba.softvis.visminer.analyzer;

import java.io.UnsupportedEncodingException;
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
import br.edu.ufba.softvis.visminer.ast.generator.ASTGeneratorFactory;
import br.edu.ufba.softvis.visminer.ast.generator.Language;
import br.edu.ufba.softvis.visminer.config.MetricConfig;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.TreeType;
import br.edu.ufba.softvis.visminer.metric.IMetric;
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
 * @version 0.9 Manages all the process to calculate the metrics.
 */
public class MetricCalculator {

	private static final Set<String> sourceExtensions = initSourceExtensions();
	private static final Set<String> acceptedExtensions = initAcceptedExtensions();
	private static final Set<String> avoidedExtensions = initAvoidedExtensions();

	// List of file files extension that should have their AST calculated.
	private static Set<String> initSourceExtensions() {

		Set<String> set = new HashSet<String>();

		// set here the files that should have their ast calculated
		set.add("java");
		set.add("h");
		set.add("cpp");
		set.add("hpp");

		return set;
	}

	// List of files extensions that will have their content accessed, but they
	// won't have any AST calculated.
	private static Set<String> initAcceptedExtensions() {

		Set<String> set = new HashSet<String>();
		set.add("txt");
		set.add("md");
		set.add("xml");
		set.add("json");
		set.add("html");
		set.add("css");

		return set;
	}

	// List of files extensions that should be avoided.
	private static Set<String> initAvoidedExtensions() {

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
	private static boolean isASTCalculable(String filePath) {
		int index = filePath.lastIndexOf(".") + 1;
		return sourceExtensions.contains(filePath.substring(index));
	}

	// True if the file should be processed or false otherwise.
	private static boolean isProcessable(String filePath) {
		int index = filePath.lastIndexOf(".") + 1;
		return !avoidedExtensions.contains(filePath.substring(index));
	}

	// True if the file should be processed or false otherwise.
	private static boolean isAcceptable(String filePath) {
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
	 *            Calculates the metrics for all commits in all trees, except
	 *            tags.
	 */
	public static void calculate(List<MetricUid> metricsId,
			IRepositorySystem repoSys, RepositoryDB repositoryDb,
			EntityManager entityManager, Language language) {

		TreeDAO treeDao = new TreeDAOImpl();
		treeDao.setEntityManager(entityManager);

		CommitDAO commitDao = new CommitDAOImpl();
		commitDao.setEntityManager(entityManager);

		Map<MetricType, Map<MetricUid, IMetric>> metricsMap = MetricConfig
				.getImplementations(metricsId);
		SaveAST saveAst = new SaveAST(repositoryDb, entityManager);

		List<TreeDB> treesDb = treeDao.findByRepository(repositoryDb.getId());
		for (TreeDB treeDb : treesDb) {
			if (treeDb.getType() == TreeType.BRANCH) {
				List<CommitDB> commitsDb = commitDao.findByTree(treeDb.getId());
				calculateMetrics(commitsDb, metricsMap, language, saveAst,
						repoSys, repositoryDb, entityManager);
			}
		}

	}

	/*
	 * Constructs the repository files state in certain commit. Prepares AST for
	 * source code files. Calculates the metrics and save their values in
	 * database.
	 */
	private static void calculateMetrics(List<CommitDB> commitsDb,
			Map<MetricType, Map<MetricUid, IMetric>> metricsMap,
			Language language, SaveAST saveAst, IRepositorySystem repoSys,
			RepositoryDB repositoryDb, EntityManager entityManager) {

		FileDAO fileDao = new FileDAOImpl();
		fileDao.setEntityManager(entityManager);

		MetricPersistance persistence = new MetricPersistance(entityManager);
		persistence.initBatchPersistence();

		Map<FileDB, AST> repositoryFiles = new HashMap<FileDB, AST>();
		Map<FileDB, AST> commitFiles = new HashMap<FileDB, AST>();
		List<CommitDB> commitsDbAux = new ArrayList<CommitDB>(commitsDb.size());

		for (int i = 0; i < commitsDb.size(); i++) {

			commitFiles.clear();
			commitsDbAux.add(commitsDb.get(i));

			CommitDB commitDb = commitsDb.get(i);
			List<FileDB> filesDbAux = fileDao.findCommitedFiles(commitDb
					.getId());

			for (FileDB fileDb : filesDbAux) {

				FileXCommitDB fxcDb = fileDb.getFileXCommits().get(0);

				if (fxcDb.isRemoved()) {
					repositoryFiles.remove(fileDb);
					commitFiles.put(fileDb, null);
				} else {
					if (isProcessable(fileDb.getPath())) {
						/*
						 * Absolute path doesn't work correctly, with absolute
						 * path JGIT doesn't find the file. I save the absolute
						 * path, so I need to remove the repository absolute
						 * path part. index is the start of relative path,
						 * transforming "repository_path/file_path" into
						 * "file_path".
						 */
						int index = repoSys.getAbsolutePath().length() + 1;
						byte[] data = repoSys.getData(commitDb.getName(),
								fileDb.getPath().substring(index));

						if (isASTCalculable(fileDb.getPath())) {
							AST ast = ASTGeneratorFactory.create(language)
									.generate(fileDb.getPath(), data,
											repositoryDb.getCharset());
							saveAst.save(fileDb, ast);
							commitFiles.put(fileDb, ast);
							repositoryFiles.put(fileDb, ast);

						} else if (isAcceptable(fileDb.getPath())) {

							Document doc = new Document();
							doc.setName(fileDb.getPath());
							AST ast = new AST();

							if (data == null) {
								ast.setSourceCode(null);
							} else {
								try {
									ast.setSourceCode(new String(data,
											repositoryDb.getCharset()));
								} catch (UnsupportedEncodingException e) {
									e.getMessage();
									System.exit(1);
								}
							}

							ast.setDocument(doc);
							saveAst.save(fileDb, ast);
							commitFiles.put(fileDb, ast);
							repositoryFiles.put(fileDb, ast);

						}

					} else {
						commitFiles.put(fileDb, null);
						repositoryFiles.put(fileDb, null);
					}

				}// end else
			}// end for(FileDB fileDb : filesDbAux)

			calculationMetricsHelper(metricsMap, commitsDbAux, commitFiles,
					repositoryFiles, persistence);

		}// end for(CommitDB commitDb : commitsDb)

		persistence.flushAllMetricValues();
	}

	// Calculates all the selected metrics for a given commit.
	private static void calculationMetricsHelper(
			Map<MetricType, Map<MetricUid, IMetric>> metricsMap,
			List<CommitDB> commits, Map<FileDB, AST> committedFiles,
			Map<FileDB, AST> repositoryFiles, MetricPersistance persistence) {

		CommitDB c = commits.get(commits.size() - 1);
		persistence.setCommitId(c.getId());

		Map<MetricUid, IMetric> metricMapAux = metricsMap
				.get(MetricType.SIMPLE);
		for (java.util.Map.Entry<MetricUid, IMetric> entry : metricMapAux
				.entrySet()) {
			persistence.setMetric(entry.getKey());
			entry.getValue().calculate(committedFiles, commits, persistence);
		}

		metricMapAux = metricsMap.get(MetricType.COMPLEX);
		for (java.util.Map.Entry<MetricUid, IMetric> entry : metricMapAux
				.entrySet()) {
			persistence.setMetric(entry.getKey());
			entry.getValue().calculate(repositoryFiles, commits, persistence);
		}

	}

}