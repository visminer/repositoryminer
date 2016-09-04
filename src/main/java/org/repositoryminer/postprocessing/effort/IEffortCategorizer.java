package org.repositoryminer.postprocessing.effort;

import org.repositoryminer.model.effort.Effort;

/**
 * <h1>A categorizer of effort agglomeration</h1>
 * <p>
 * Based on the mined/calculated effort, it must decide a fitting category name.
 * Few examples of category names are: MINOR, MAJOR, SUB-MAJOR and others.
 * <p>
 * Minimal and maximal thresholds of efforts are provided to adequately situate
 * an effort within the targeted categories.
 */
public interface IEffortCategorizer {

	/**
	 * Decides which category the effort belongs to
	 * 
	 * @param effort
	 *            a candidate effort that must fit in a category
	 * @param minEffort
	 *            the minimal calculated effort of the whole set of efforts
	 *            being categorized
	 * @param maxEffort
	 *            the maximal calculated effort of the whole set of efforts
	 *            being categorized
	 * @return
	 */
	public String getCategory(Effort effort, double minEffort, double maxEffort);

}
