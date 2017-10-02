package org.repositoryminer.codesmell;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.metric.MetricID;

public class BrainClass implements ICodeSmell {

	private static final MetricID[] REQUIRED_METRICS = { MetricID.WMC, MetricID.TCC, MetricID.LOC };
	private static final CodeSmellID[] REQUIRED_CODESMELLS = { CodeSmellID.BRAIN_METHOD };

	private int wmcThreshold = 47;
	private float tccThreshold = 0.5f;
	private int brainMethodThreshold = 1;
	private int locThreshold = 195;

	public BrainClass() {
	}

	public BrainClass(int wmcThreshold, float tccThreshold, int brainMethodThreshold, int locThreshold) {
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
		this.brainMethodThreshold = brainMethodThreshold;
		this.locThreshold = locThreshold;
	}

	@Override
	public void detect(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			int wmc = (Integer) type.getMetrics().get(MetricID.WMC);
			float tcc = (Float) type.getMetrics().get(MetricID.TCC);
			int loc = (Integer) type.getMetrics().get(MetricID.LOC);
			int nbm = 0;

			for (AbstractMethod method : type.getMethods())
				if (method.getCodeSmells().contains(CodeSmellID.BRAIN_METHOD))
					nbm++;

			if (detect(nbm, loc, wmc, tcc))
				type.getCodeSmells().add(CodeSmellID.BRAIN_CLASS);
		}
	}

	public boolean detect(int nbm, int loc, int wmc, float tcc) {
		boolean exp1 = nbm > brainMethodThreshold && loc >= locThreshold;
		boolean exp2 = nbm == brainMethodThreshold && loc >= (2 * locThreshold) && wmc >= (2 * wmcThreshold);
		boolean exp3 = wmc >= wmcThreshold && tcc < tccThreshold;
		return (exp1 || exp2) && exp3;
	}

	@Override
	public CodeSmellID getId() {
		return CodeSmellID.BRAIN_CLASS;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return REQUIRED_METRICS;
	}

	@Override
	public CodeSmellID[] getRequiredCodeSmells() {
		return REQUIRED_CODESMELLS;
	}
	
	// *** GETTERS AND SETTERS ***//
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