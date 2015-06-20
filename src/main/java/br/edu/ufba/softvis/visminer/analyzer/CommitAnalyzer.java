package br.edu.ufba.softvis.visminer.analyzer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IRepositorySystem;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitDAOImpl;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * @see CommitterAnalyzer
 * @see FileAnalyzer
 * @see IssueAnalyzer
 * @see MilestoneAnalyzer
 * @see RepositoryAnalyzer
 * @see TreeAnalyzer
 * @see IAnalyzer
 * 
 * Defines how to save or to increment informations about commits in database.
 */
public class CommitAnalyzer implements IAnalyzer<List<CommitDB>>{

	@SuppressWarnings("unchecked")
	@Override
	public List<CommitDB> persist(Object... objects) {

		List<CommitterDB> committersDb = (List<CommitterDB>) objects[0];
		IRepositorySystem repoSys = (IRepositorySystem) objects[1];
		EntityManager entityManager = (EntityManager) objects[2];
		
		List<CommitDB> commitsDb = new ArrayList<CommitDB>();
		
		for(CommitterDB committerDb : committersDb){
			List<Commit> commits = repoSys.getCommitsByCommitter(committerDb.getEmail());
			for(Commit commit : commits){
				CommitDB commitDb = new CommitDB(0, commit.getDate(), commit.getMessage(), commit.getName());
				commitDb.setCommitter(committerDb);
				commitsDb.add(commitDb);
			}
		}
		
		CommitDAO commitDao = new CommitDAOImpl();
		commitDao.setEntityManager(entityManager);
		commitDao.batchSave(commitsDb);
		return commitsDb;
		
	}

	@Override
	public List<CommitDB> increment(Object... objects) {
		return null;
	}

}
