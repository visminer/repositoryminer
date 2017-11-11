package org.repositoryminer.metrics.codemetric;

import java.util.HashSet;
import java.util.Set;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractFieldAccess;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractMethodInvocation;
import org.repositoryminer.metrics.ast.AbstractStatement;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.ast.NodeType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class ATFD extends CodeMetric {

	public ATFD() {
		super.id = CodeMetricId.ATFD;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			int atfdClass = 0;
			
			for (AbstractMethod method : type.getMethods()) {
				int atfdMethod = calculate(type, method);
				atfdClass += atfdMethod;
				MethodReport mr = cr.getMethodBySignature(method.getName());
				mr.getMetricsReport().setCodeMetric(CodeMetricId.ATFD, atfdMethod);
			}
			
			cr.getMetricsReport().setCodeMetric(CodeMetricId.ATFD, atfdClass);
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

	@Override
	public void clean(ProjectReport projectReport) {
	}

}