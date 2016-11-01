package org.repositoryminer.metric.clazz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.metric.MetricId;

/**
 * <h1>Access To Foreign Data</h1>
 * <p>
 * ATFD is defined as the number of attributes from unrelated classes that are
 * accessed directly or by invoking accessor methods.
 */
public class ATFD extends MethodBasedMetricTemplate {

	private List<Document> methodsDoc;

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		methodsDoc = new ArrayList<Document>();

		int atfdClass = calculate(type, methods, true);
		document.append("name", MetricId.ATFD).append("accumulated", new Integer(atfdClass)).append("methods",
				methodsDoc);
	}

	public int calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, boolean calculateByMethod) {
		int atfdClass = 0;

		for (MethodDeclaration mDeclaration : methods) {
			int atfdMethod = countForeignAccessedFields(type, mDeclaration);

			atfdClass += atfdMethod;
			if (calculateByMethod) {
				methodsDoc.add(new Document("method", mDeclaration.getName()).append("value", new Integer(atfdMethod)));
			}
		}

		return atfdClass;
	}

	private int countForeignAccessedFields(AbstractTypeDeclaration currType, MethodDeclaration method) {
		Set<String> accessedFields = new HashSet<String>();

		for (Statement stm : method.getStatements()) {

			if (stm.getNodeType().equals(NodeType.FIELD_ACCESS)) {
				String exp = stm.getExpression();
				String type = exp.substring(0, exp.lastIndexOf("."));
				if (!currType.getName().equals(type))
					accessedFields.add(exp.toLowerCase());
			} else if (stm.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
				String exp = stm.getExpression();
				String type = exp.substring(0, exp.lastIndexOf("."));
				String methodInv = exp.substring(exp.lastIndexOf(".") + 1);
				if (!currType.getName().equals(type)) {
					if ((methodInv.startsWith("get") || methodInv.startsWith("set")) && methodInv.length() > 3) {
						accessedFields.add((type + "." + methodInv.substring(3)).toLowerCase());
					} else if (methodInv.startsWith("is") && methodInv.length() > 2)
						accessedFields.add((type + "." + methodInv.substring(2)).toLowerCase());
				}
			}
		}

		return accessedFields.size();
	}

}