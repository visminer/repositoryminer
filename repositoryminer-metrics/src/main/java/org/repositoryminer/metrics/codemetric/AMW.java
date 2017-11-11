package org.repositoryminer.metrics.codemetric;

import java.math.BigDecimal;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class AMW extends CodeMetric {

	private static final CodeMetricId[] REQUIRED_METRICS = {CodeMetricId.WMC, CodeMetricId.NOM};
	
	public AMW() {
		super.id = CodeMetricId.AMW;
		super.requiredMetrics = REQUIRED_METRICS;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			int wmc = cr.getMetricsReport().getCodeMetric(CodeMetricId.WMC, Integer.class);
			int nom = cr.getMetricsReport().getCodeMetric(CodeMetricId.NOM, Integer.class);
			cr.getMetricsReport().setCodeMetric(CodeMetricId.AMW, calculate(wmc, nom));
		}
	}

	public float calculate(int wmc, int nom) {
		if (nom == 0) {
			return 0l;
		}
		return new BigDecimal(wmc * 1f / nom).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	@Override
	public void clean(ProjectReport projectReport) {}

}