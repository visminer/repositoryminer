package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractStatement;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.ast.NodeType;

public class CYCLO implements IDirectCodeMetric {

	@Override
	public Object calculateFromFile(AST ast) {
		return null;
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		return null;
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return calculate(method);
	}

	@Override
	public String getMetric() {
		return "CYCLO";
	}

	public int calculate(AbstractMethod method) {
		if (method.getStatements() == null) {
			return 1;
		}

		int cc = 1;
		for (AbstractStatement statement : method.getStatements()) {
			switch (statement.getNodeType()) {
			case SWITCH_CASE:
				cc++;
				break;

			case IF:
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