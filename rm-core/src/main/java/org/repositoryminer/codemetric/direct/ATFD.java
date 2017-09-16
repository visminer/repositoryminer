package org.repositoryminer.codemetric.direct;

import java.util.HashSet;
import java.util.Set;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractFieldAccess;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractMethodInvocation;
import org.repositoryminer.ast.AbstractStatement;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.ast.NodeType;

public class ATFD implements IDirectCodeMetric {

	private int atfdClass;

	@Override
	public Object calculateFromFile(AST ast) {
		return null;
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		int temp = atfdClass;
		atfdClass = 0;
		return temp;
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		int atfd = calculate(type, method);
		atfdClass += atfd;
		return atfd;
	}

	@Override
	public String getMetric() {
		return "ATFD";
	}

	public int calculate(AbstractType currType, AbstractMethod method) {
		Set<String> accessedFields = new HashSet<String>();
		for (AbstractStatement stmt : method.getStatements()) {
			String field = null;
			String declarringClass = null;

			if (stmt.getNodeType() == NodeType.FIELD_ACCESS) {
				AbstractFieldAccess fieldAccess = (AbstractFieldAccess) stmt;
				field = fieldAccess.getExpression();
				declarringClass = fieldAccess.getDeclaringClass();
			} else if (stmt.getNodeType() == NodeType.METHOD_INVOCATION) {
				AbstractMethodInvocation methodInvocation = (AbstractMethodInvocation) stmt;
				if (!methodInvocation.isAccessor()) {
					continue;
				}
				field = methodInvocation.getAccessedField();
				declarringClass = methodInvocation.getDeclaringClass();
			} else {
				continue;
			}

			if (!currType.getName().equals(declarringClass)) {
				accessedFields.add(declarringClass + '.' + field);
			}
		}

		return accessedFields.size();
	}

}