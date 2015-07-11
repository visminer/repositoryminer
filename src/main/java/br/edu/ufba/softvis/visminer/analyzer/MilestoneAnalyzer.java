package br.edu.ufba.softvis.visminer.analyzer;

import br.edu.ufba.softvis.visminer.model.database.MilestoneDB;

/**
 * @version 0.9
 * @see CommitAnalyzer
 * @see CommitterAnalyzer
 * @see FileAnalyzer
 * @see IssueAnalyzer
 * @see RepositoryAnalyzer
 * @see TreeAnalyzer
 * @see IAnalyzer
 * 
 * Defines how to save or to increment informations about milestones in database
 */
public class MilestoneAnalyzer implements IAnalyzer<MilestoneDB>{

	@Override
	public MilestoneDB persist(Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MilestoneDB increment(Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

}
