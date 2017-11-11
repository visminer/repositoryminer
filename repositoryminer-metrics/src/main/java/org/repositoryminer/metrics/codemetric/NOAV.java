package org.repositoryminer.metrics.codemetric;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class NOAV extends CodeMetric {

	private static final CodeMetricId[] REQUIRED_METRICS = { CodeMetricId.LVAR, CodeMetricId.PAR };

	public NOAV() {
		super.id = CodeMetricId.NOAV;
		super.requiredMetrics = REQUIRED_METRICS;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			for (AbstractMethod method : type.getMethods()) {
				MethodReport mr = cr.getMethodBySignature(method.getName());
				mr.getMetricsReport().setCodeMetric(CodeMetricId.NOAV, calculate(method, mr));
			}
		}
	}

	public int calculate(AbstractMethod method, MethodReport methodReport) {
		int accessFields = LAA.countAccessedFields(method);
		int nVar = methodReport.getMetricsReport().getCodeMetric(CodeMetricId.LVAR, Integer.class);
		int nParams = method.getParameters().size();
		return accessFields + nVar + nParams;
	}

	@Override
	public void clean(ProjectReport projectReport) {}

}