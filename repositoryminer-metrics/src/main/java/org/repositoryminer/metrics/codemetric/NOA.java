package org.repositoryminer.metrics.codemetric;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class NOA extends CodeMetric {

	public NOA() {
		super.id = CodeMetricId.NOA;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			cr.getMetricsReport().setCodeMetric(CodeMetricId.NOA, type.getFields().size());
		}
	}

	@Override
	public void clean(ProjectReport projectReport) {}

}