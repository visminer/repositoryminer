package org.repositoryminer.metric;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;

/**
 * Metrics calculations definition.
 */
public interface IMetric extends MetricIds {

	public void calculate(AbstractTypeDeclaration type, AST ast, Document document);

}
