package org.repositoryminer.metric.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
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
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		methodsDoc = new ArrayList<Document>();
		for (MethodDeclaration method : methods) {
			int maxNesting = calculate(method);
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(maxNesting)));
		}
		document.append("name", MetricId.MAXNESTING).append("methods", methodsDoc);
	}

	// FIXME: solve inconsistency of the values found
	public int calculate(MethodDeclaration method) {
		return method.getMaxNesting(); 
	}

}