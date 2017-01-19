package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

/**
 * <h1>Number of Methods</h1>
 * <p>
 * NOM is defined as the number of the methods inside a class.
 */
public class NOM extends MethodBasedMetricTemplate {

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.NOM;
	}

	@Override
	public Document calculate(AbstractClassDeclaration type, List<MethodDeclaration> methods, AST ast) {
		return new Document("metric", CodeMetricId.NOM.toString()).append("value", methods.size());
	}

}