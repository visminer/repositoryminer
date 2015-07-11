package br.edu.ufba.softvis.visminer.analyzer;

/**
 * @param <T> Type of that will be returned by interface methods. 
 * @version 0.9
 * @see CommitAnalyzer
 * @see CommitterAnalyzer
 * @see FileAnalyzer
 * @see IssueAnalyzer
 * @see MilestoneAnalyzer
 * @see RepositoryAnalyzer
 * @see TreeAnalyzer
 * 
 * Defines methods to save or to increment repository informations in database.
 */
public interface IAnalyzer<T> {

	/**
	 * @param objects
	 * @return
	 * Defines how to save some information about repository in database.
	 */
	public T persist(Object...objects);
	
	/**
	 * @param objects
	 * @return
	 * Defines how to incremet some information about repository in database.
	 */
	public T increment(Object...objects);
	
}
