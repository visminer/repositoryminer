package org.repositoryminer.metrics.codemetric;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.ProjectReport;

public abstract class CodeMetric {

	protected CodeMetricId id;
	protected CodeMetricId[] requiredMetrics;

	/**
	 * Calculates the metric and stores the result in the AST.
	 * 
	 * @param ast
	 */
	public abstract void calculate(AST ast, FileReport fileReport, ProjectReport projectReport);

	/**
	 * Performs any final work after process all the ASTs. This method can be
	 * interesting for some metrics that needs informations about all the classes
	 * before perform any calculus.
	 */
	public abstract void clean(ProjectReport projectReport);

	/**
	 * @return the metric identifier.
	 */
	public CodeMetricId getId() {
		return id;
	}

	/**
	 * @return the required metrics.
	 */
	public CodeMetricId[] getRequiredMetrics() {
		return requiredMetrics;
	}

}