package br.edu.ufba.softvis.visminer.retriever;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.Tree;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.persistence.dao.TreeDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.relational.TreeDAOImpl;

public class TreeRetriever extends Retriever {

	public List<Tree> findTrees(int repositoryId) {

		TreeDAO treeDAO = new TreeDAOImpl();
		super.createEntityManager();
		super.shareEntityManager(treeDAO);

		List<TreeDB> treesDb = treeDAO.findByRepository(repositoryId);
		super.closeEntityManager();
		return TreeDB.toBusiness(treesDb);
	}

}
