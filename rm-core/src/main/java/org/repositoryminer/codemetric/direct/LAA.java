package org.repositoryminer.codemetric.direct;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractFieldAccess;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractMethodInvocation;
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

	public float calculate(AbstractType type, AbstractMethod method) {
		int countFields = countAccessedFields(method);
		float result = countFields > 0 ? (type.getFields().size() * 1.0f) / countFields : 0;
		return new BigDecimal(result).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	public static int countAccessedFields(AbstractMethod method) {
		Set<String> accessedFields = new HashSet<String>();
		for (AbstractStatement stmt : method.getStatements()) {
			if (stmt.getNodeType() == NodeType.FIELD_ACCESS) {
				AbstractFieldAccess fieldAccess = (AbstractFieldAccess) stmt;
				accessedFields.add(fieldAccess.getDeclaringClass() + '.' + fieldAccess.getExpression());
			} else if (stmt.getNodeType() == NodeType.METHOD_INVOCATION) {
				AbstractMethodInvocation methodInvocation = (AbstractMethodInvocation) stmt;
				if (methodInvocation.isAccessor()) {
					accessedFields.add(methodInvocation.getDeclaringClass() + '.' + methodInvocation.getExpression());
				}
			} else {
				continue;
			}
		}
		return accessedFields.size();
	}

}