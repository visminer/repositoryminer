package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.CommitReferenceIssueDB;
import br.edu.ufba.softvis.visminer.model.database.CommitReferenceIssuePK;

public interface CommitReferenceIssueDAO extends DAO<CommitReferenceIssueDB, CommitReferenceIssuePK>{

	public List<CommitReferenceIssueDB> findByCommit(int commitId);
	
}
