package org.repositoryminer.metrics.codesmell;

import java.util.Map;

import org.repositoryminer.metrics.codemetric.CodeMetricId;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.ProjectReport;

public abstract class CodeSmell {

	protected CodeSmellId id;
	protected Map<String, Number> thresholds;
	protected CodeMetricId[] requiredMetrics;
	protected CodeSmellId[] requiredCodesmells;
	
	/**
	 * Detects the code smell and stores the result in the ast.
	 * 
	 * @param ast
	 */
	public abstract void detect(FileReport fileReport, ProjectReport projectReport);

	/**
	 * @return the code smell identifier.
	 */
	public CodeSmellId getId() {
		return id;
	}

	/**
	 * A code smell/code metric id name should be used as key and the threshold as the value.
	 * @return a map containing the thresholds values used by the code smell. 
	 */
	public Map<String, Number> getThresholds() {
		return thresholds;
	}

	/**
	 * @return the required metrics.
	 */
	public CodeMetricId[] getRequiredMetrics() {
		return requiredMetrics;
	}

	/**
	 * @return the required code smells.
	 */
	public CodeSmellId[] getRequiredCodeSmells() {
		return requiredCodesmells;
	}

}