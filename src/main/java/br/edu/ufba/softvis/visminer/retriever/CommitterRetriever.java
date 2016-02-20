package br.edu.ufba.softvis.visminer.retriever;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.Committer;
import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitterDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.relational.CommitterDAOImpl;

public class CommitterRetriever extends Retriever {

	public List<Committer> findCommitters(int repositoryId) {

		CommitterDAO committerDAO = new CommitterDAOImpl();
		super.createEntityManager();
		super.shareEntityManager(committerDAO);

		List<CommitterDB> committersDb = committerDAO
				.findByRepository(repositoryId);

		super.closeEntityManager();
		return CommitterDB.toBusiness(committersDb);
		
	}

}
