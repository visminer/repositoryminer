package org.repositoryminer.metric.clazz;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.metric.MetricId;

/**
 * Metrics calculations definition.
 */
public interface IClassMetric extends MetricId {

	public void calculate(AbstractTypeDeclaration type, AST ast, Document document);

}
