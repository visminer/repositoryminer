package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractStatement;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.ast.NodeType;

public class LAA implements IDirectCodeMetric {

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
		return calculate(type, method);
	}

	@Override
	public String getMetric() {
		return "LAA";
	}

	public float calculate(AbstractType type, AbstractMethod methodDeclaration) {
		int totalAttributeAccessed = 0;

		for (AbstractStatement statement : methodDeclaration.getStatements()) {
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

}