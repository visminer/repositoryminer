package org.repositoryminer.codemetric.direct;

import java.util.HashSet;
import java.util.Set;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractStatement;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.ast.NodeType;

public class ATFD implements IDirectCodeMetric {

	Set<String> clazzLevelValue = new HashSet<String>();

	@Override
	public Object calculateFromFile(AST ast) {
		return null;
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		int result = clazzLevelValue.size();
		clazzLevelValue.clear();
		return result;
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return calculate(type, method);
	}

	@Override
	public String getMetric() {
		return "ATFD";
	}

	public int calculate(AbstractType currType, AbstractMethod method) {
		Set<String> accessedFields = new HashSet<String>();

		for (AbstractStatement stmt : method.getStatements()) {
			String exp, type;

			if (stmt.getNodeType() == NodeType.FIELD_ACCESS || stmt.getNodeType() == NodeType.METHOD_INVOCATION) {
				exp = stmt.getExpression();
				type = exp.substring(0, exp.lastIndexOf("."));
			} else {
				continue;
			}

			if (stmt.getNodeType().equals(NodeType.FIELD_ACCESS)) {
				if (!currType.getName().equals(type)) {
					accessedFields.add(exp.toLowerCase());
					clazzLevelValue.add(exp.toLowerCase());
				}
			} else if (stmt.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
				String methodInv = exp.substring(exp.lastIndexOf(".") + 1);
				if (!currType.getName().equals(type)) {
					if ((methodInv.startsWith("get") || methodInv.startsWith("set")) && methodInv.length() > 3) {
						accessedFields.add((type + "." + methodInv.substring(3)).toLowerCase());
						clazzLevelValue.add((type + "." + methodInv.substring(3)).toLowerCase());
					} else if (methodInv.startsWith("is") && methodInv.length() > 2) {
						accessedFields.add((type + "." + methodInv.substring(2)).toLowerCase());
						clazzLevelValue.add((type + "." + methodInv.substring(2)).toLowerCase());
					}
				}
			}
		}

		return accessedFields.size();
	}

}