package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.codemetric.CodeMetricId;

/**
 * <h1>Locality Attribute Accesses</h1>
 * <p>
 * LAA is defined as the number of attributes from the method's definition
 * class, divided by the total number of variables accessed (including
 * attributes used via accessor methods), whereby the number of local attributes
 * accessed is comuted in conformity with LAA specifications.
 */
public class LAA implements IDirectCodeMetric {

	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		Document cycloDocs = new Document("name", getId().toString());

		cycloDocs.append("methods", calculate(type));

		return cycloDocs;
	}

	public List<Document> calculate(AbstractClassDeclaration type) {

		List<Document> ccDocs = new ArrayList<Document>();

		for (MethodDeclaration methodDeclaration : type.getMethods()) {
			float laaValue = calculate(type, methodDeclaration);

			ccDocs.add(new Document("method", methodDeclaration.getName()).append("value", laaValue));
		}

		return ccDocs;
	}

	public float calculate(AbstractClassDeclaration type, MethodDeclaration methodDeclaration) {
		int totalAttributeAccessed = 0;

		for (Statement statement : methodDeclaration.getStatements()) {
			String exp = statement.getExpression();

			if (statement.getNodeType().equals(NodeType.FIELD_ACCESS)) {
				totalAttributeAccessed++;
			} else if (statement.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
				exp = exp.substring(0, exp.indexOf("("));
				String methodInv = exp.substring(exp.lastIndexOf(".") + 1);
				if ((methodInv.startsWith("get") || methodInv.startsWith("set")) && methodInv.length() > 3) {
					totalAttributeAccessed++;
				} else if (methodInv.startsWith("is") && methodInv.length() > 2)
					totalAttributeAccessed++;

			}
		}

		return totalAttributeAccessed == 0 ? 0 : type.getFields().size() * 1.0f / totalAttributeAccessed;

	}

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.LAA;
	}

}
