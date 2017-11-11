package org.repositoryminer.metrics.codemetric;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class WMC extends CodeMetric {

	private static final CodeMetricId[] REQUIRED_METRICS = { CodeMetricId.CYCLO };

	public WMC() {
		super.id = CodeMetricId.WMC;
		super.requiredMetrics = REQUIRED_METRICS;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			int wmc = 0;
			
			for (AbstractMethod method : type.getMethods()) {
				MethodReport mr = cr.getMethodBySignature(method.getName());
				wmc += mr.getMetricsReport().getCodeMetric(CodeMetricId.CYCLO, Integer.class);
			}

			cr.getMetricsReport().setCodeMetric(CodeMetricId.WMC, wmc);
		}
	}

	@Override
	public void clean(ProjectReport projectReport) {}

}