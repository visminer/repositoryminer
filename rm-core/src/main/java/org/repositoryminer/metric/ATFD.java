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

public class ATFD implements IMetric {

	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			int atfdClass = 0;
			for (AbstractMethod method : type.getMethods()) {
				int atfdMethod = calculate(type, method);
				atfdClass += atfdMethod;
				method.getMetrics().put(MetricID.ATFD, atfdMethod);
			}
			type.getMetrics().put(MetricID.ATFD, atfdClass);
		}
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
				if (!methodInvocation.isAccessor()) 
					continue;
				
				field = methodInvocation.getAccessedField();
				declarringClass = methodInvocation.getDeclaringClass();
			} else 
				continue;

			if (!currType.getName().equals(declarringClass))
				accessedFields.add(declarringClass + '.' + field);
		}

		return accessedFields.size();
	}

	@Override
	public MetricID getId() {
		return MetricID.ATFD;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return null;
	}

}