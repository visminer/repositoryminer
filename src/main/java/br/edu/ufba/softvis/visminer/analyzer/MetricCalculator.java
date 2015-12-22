package br.edu.ufba.softvis.visminer.analyzer;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import br.edu.ufba.softvis.visminer.analyzer.scm.SCM;
import br.edu.ufba.softvis.visminer.annotations.ASTGeneratorAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.generator.ASTGeneratorFactory;
import br.edu.ufba.softvis.visminer.ast.generator.IASTGenerator;
import br.edu.ufba.softvis.visminer.constant.LanguageType;
import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.model.Commit;
import br.edu.ufba.softvis.visminer.model.File;
import br.edu.ufba.softvis.visminer.model.Repository;
import br.edu.ufba.softvis.visminer.persistence.ASTProcessor;

/**
 * Manages the metrics calculation
 */
public class MetricCalculator {
	private ASTProcessor astProcessor;
	private SCM repoSys;

	private List<String> sourceFolders;
	private Map<String, IASTGenerator> astGenerators;

	/**
	 * @param repository
	 * @param metrics
	 * @param repoSys
	 * @param languages
	 */
	public void calculate(Repository repository, List<IMetric> metrics, SCM repoSys, List<LanguageType> languages) {
		this.repoSys = repoSys;
		this.astProcessor = new ASTProcessor(repository);

		getSourceFolders(repoSys.getAbsolutePath());

		createASTGenerators(languages);

		List<Commit> commits = repository.getCommits();
		if (commits != null) {
			calculateMetrics(commits, metrics);
		}
	}

	private void calculateMetrics(List<Commit> commits, List<IMetric> metrics) {
		int c = 1;
		for (Commit commit : commits) {
			System.out.println(c + " de " + commits.size());
			c++;

			repoSys.checkout(commit.getName());
			for (File file : commit.getCommitedFiles()) {
				processAST(file, commit.getName(), metrics);
			}
		}
	}

	private void createASTGenerators(List<LanguageType> languages) {
		astGenerators = new Hashtable<String, IASTGenerator>();

		for (LanguageType lang : languages) {
			IASTGenerator astGen = ASTGeneratorFactory.create(lang);
			ASTGeneratorAnnotation annotation = astGen.getClass().getAnnotation(ASTGeneratorAnnotation.class);
			for (String str : annotation.extensions()) {
				astGenerators.put(str, astGen);
			}
		}
	}

	private void processAST(File file, String commitName, List<IMetric> metrics) {
		int index = file.getPath().lastIndexOf(".") + 1;
		String ext = file.getPath().substring(index);

		IASTGenerator gen = astGenerators.get(ext);
		if (gen == null) {
			return;
		}

		try {
			String source = repoSys.getSource(commitName, file.getPath());
			AST ast = gen.generate(file.getPath(), source, sourceFolders.toArray(new String[sourceFolders.size()]));
			astProcessor.process(file, ast, metrics);
		} catch (Exception e) {
			// TODO tratar no altonivel (segue ou para?)
			System.out.println("Erro processando: " + file.getPath() + 
					". Mensagem de erro: " + e.getMessage());
		}
	}

	private void getSourceFolders(String repoPath) {
		sourceFolders = new ArrayList<String>();
		java.io.File directory = new java.io.File(repoPath);

		java.io.File[] fList = directory.listFiles(new FileFilter() {
			public boolean accept(java.io.File file) {
				return file.isDirectory() && !file.isHidden();
			}
		});

		for (java.io.File file : fList) {
			if (validateSourceFolder(file)) {
				sourceFolders.add(file.getAbsolutePath());
				getSourceFolders(file.getAbsolutePath());
			}
		}
	}

	private boolean validateSourceFolder(java.io.File f) {
		java.io.File[] fList = f.listFiles(new FileFilter() {
			public boolean accept(java.io.File file) {
				return (file.isDirectory() && !file.isHidden()) || file.getName().endsWith(".java");
			}
		});

		if (fList == null)
			return false;

		for (java.io.File f2 : fList) {
			if (f2.getName().endsWith(".java")) {
				return true;
			} else if (validateSourceFolder(f2)) {
				return true;
			}
		}
		return false;
	}

}
