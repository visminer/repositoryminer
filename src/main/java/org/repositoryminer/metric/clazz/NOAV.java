package org.repositoryminer.metric.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.metric.MetricId;

/**
 * <h1>Number of Accessed Variables</h1>
 * <p>
 * NOAV is defined as the total number of variables accessed directly from the
 * measured operation. Variables include parameters, local variables, but also
 * instance variables and global variables.
 */
public class NOAV extends MethodBasedMetricTemplate {

	private List<Document> methodsDoc;

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		methodsDoc = new ArrayList<Document>();

		for (MethodDeclaration method : methods) {
			int noav = calculate(method);
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(noav)));
		}

		document.append("name", MetricId.NOAV).append("methods", methodsDoc);
	}

	public int calculate(MethodDeclaration method) {
		int noav = 0;
		LVAR lvarMetric = new LVAR();
		PAR parMetric = new PAR();

		for (Statement stmt : method.getStatements()) {
			if (NodeType.VARIABLE_ACCESS.equals(stmt.getNodeType()))
				noav++;
		}

		// removing variable declarations from count
		noav = noav - lvarMetric.calculate(method);
		// removing method parameters from count
		noav = noav - parMetric.calculate(method);

		return noav;
	}

}
