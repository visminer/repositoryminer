package org.repositoryminer.codesmell;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.metric.MetricID;

public class BrainMethod implements ICodeSmell {

	private static final MetricID[] REQUIRED_METRICS = { MetricID.LOC, MetricID.CYCLO, MetricID.NOAV,
			MetricID.MAXNESTING };

	private int mlocThreshold = 65;
	private float ccThreshold = 7;
	private int maxNestingThreshold = 5;
	private int noavThreshold = 8;

	public BrainMethod() {
	}

	public BrainMethod(int mlocThreshold, float ccThreshold, int maxNestingThreshold, int noavThreshold) {
		this.mlocThreshold = mlocThreshold;
		this.ccThreshold = ccThreshold;
		this.maxNestingThreshold = maxNestingThreshold;
		this.noavThreshold = noavThreshold;
	}

	@Override
	public void detect(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			for (AbstractMethod method : type.getMethods()) {
				int mloc = (Integer) method.getMetrics().get(MetricID.LOC);
				int cyclo = (Integer) method.getMetrics().get(MetricID.CYCLO);
				int noav = (Integer) method.getMetrics().get(MetricID.NOAV);
				int maxnesting = (Integer) method.getMetrics().get(MetricID.MAXNESTING);
				if (detect(cyclo, mloc, noav, maxnesting))
					method.getCodeSmells().add(CodeSmellID.BRAIN_METHOD);
			}
		}
	}

	public boolean detect(int cc, int mloc, int noav, int maxNesting) {
		return mloc > mlocThreshold && cc > ccThreshold && maxNesting >= maxNestingThreshold && noav > noavThreshold;
	}

	@Override
	public CodeSmellID getId() {
		return CodeSmellID.BRAIN_METHOD;
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

	public int getMlocThreshold() {
		return mlocThreshold;
	}

	public void setMlocThreshold(int mlocThreshold) {
		this.mlocThreshold = mlocThreshold;
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