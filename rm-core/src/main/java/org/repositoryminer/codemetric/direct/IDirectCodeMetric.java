package org.repositoryminer.codemetric.direct;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

/**
 * <h1>Top class for all metrics to inherit from</h1>
 * <p>
 * Concrete metric classes must implement this class. Although the class does
 * not amount much into methods (to better justify its existence), it helps out
 * other classes in encapsulating generic collections of metrics, as in, for
 * instance,
 * {@link org.repositoryminer.mining.DirectCodeAnalysisProcessor#processCommitMetrics}.
 * <p>
 * We also aim for any future necessary evolution of the class to contain
 * further processing beyond the calculation of the metrics.
 * <p>
 * It is up to the implementations of this interface to properly decide how to
 * persist the data extracted from a given AST (
 * {@link org.repositoryminer.ast.AST}). After the metric's value is obtained it
 * can be pushed to the instance of org.bson.Document injected into
 * {@link #calculate(AbstractClassDeclaration, AST, Document)}.
 */
public interface IDirectCodeMetric {

	/**
	 * Activates the calculation of the metric
	 * 
	 * @param type
	 *            the abstract representation of a type (
	 *            {@link org.repositoryminer.ast.AbstractClassDeclaration}).
	 * @param ast
	 *            an instance of an abstract syntactic tree (
	 *            {@link org.repositoryminer.ast.AST})
	 * @return the document with the data to persist in database
	 */
	public Document calculate(AbstractClassDeclaration type, AST ast);

	/**
	 * @return The metric ID
	 */
	public CodeMetricId getId();

}