package org.repositoryminer.metrics.codemetric;

import java.math.BigDecimal;
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

public class LAA extends CodeMetric {

	public LAA() {
		super.id = CodeMetricId.LAA;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			for (AbstractMethod method : type.getMethods()) {
				MethodReport mr = cr.getMethodBySignature(method.getName());
				mr.getMetricsReport().setCodeMetric(CodeMetricId.LAA, calculate(type, method));
			}
		}
	}

	public double calculate(AbstractType type, AbstractMethod method) {
		int countFields = countAccessedFields(method);
		double result = countFields > 0 ? (type.getFields().size() * 1.0) / countFields : 0;
		return new BigDecimal(result).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
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

	@Override
	public void clean(ProjectReport projectReport) {}

}