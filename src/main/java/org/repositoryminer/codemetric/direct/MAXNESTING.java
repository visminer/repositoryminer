package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.codemetric.CodeMetricId;

/**
 * <h1>Maximum Nesting Level</h1>
 * <p>
 * MAXNESTING is defined as the maximum nesting level of control structures
 * within an operation.
 */
public class MAXNESTING implements IDirectCodeMetric {

	private List<Document> methodsDoc = new ArrayList<Document>();

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.MAXNESTING;
	}
	
	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		methodsDoc.clear();
		for (MethodDeclaration method : type.getMethods()) {
			methodsDoc.add(new Document("method", method.getName()).append("value", calculate(method)));
		}
		return new Document("metric", CodeMetricId.MAXNESTING.toString()).append("methods", methodsDoc);
	}

	public int calculate(MethodDeclaration method) {
		int max = 0;
		for (Statement stmt : method.getStatements()) {
			max = Math.max(max, stmt.getNesting());
		}
		return max;
	}
	
}