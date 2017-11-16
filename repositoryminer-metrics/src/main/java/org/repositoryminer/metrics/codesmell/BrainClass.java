package org.repositoryminer.metrics.codesmell;

import java.util.HashMap;

import org.repositoryminer.metrics.codemetric.CodeMetricId;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class BrainClass extends CodeSmell {

	private static final CodeMetricId[] REQUIRED_METRICS = { CodeMetricId.WMC, CodeMetricId.TCC, CodeMetricId.LOC };
	private static final CodeSmellId[] REQUIRED_CODESMELLS = { CodeSmellId.BRAIN_METHOD };

	private int wmcThreshold = 47;
	private double tccThreshold = 0.5;
	private int brainMethodThreshold = 1;
	private int locThreshold = 195;

	public BrainClass() {
		init();
	}

	public BrainClass(int wmcThreshold, double tccThreshold, int brainMethodThreshold, int locThreshold) {
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
		this.brainMethodThreshold = brainMethodThreshold;
		this.locThreshold = locThreshold;
		init();
	}

	private void init() {
		super.id = CodeSmellId.BRAIN_CLASS;
		super.requiredMetrics = REQUIRED_METRICS;
		super.requiredCodesmells = REQUIRED_CODESMELLS;
		
		super.thresholds = new HashMap<>();
		super.thresholds.put(CodeMetricId.WMC.name(), wmcThreshold);
		super.thresholds.put(CodeMetricId.TCC.name(), tccThreshold);
		super.thresholds.put(CodeMetricId.LOC.name(), locThreshold);
		super.thresholds.put(CodeSmellId.BRAIN_METHOD.name(), brainMethodThreshold);
	}
	
	@Override
	public void detect(FileReport fileReport, ProjectReport projectReport) {
		for (ClassReport cr : fileReport.getClasses()) {
			int wmc = cr.getMetricsReport().getCodeMetric(CodeMetricId.WMC, Integer.class);
			double tcc = cr.getMetricsReport().getCodeMetric(CodeMetricId.TCC, Double.class);
			int loc = cr.getMetricsReport().getCodeMetric(CodeMetricId.LOC, Integer.class);
			int nbm = 0;

			for (MethodReport mr : cr.getMethods()) {
				if (mr.getMetricsReport().hasCodeSmell(CodeSmellId.BRAIN_METHOD)) {
					nbm++;
				}
			}

			if (detect(nbm, loc, wmc, tcc)) {
				cr.getMetricsReport().setCodeSmell(CodeSmellId.BRAIN_CLASS);
			}
		}
	}

	public boolean detect(int nbm, int loc, int wmc, double tcc) {
		boolean exp1 = nbm > brainMethodThreshold && loc >= locThreshold;
		boolean exp2 = nbm == brainMethodThreshold && loc >= (2 * locThreshold) && wmc >= (2 * wmcThreshold);
		boolean exp3 = wmc >= wmcThreshold && tcc < tccThreshold;
		return (exp1 || exp2) && exp3;
	}

	// *** GETTERS AND SETTERS ***//

	public int getWmcThreshold() {
		return wmcThreshold;
	}

	public void setWmcThreshold(int wmcThreshold) {
		this.wmcThreshold = wmcThreshold;
	}

	public double getTccThreshold() {
		return tccThreshold;
	}

	public void setTccThreshold(double tccThreshold) {
		this.tccThreshold = tccThreshold;
	}

	public int getBrainMethodThreshold() {
		return brainMethodThreshold;
	}

	public void setBrainMethodThreshold(int brainMethodThreshold) {
		this.brainMethodThreshold = brainMethodThreshold;
	}

	public int getLocThreshold() {
		return locThreshold;
	}

	public void setLocThreshold(int locThreshold) {
		this.locThreshold = locThreshold;
	}

}