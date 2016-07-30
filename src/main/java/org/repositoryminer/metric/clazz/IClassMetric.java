package org.repositoryminer.metric.clazz;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;

/**
 * <h1>Top class for all metrics to inherit from</h1>
 * <p>
 * Concrete metric classes must implement this class. Although the class does
 * not amount much into methods (to better justify its existence), it helps out
 * other classes in encapsulating generic collections of metrics, as in, for
 * instance,
 * {@link org.repositoryminer.mining.SourceAnalyzer#processCommitMetrics}.
 * <p>
 * We also aim for any future necessary evolution of the class to contain
 * further processing beyond the calculation of the metrics.
 * <p>
 * It is up to the implementations of this interface to properly decide how to
 * persist the data extracted from a given AST (
 * {@link org.repositoryminer.ast.AST}). After the metric's value is obtained it
 * can be pushed to the instance of org.bson.Document injected into
 * {@link #calculate(AbstractTypeDeclaration, AST, Document)}.
 */
public interface IClassMetric {

	/**
	 * Activates the calculation of the metric
	 * 
	 * @param type
	 *            the abstract representation of a type (
	 *            {@link org.repositoryminer.ast.AbstractTypeDeclaration}).
	 * @param ast
	 *            an instance of an abstract syntactic tree (
	 *            {@link org.repositoryminer.ast.AST})
	 * @param document
	 *            the root of mongodb document in which metrics values must be
	 *            filled
	 */
	public void calculate(AbstractTypeDeclaration type, AST ast, Document document);

}
