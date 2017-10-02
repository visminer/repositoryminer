package org.repositoryminer.codesmell;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.metric.MetricID;

public class GodClass implements ICodeSmell {

	private static final MetricID[] REQUIRED_METRICS = { MetricID.ATFD, MetricID.WMC, MetricID.TCC };

	private int atfdThreshold = 5;
	private int wmcThreshold = 47;
	private float tccThreshold = 0.33f;

	public GodClass() {
	}

	public GodClass(int atfdThreshold, int wmcThreshold, float tccThreshold) {
		this.atfdThreshold = atfdThreshold;
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
	}

	@Override
	public void detect(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			int atfd = (Integer) type.getMetrics().get(MetricID.ATFD);
			int wmc = (Integer) type.getMetrics().get(MetricID.WMC);
			float tcc = (Float) type.getMetrics().get(MetricID.TCC);
			if (detect(atfd, wmc, tcc))
				type.getCodeSmells().add(CodeSmellID.GOD_CLASS);
		}
	}

	public boolean detect(int atfd, int wmc, float tcc) {
		return (atfd > atfdThreshold) && (wmc >= wmcThreshold) && (tcc < tccThreshold);
	}

	@Override
	public CodeSmellID getId() {
		return CodeSmellID.GOD_CLASS;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return REQUIRED_METRICS;
	}

	@Override
	public CodeSmellID[] getRequiredCodeSmells() {
		return null;
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

	public float getTccThreshold() {
		return tccThreshold;
	}

	public void setTccThreshold(float tccThreshold) {
		this.tccThreshold = tccThreshold;
	}

}