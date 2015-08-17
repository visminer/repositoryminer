package br.edu.ufba.softvis.visminer.analyzer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IRepositorySystem;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.persistence.dao.TreeDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.TreeDAOImpl;

/**
 * @version 0.9
 * Defines how to save or to increment informations about trees in repository.
 */
public class TreeAnalyzer{

	public Void persist(List<CommitDB> commitsDb, RepositoryDB repositoryDb, IRepositorySystem repoSys,
			EntityManager entityManager) {

		TreeDAO treeDao = new TreeDAOImpl();
		treeDao.setEntityManager(entityManager);
		
		List<TreeDB> treesDb = new ArrayList<TreeDB>();
		
		CommitDB commitDb = new CommitDB();
		
		for(TreeDB tree : repoSys.getTrees()){
			tree.setRepository(repositoryDb);
			tree.setCommits(new ArrayList<CommitDB>());
			
			for(CommitDB commit : repoSys.getCommitsByTree(tree.getFullName(), tree.getType())){
				commitDb.setName(commit.getName());
				int index = commitsDb.indexOf(commitDb);
				tree.getCommits().add(commitsDb.get(index));
			}
			
			treesDb.add(tree);
		}
		
		treeDao.batchSave(treesDb);
		return null;
		
	}

}