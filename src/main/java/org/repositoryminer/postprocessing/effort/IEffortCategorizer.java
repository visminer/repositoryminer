package org.repositoryminer.postprocessing.effort;

import org.repositoryminer.model.effort.Effort;

/**
 * <h1>A categorizer of effort agglomeration</h1>
 * <p>
 * It must decide, based on the mined/calculated effort, a fitting category name
 */
public interface IEffortCategorizer {

	public String getCategory(Effort effort, double minEffort, double maxEffort);

}
