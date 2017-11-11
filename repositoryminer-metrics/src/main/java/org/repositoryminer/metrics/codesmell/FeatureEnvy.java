package org.repositoryminer.metrics.codesmell;

import java.util.HashMap;

import org.repositoryminer.metrics.codemetric.CodeMetricId;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class FeatureEnvy extends CodeSmell {

	private static final CodeMetricId[] REQUIRED_METRICS = { CodeMetricId.LAA, CodeMetricId.ATFD, CodeMetricId.FDP };

	private float laaThreshold = 0.33f;
	private int atfdThreshold = 5;
	private int fdpThreshold = 5;

	public FeatureEnvy() {
		init();
	}

	public FeatureEnvy(float laaThreshold, int atfdThreshold, int fdpThreshold) {
		this.laaThreshold = laaThreshold;
		this.atfdThreshold = atfdThreshold;
		this.fdpThreshold = fdpThreshold;
		init();
	}

	private void init() {
		super.id = CodeSmellId.FEATURE_ENVY;
		super.requiredMetrics = REQUIRED_METRICS;
		
		super.thresholds = new HashMap<>();
		super.thresholds.put(CodeMetricId.LAA.name(), laaThreshold);
		super.thresholds.put(CodeMetricId.FDP.name(), fdpThreshold);
		super.thresholds.put(CodeMetricId.ATFD.name(), atfdThreshold);
	}
	
	@Override
	public void detect(FileReport fileReport, ProjectReport projectReport) {
		for (ClassReport cr : fileReport.getClasses()) {
			for (MethodReport mr : cr.getMethods()) {
				float laa = mr.getMetricsReport().getCodeMetric(CodeMetricId.LAA, Float.class);
				int fdp = mr.getMetricsReport().getCodeMetric(CodeMetricId.FDP, Integer.class);
				int atfd = mr.getMetricsReport().getCodeMetric(CodeMetricId.ATFD, Integer.class);
				if (detect(laa, atfd, fdp)) {
					mr.getMetricsReport().setCodeSmell(CodeSmellId.FEATURE_ENVY);
				}
			}
		}
	}

	private boolean detect(float laaValue, int atfdValue, int fdpValue) {
		return (atfdValue > atfdThreshold) && (laaValue < laaThreshold) && (fdpValue <= fdpThreshold);
	}

	/*** GETTERS AND SETTERS ***/

	public float getLaaThreshold() {
		return laaThreshold;
	}

	public void setLaaThreshold(float laaThreshold) {
		this.laaThreshold = laaThreshold;
	}

	public int getAtfdThreshold() {
		return atfdThreshold;
	}

	public void setAtfdThreshold(int atfdThreshold) {
		this.atfdThreshold = atfdThreshold;
	}

	public int getFdpThreshold() {
		return fdpThreshold;
	}

	public void setFdpThreshold(int fdpThreshold) {
		this.fdpThreshold = fdpThreshold;
	}

}