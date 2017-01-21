package org.repositoryminer.codemetric.direct;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

/**
 * <h1>Number of attributes</h1>
 * <p>
 * NOA is defined as the number of attributes in a class.
 */
public class NOA implements IDirectCodeMetric {

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.NOA;
	}

	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		return new Document("metric", CodeMetricId.NOA.toString()).append("value", type.getFields().size());
	}

}