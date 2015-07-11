package br.edu.ufba.softvis.visminer.analyzer;

import br.edu.ufba.softvis.visminer.model.database.IssueDB;

/**
 * @version 0.9
 * @see CommitAnalyzer
 * @see CommitterAnalyzer
 * @see FileAnalyzer
 * @see MilestoneAnalyzer
 * @see RepositoryAnalyzer
 * @see TreeAnalyzer
 * @see IAnalyzer
 * 
 * Defines how to save or to increment informations about issues in database.
 */
public class IssueAnalyzer implements IAnalyzer<IssueDB>{

	@Override
	public IssueDB persist(Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IssueDB increment(Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

}
