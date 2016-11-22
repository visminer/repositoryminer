package org.repositoryminer.metric.clazz;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.metric.MetricId;

/**
 * <h1>Number of Methods</h1>
 * <p>
 * NOM is defined as the number of the methods inside a class.
 */
public class NOM extends MethodBasedMetricTemplate {

	@Override
	public MetricId getId() {
		return MetricId.NOM;
	}

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		document.append("name", MetricId.NOM.toString()).append("value", methods.size());
	}

}