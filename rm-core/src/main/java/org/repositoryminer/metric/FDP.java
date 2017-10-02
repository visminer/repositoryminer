package org.repositoryminer.metric;

import java.util.HashSet;
import java.util.Set;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractFieldAccess;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractMethodInvocation;
import org.repositoryminer.ast.AbstractStatement;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.ast.NodeType;

public class FDP implements IMetric {

	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes())
			for (AbstractMethod method : type.getMethods())
				method.getMetrics().put(MetricID.FDP, calculate(type, method));
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
				if (!methodInvocation.isAccessor())
					continue;
				
				declarringClass = methodInvocation.getDeclaringClass();
			} else
				continue;

			if (!currType.getName().equals(declarringClass))
				accessedClasses.add(declarringClass);
		}

		return accessedClasses.size();
	}

	@Override
	public MetricID getId() {
		return MetricID.FDP;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return null;
	}

}