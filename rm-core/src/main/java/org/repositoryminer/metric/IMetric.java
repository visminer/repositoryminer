package org.repositoryminer.metric;

import org.repositoryminer.ast.AST;

public interface IMetric {

	/**
	 * Calculates the metric and stores the result in the AST.
	 * 
	 * @param ast
	 */
	public void calculate(AST ast);

	/**
	 * @return the metric identifier.
	 */
	public MetricID getId();

	/**
	 * @return the required metrics.
	 */
	public MetricID[] getRequiredMetrics();

}