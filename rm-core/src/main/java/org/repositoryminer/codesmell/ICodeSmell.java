package org.repositoryminer.codesmell;

import org.repositoryminer.ast.AST;
import org.repositoryminer.metric.MetricID;

public interface ICodeSmell {

	/**
	 * Detects the code smell and stores the result in the ast.
	 * 
	 * @param ast
	 */
	public void detect(AST ast);

	/**
	 * @return the code smell identifier.
	 */
	public CodeSmellID getId();

	/**
	 * @return the required metrics.
	 */
	public MetricID[] getRequiredMetrics();

	/**
	 * @return the required code smells.
	 */
	public CodeSmellID[] getRequiredCodeSmells();

}