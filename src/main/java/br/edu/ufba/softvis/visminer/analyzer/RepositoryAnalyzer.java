package br.edu.ufba.softvis.visminer.analyzer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.management.openmbean.KeyAlreadyExistsException;

import br.edu.ufba.softvis.visminer.analyzer.scm.SCM;
import br.edu.ufba.softvis.visminer.analyzer.scm.SCMFactory;
import br.edu.ufba.softvis.visminer.antipattern.IAntiPattern;
import br.edu.ufba.softvis.visminer.constant.LanguageType;
import br.edu.ufba.softvis.visminer.constant.SCMType;
import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.model.Commit;
import br.edu.ufba.softvis.visminer.model.File;
import br.edu.ufba.softvis.visminer.model.Repository;
import br.edu.ufba.softvis.visminer.model.Tree;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.TreeDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.RepositoryDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.TreeDAOImpl;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

public class RepositoryAnalyzer {

	private static final String EXCEPTION_MESSAGE = "Repository already exists in database";

	public void persist(Repository repository, SCMType type, List<IMetric> metrics, List<IAntiPattern> antiPatterns, List<LanguageType> languages) {
		SCM repoSys = SCMFactory.getRepository(type);
		repoSys.open(repository.getPath());

		RepositoryDAO repositoryDao = new RepositoryDAOImpl();

		String path = repoSys.getAbsolutePath();
		if (repositoryDao.hasRepository(path)) {
			throw new KeyAlreadyExistsException(EXCEPTION_MESSAGE);
		}

		String uid = StringUtils.sha1(path);
		repository.setUid(uid);

		// saving repo docs
		repositoryDao.save(repository);

		List<Commit> commits = sortCommits(repoSys.getCommits());
		saveCommitedFiles(commits, repository, repoSys);
		saveTrees(commits, repository, repoSys);

		// saving repo+tree+commit docs
		repositoryDao.save(repository);
		
		if ((metrics != null) && (metrics.size() > 0) && (antiPatterns != null) && (antiPatterns.size() > 0) ) {
			SourceAnalyzer sourceAnalyzer = new SourceAnalyzer();
			sourceAnalyzer.analyze(repository, metrics, antiPatterns, repoSys, languages);
		}
		
		//

		repoSys.reset();
		repoSys.close();
	}

	private List<Commit> sortCommits(List<Commit> commits) {
		Collections.sort(commits, new Comparator<Commit>() {

			@Override
			public int compare(Commit o1, Commit o2) {
				return o1.getDate().compareTo(o2.getDate());
			}

		});

		return commits;
	}

	private void saveCommitedFiles(List<Commit> commits, Repository repository, SCM repoSys) {
		CommitDAO dao = new CommitDAOImpl();
		
		for (Commit commit : commits) {
			commit.setRepository(repository);
			List<File> files = repoSys.getCommitedFiles(commit);
			commit.setCommitedFiles(files);
			
			dao.save(commit);
		}
		
		repository.setCommits(commits);
	}

	private void saveTrees(List<Commit> commits, Repository repository, SCM repoSys) {
		TreeDAO dao = new TreeDAOImpl();
		
		List<Tree> trees = repoSys.getTrees();
		for (Tree tree : trees) {
			tree.setRepository(repository);
			tree.setCommits(repoSys.getCommitsByTree(tree.getFullName(), tree.getType()));
			
			dao.save(tree);
		}

		repository.setTrees(trees);
	}

}
