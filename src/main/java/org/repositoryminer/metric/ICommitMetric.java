package org.repositoryminer.metric;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;

/**
 * Metrics calculations definition.
 */
public interface ICommitMetric extends MetricId {

	public void calculate(AbstractTypeDeclaration type, AST ast, Document document);

}
