package org.repositoryminer.metric.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.metric.MetricId;

/**
 * <h1>Maximum Nesting Level</h1>
 * <p>
 * MAXNESTING is defined as the maximum nesting level of control structures
 * within an operation.
 */
public class MAXNESTING extends MethodBasedMetricTemplate {

	private List<Document> methodsDoc;

	@Override
	public MetricId getId() {
		return MetricId.MAXNESTING;
	}
	
	@Override
	public Document calculate(AbstractClassDeclaration type, List<MethodDeclaration> methods, AST ast) {
		methodsDoc = new ArrayList<Document>();
		for (MethodDeclaration method : methods) {
			methodsDoc.add(new Document("method", method.getName()).append("value", calculate(method)));
		}
		return new Document("metric", MetricId.MAXNESTING.toString()).append("methods", methodsDoc);
	}

	public int calculate(MethodDeclaration method) {
		int max = 0;
		for (Statement stmt : method.getStatements()) {
			max = Math.max(max, stmt.getNesting());
		}
		return max;
	}
	
}