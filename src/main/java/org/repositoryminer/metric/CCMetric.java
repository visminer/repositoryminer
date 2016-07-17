package org.repositoryminer.metric;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.listener.IMetricCalculationListener;

public class CCMetric extends MethodBasedMetricTemplate {

	private Map<String, Integer> valuesPerMethod;

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, 
			IMetricCalculationListener listener) {
		valuesPerMethod = new HashMap<String, Integer>();

		int ccClass = calculate(methods);
		listener.updateMethodBasedMetricValue(CC, ccClass, valuesPerMethod);
	}

	// for classes
	public int calculate(List<MethodDeclaration> methods) {
		int ccClass = 0;
		for (MethodDeclaration method : methods) {

			int cc = calculate(method);
			ccClass += cc;
			valuesPerMethod.put(method.getName(),new Integer(cc));
		}
		return ccClass;
	}

	// for methods
	public int calculate(MethodDeclaration method) {
		if (method.getStatements() == null) {
			return 1;
		}

		int cc = 1;
		for (Statement statement : method.getStatements()) {
			switch (statement.getNodeType()) {
			case IF:
			case SWITCH_CASE:
			case FOR:
			case DO_WHILE:
			case WHILE:
			case CATCH:
			case CONDITIONAL_EXPRESSION:
				cc += calculateExpression(statement.getExpression(), statement.getNodeType());
				break;
			default:
				break;
			}
		}

		return cc;
	}

	private int calculateExpression(String expression, NodeType type) {
		int cc = 1;
		char[] chars = expression.toCharArray();

		if (type != NodeType.CATCH) {
			for (int i = 0; i < chars.length - 1; i++) {
				char next = chars[i];
				if ((next == '&' || next == '|') && (next == chars[i + 1])) {
					cc++;
				}
			}
		} else {
			for (int i = 0; i < chars.length - 1; i++) {
				if (chars[i] == '|') {
					cc++;
				}
			}
		}

		return cc;
	}

}