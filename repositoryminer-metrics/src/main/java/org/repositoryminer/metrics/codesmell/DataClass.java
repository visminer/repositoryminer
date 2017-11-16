package org.repositoryminer.metrics.codesmell;

import java.util.HashMap;

import org.repositoryminer.metrics.codemetric.CodeMetricId;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class DataClass extends CodeSmell {

	private static final CodeMetricId[] REQUIRED_METRICS = { CodeMetricId.WOC, CodeMetricId.NOPA, CodeMetricId.NOAM, CodeMetricId.WMC };

	private double wocThreshold = 0.33;
	private int wmcThreshold1 = 31;
	private int wmcThreshold2 = 47;
	private int publicMembersThreshold1 = 5;
	private int publicMembersThreshold2 = 8;

	public DataClass() {
		init();
	}

	public DataClass(double wocThreshold, int wmcThreshold1, int wmcThreshold2, int publicMembersThreshold1, int publicMembersThreshold2) {
		this.wocThreshold = wocThreshold;
		this.wmcThreshold1 = wmcThreshold1;
		this.wmcThreshold2 = wmcThreshold2;
		this.publicMembersThreshold1 = publicMembersThreshold1;
		this.publicMembersThreshold2 = publicMembersThreshold2;
		init();
	}

	public void init() {
		super.id = CodeSmellId.DATA_CLASS;
		super.requiredMetrics = REQUIRED_METRICS;
		
		super.thresholds = new HashMap<>();
		super.thresholds.put(CodeMetricId.WOC.name(), wocThreshold);
		super.thresholds.put(CodeMetricId.WMC.name()+"(1)", wmcThreshold1);
		super.thresholds.put(CodeMetricId.WMC.name()+"(2)", wmcThreshold2);
		super.thresholds.put(CodeMetricId.NOPA.name()+"+"+CodeMetricId.NOAM.name()+"(1)", publicMembersThreshold1);
		super.thresholds.put(CodeMetricId.NOPA.name()+"+"+CodeMetricId.NOAM.name()+"(2)", publicMembersThreshold2);
	}
	
	@Override
	public void detect(FileReport fileReport, ProjectReport projectReport) {
		for (ClassReport cr : fileReport.getClasses()) {
			double woc = cr.getMetricsReport().getCodeMetric(CodeMetricId.WOC, Double.class);
			int nopa = cr.getMetricsReport().getCodeMetric(CodeMetricId.NOPA, Integer.class);
			int noam = cr.getMetricsReport().getCodeMetric(CodeMetricId.NOAM, Integer.class);
			int wmc = cr.getMetricsReport().getCodeMetric(CodeMetricId.WMC, Integer.class);
			if (detect(woc, nopa, noam, wmc)) {
				cr.getMetricsReport().setCodeSmell(CodeSmellId.DATA_CLASS);
			}
		}
	}

	private boolean detect(double woc, int nopa, int noam, int wmc) {
		int publicMembers = nopa + noam;
		boolean offerManyData = woc < wocThreshold;
		boolean isNotComplex = (publicMembers > publicMembersThreshold1 && wmc < wmcThreshold1)
				|| (publicMembers > publicMembersThreshold2 && wmc < wmcThreshold2);
		return offerManyData && isNotComplex;
	}

	/*** GETTERS AND SETTERS ***/

	public double getWocThreshold() {
		return wocThreshold;
	}

	public void setWocThreshold(double wocThreshold) {
		this.wocThreshold = wocThreshold;
	}

	public int getWmcThreshold1() {
		return wmcThreshold1;
	}

	public void setWmcThreshold1(int wmcThreshold1) {
		this.wmcThreshold1 = wmcThreshold1;
	}

	public int getWmcThreshold2() {
		return wmcThreshold2;
	}

	public void setWmcThreshold2(int wmcThreshold2) {
		this.wmcThreshold2 = wmcThreshold2;
	}

	public int getPublicMembersThreshold1() {
		return publicMembersThreshold1;
	}

	public void setPublicMembersThreshold1(int publicMembersThreshold1) {
		this.publicMembersThreshold1 = publicMembersThreshold1;
	}

	public int getPublicMembersThreshold2() {
		return publicMembersThreshold2;
	}

	public void setPublicMembersThreshold2(int publicMembersThreshold2) {
		this.publicMembersThreshold2 = publicMembersThreshold2;
	}

}