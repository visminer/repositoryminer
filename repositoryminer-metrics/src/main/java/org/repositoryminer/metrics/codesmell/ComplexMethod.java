package org.repositoryminer.metrics.codesmell;

import java.util.HashMap;

import org.repositoryminer.metrics.codemetric.CodeMetricId;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class ComplexMethod extends CodeSmell {

	private static final CodeMetricId[] REQUIRED_METRICS = { CodeMetricId.CYCLO };

	private int cycloThreshold = 10;

	public ComplexMethod() {
		init();
	}

	public ComplexMethod(int cycloThreshold) {
		this.cycloThreshold = cycloThreshold;
		init();
	}
	
	private void init() {
		super.id = CodeSmellId.COMPLEX_METHOD;
		super.requiredMetrics = REQUIRED_METRICS;
		
		super.thresholds = new HashMap<>();
		super.thresholds.put(CodeMetricId.CYCLO.name(), cycloThreshold);
	}

	@Override
	public void detect(FileReport fileReport, ProjectReport projectReport) {
		for (ClassReport cr : fileReport.getClasses()) {
			for (MethodReport mr : cr.getMethods()) {
				int cyclo = mr.getMetricsReport().getCodeMetric(CodeMetricId.CYCLO, Integer.class);
				if (cyclo > cycloThreshold) {
					mr.getMetricsReport().setCodeSmell(CodeSmellId.COMPLEX_METHOD);
				}
			}
		}
	}

	/*** GETTERS AND SETTERS ***/

	public int getCycloThreshold() {
		return cycloThreshold;
	}

	public void setCycloThreshold(int cycloThreshold) {
		this.cycloThreshold = cycloThreshold;
	}

}