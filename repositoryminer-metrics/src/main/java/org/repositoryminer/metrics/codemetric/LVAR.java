package org.repositoryminer.metrics.codemetric;

import java.util.ArrayList;
import java.util.List;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractStatement;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.ast.NodeType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class LVAR extends CodeMetric {

	public LVAR() {
		super.id = CodeMetricId.LVAR;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			for (AbstractMethod method : type.getMethods()) {
				MethodReport mr = cr.getMethodBySignature(method.getName());
				mr.getMetricsReport().setCodeMetric(CodeMetricId.LVAR, calculate(method));
			}
		}
	}

	public int calculate(AbstractMethod method) {
		List<String> lvar = new ArrayList<String>();
		for (AbstractStatement statement : method.getStatements()) {
			if (statement.getNodeType() == NodeType.VARIABLE_DECLARATION && !lvar.contains(statement.getExpression())) {
				lvar.add(statement.getExpression());
			}
		}
		return lvar.size();
	}

	@Override
	public void clean(ProjectReport projectReport) {}

}