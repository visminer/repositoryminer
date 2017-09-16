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

public class FDP implements IDirectCodeMetric {

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
		return "FDP";
	}

	public int calculate(AbstractType currType, AbstractMethod method) {
		Set<String> accessedClasses = new HashSet<String>();
		for (AbstractStatement stmt : method.getStatements()) {
			String declarringClass = null;

			if (stmt.getNodeType() == NodeType.FIELD_ACCESS) {
				AbstractFieldAccess fieldAccess = (AbstractFieldAccess) stmt;
				declarringClass = fieldAccess.getDeclaringClass();
			} else if (stmt.getNodeType() == NodeType.METHOD_INVOCATION) {
				AbstractMethodInvocation methodInvocation = (AbstractMethodInvocation) stmt;
				if (!methodInvocation.isAccessor()) {
					continue;
				}
				declarringClass = methodInvocation.getDeclaringClass();
			} else {
				continue;
			}

			if (!currType.getName().equals(declarringClass)) {
				accessedClasses.add(declarringClass);
			}
		}

		return accessedClasses.size();
	}

}