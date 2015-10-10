package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.CommitReferenceIssueDB;
import br.edu.ufba.softvis.visminer.model.database.CommitReferenceIssuePK;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitReferenceIssueDAO;

@SuppressWarnings("unchecked")
public class CommitReferenceIssueDAOImpl extends DAOImpl<CommitReferenceIssueDB, CommitReferenceIssuePK> implements CommitReferenceIssueDAO{

	@Override
	public List<CommitReferenceIssueDB> findByCommit(int commitId) {
		
		EntityManager em = getEntityManager();
		TypedQuery<CommitReferenceIssueDB> query = em.createNamedQuery("CommitReferenceIssueDB.findByCommit", CommitReferenceIssueDB.class);
		query.setParameter("commitId", commitId);
		return (List<CommitReferenceIssueDB>) getResultList(query);
		
	}

}
