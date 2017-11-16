package org.repositoryminer.metrics.codesmell;

import java.util.HashMap;

import org.repositoryminer.metrics.codemetric.CodeMetricId;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class GodClass extends CodeSmell {

	private static final CodeMetricId[] REQUIRED_METRICS = { CodeMetricId.ATFD, CodeMetricId.WMC, CodeMetricId.TCC };

	private int atfdThreshold = 5;
	private int wmcThreshold = 47;
	private double tccThreshold = 0.33;

	public GodClass() {
		init();
	}

	public GodClass(int atfdThreshold, int wmcThreshold, double tccThreshold) {
		this.atfdThreshold = atfdThreshold;
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
		init();
	}

	private void init() {
		super.id = CodeSmellId.GOD_CLASS;
		super.requiredMetrics = REQUIRED_METRICS;
		
		super.thresholds = new HashMap<>();
		super.thresholds.put(CodeMetricId.ATFD.name(), atfdThreshold);
		super.thresholds.put(CodeMetricId.WMC.name(), wmcThreshold);
		super.thresholds.put(CodeMetricId.TCC.name(), tccThreshold);
	}
	
	@Override
	public void detect(FileReport fileReport, ProjectReport projectReport) {
		for (ClassReport cr : fileReport.getClasses()) {
			int atfd = cr.getMetricsReport().getCodeMetric(CodeMetricId.ATFD, Integer.class);
			int wmc = cr.getMetricsReport().getCodeMetric(CodeMetricId.WMC, Integer.class);
			double tcc = cr.getMetricsReport().getCodeMetric(CodeMetricId.TCC, Double.class);
			if (detect(atfd, wmc, tcc)) {
				cr.getMetricsReport().setCodeSmell(CodeSmellId.GOD_CLASS);
			}
		}
	}

	public boolean detect(int atfd, int wmc, double tcc) {
		return (atfd > atfdThreshold) && (wmc >= wmcThreshold) && (tcc < tccThreshold);
	}

	
	/*** GETTERS AND SETTERS ***/
	
	public int getAtfdThreshold() {
		return atfdThreshold;
	}

	public void setAtfdThreshold(int atfdThreshold) {
		this.atfdThreshold = atfdThreshold;
	}

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

}