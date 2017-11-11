package org.repositoryminer.metrics.codesmell;

import java.util.HashMap;

import org.repositoryminer.metrics.codemetric.CodeMetricId;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class BrainMethod extends CodeSmell {

	private static final CodeMetricId[] REQUIRED_METRICS = { CodeMetricId.LOC, CodeMetricId.CYCLO, CodeMetricId.NOAV,
			CodeMetricId.MAXNESTING };

	private int locThreshold = 65;
	private float ccThreshold = 7;
	private int maxNestingThreshold = 5;
	private int noavThreshold = 8;

	public BrainMethod() {
		init();
	}

	public BrainMethod(int mlocThreshold, float ccThreshold, int maxNestingThreshold, int noavThreshold) {
		this.locThreshold = mlocThreshold;
		this.ccThreshold = ccThreshold;
		this.maxNestingThreshold = maxNestingThreshold;
		this.noavThreshold = noavThreshold;
		init();
	}

	private void init() {
		super.id = CodeSmellId.BRAIN_METHOD;
		super.requiredMetrics = REQUIRED_METRICS;
		
		super.thresholds = new HashMap<>();
		super.thresholds.put(CodeMetricId.LOC.name(), locThreshold);
		super.thresholds.put(CodeMetricId.CYCLO.name(), ccThreshold);
		super.thresholds.put(CodeMetricId.NOAV.name(), noavThreshold);
		super.thresholds.put(CodeMetricId.MAXNESTING.name(), maxNestingThreshold);
	}
	
	@Override
	public void detect(FileReport fileReport, ProjectReport projectReport) {
		for (ClassReport cr : fileReport.getClasses()) {
			for (MethodReport mr : cr.getMethods()) {
				int mloc = mr.getMetricsReport().getCodeMetric(CodeMetricId.LOC, Integer.class);
				int cyclo = mr.getMetricsReport().getCodeMetric(CodeMetricId.CYCLO, Integer.class);
				int noav = mr.getMetricsReport().getCodeMetric(CodeMetricId.NOAV, Integer.class);
				int maxnesting = mr.getMetricsReport().getCodeMetric(CodeMetricId.MAXNESTING, Integer.class);
				if (detect(cyclo, mloc, noav, maxnesting)) {
					mr.getMetricsReport().setCodeSmell(CodeSmellId.BRAIN_METHOD);
				}
			}
		}
	}

	public boolean detect(int cc, int mloc, int noav, int maxNesting) {
		return mloc > locThreshold && cc > ccThreshold && maxNesting >= maxNestingThreshold && noav > noavThreshold;
	}

	/*** GETTERS AND SETTERS ***/

	public int getMlocThreshold() {
		return locThreshold;
	}

	public void setMlocThreshold(int mlocThreshold) {
		this.locThreshold = mlocThreshold;
	}

	public float getCcThreshold() {
		return ccThreshold;
	}

	public void setCcThreshold(float ccThreshold) {
		this.ccThreshold = ccThreshold;
	}

	public int getMaxNestingThreshold() {
		return maxNestingThreshold;
	}

	public void setMaxNestingThreshold(int maxNestingThreshold) {
		this.maxNestingThreshold = maxNestingThreshold;
	}

	public int getNoavThreshold() {
		return noavThreshold;
	}

	public void setNoavThreshold(int noavThreshold) {
		this.noavThreshold = noavThreshold;
	}

}