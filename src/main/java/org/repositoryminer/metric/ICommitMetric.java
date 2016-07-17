package org.repositoryminer.metric;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.listener.IMetricCalculationListener;

/**
 * Metrics calculations definition.
 */
public interface ICommitMetric extends MetricId {

	public void calculate(AbstractTypeDeclaration type, AST ast, IMetricCalculationListener listener);

}
