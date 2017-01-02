package org.repositoryminer.metric.indirect;

import java.util.Map;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.metric.MetricId;

public interface IIndirectMetric {

	/**
	 * Activates the calculation of the metric
	 * 
	 * @param type
	 *            the abstract representation of a type (
	 *            {@link org.repositoryminer.ast.AbstractClassDeclaration}).
	 * @param ast
	 *            an instance of an abstract syntactic tree (
	 *            {@link org.repositoryminer.ast.AST})
	 */
	public void calculate(AbstractClassDeclaration type, AST ast);

	/**
	 * @return The metric ID
	 */
	public MetricId getId();

	/**
	 * @return The result of the metric after process all the source files. This
	 *         method ought to return a map containing the canonical class name as
	 *         key and a document with the metric value of the class used as key.
	 */
	public Map<String, Document> getResult();

}