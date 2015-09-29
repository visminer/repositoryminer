package br.edu.ufba.softvis.visminer.retriever;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufba.softvis.visminer.model.business.Committer;
import br.edu.ufba.softvis.visminer.model.business.Project;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.Tree;
import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitterDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.TreeDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitterDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.RepositoryDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.TreeDAOImpl;

public class RepositoryRetriever extends Retriever {

	private static final String MASTER_TREE = "master";
	private static final String HEAD_TREE = "head";

	private List<Committer> findCommitters(int repositoryId) {
		
		List<Committer> committers = new ArrayList<Committer>();
		CommitterDAO committerDAO = new CommitterDAOImpl();
		super.shareEntityManager(committerDAO);

		List<CommitterDB> committersDb = committerDAO
				.findByRepository(repositoryId);
		if (committersDb != null) {
			committers.addAll(CommitterDB.toBusiness(committersDb));
		}

		return committers;
	}

	private List<Tree> findTrees(int repositoryId) {
		
		List<Tree> trees = new ArrayList<Tree>();
		TreeDAO treeDAO = new TreeDAOImpl();
		super.shareEntityManager(treeDAO);

		List<TreeDB> treesDb = treeDAO.findByRepository(repositoryId);
		if (treesDb != null) {
			trees.addAll(TreeDB.toBusiness(treesDb));
		}

		return trees;
	}

	private Repository addTreesAndCommitters(Repository repository) {
		
		List<Tree> trees = findTrees(repository.getId());
		List<Committer> committers = findCommitters(repository.getId());

		repository.setCommitters(committers);
		repository.setTrees(trees);

		Project project = new Project(repository);
		for (Tree t : trees) {
			String s = t.getName().toLowerCase();
			if (s.equals(MASTER_TREE) || s.equals(HEAD_TREE)) {
				project.setCurrentTree(t);
			}
		}

		if (project.getCurrentTree() == null) {
			project.setCurrentTree(trees.get(0));
		}

		repository.setProject(project);
		return repository;
	}

	public List<Repository> retrieveAll() {
		
		List<Repository> repositories = new ArrayList<Repository>();
		RepositoryDAO dao = new RepositoryDAOImpl();

		super.createEntityManager();
		super.shareEntityManager(dao);
		
		List<RepositoryDB> reposDB = (List<RepositoryDB>) dao.findAll();
		if (reposDB != null) {
			repositories.addAll(RepositoryDB.toBusiness(reposDB));
			for (Repository repository : repositories) {
				addTreesAndCommitters(repository);
			}
		}

		super.closeEntityManager();
		return repositories;
	}

	public Repository retrieveById(int id) {
		
		Repository repository = null;
		RepositoryDAO dao = new RepositoryDAOImpl();
		
		super.createEntityManager();
		super.shareEntityManager(dao);

		RepositoryDB repoDB = dao.find(id);
		if (repoDB != null) {
			repository = addTreesAndCommitters(repoDB.toBusiness());
		}

		super.closeEntityManager();
		return repository;
	}

	public Repository retrieveByPath(String path) {
		
		Repository repository = null;
		RepositoryDAO dao = new RepositoryDAOImpl();
		
		super.createEntityManager();
		super.shareEntityManager(dao);
		
		RepositoryDB repoDB = dao.findByPath(path);
		if (repoDB != null) {
			repository = addTreesAndCommitters(repoDB.toBusiness());
		}

		super.closeEntityManager();
		return repository;
	}
	
	public boolean checkIfExists(String repositoryPath){

		RepositoryDAO dao = new RepositoryDAOImpl();

		super.createEntityManager();
		super.shareEntityManager(dao);

		boolean result = dao.hasRepository(repositoryPath);

		super.closeEntityManager();
		return result;

	}

}