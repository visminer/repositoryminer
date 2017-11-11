package org.repositoryminer.metrics.codesmell;

import java.util.HashMap;

import org.repositoryminer.metrics.codemetric.CodeMetricId;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class LongMethod extends CodeSmell {

	private static final CodeMetricId[] REQUIRED_METRICS = { CodeMetricId.LOC };

	private int mlocThreshold = 65;

	public LongMethod() {
		init();
	}

	public LongMethod(int mlocThreshold) {
		this.mlocThreshold = mlocThreshold;
		init();
	}

	private void init() {
		super.id = CodeSmellId.LONG_METHOD;
		super.requiredMetrics = REQUIRED_METRICS;
		
		super.thresholds = new HashMap<>();
		super.thresholds.put(CodeMetricId.LOC.name(), mlocThreshold);
	}
	
	@Override
	public void detect(FileReport fileReport, ProjectReport projectReport) {
		for (ClassReport cr : fileReport.getClasses()) {
			for (MethodReport mr : cr.getMethods()) {
				int loc = mr.getMetricsReport().getCodeMetric(CodeMetricId.LOC, Integer.class);
				if (loc > mlocThreshold) {
					cr.getMetricsReport().setCodeSmell(CodeSmellId.LONG_METHOD);
				}
			}
		}
	}

	/*** GETTERS AND SETTERS ***/

	public int getMlocThreshold() {
		return mlocThreshold;
	}

	public void setMlocThreshold(int mlocThreshold) {
		this.mlocThreshold = mlocThreshold;
	}

}