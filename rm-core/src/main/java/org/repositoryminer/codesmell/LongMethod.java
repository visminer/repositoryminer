package org.repositoryminer.codesmell;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.metric.MetricID;

public class LongMethod implements ICodeSmell {

	private static final MetricID[] REQUIRED_METRICS = { MetricID.LOC };

	private int mlocThreshold = 65;

	public LongMethod() {
	}

	public LongMethod(int mlocThreshold) {
		this.mlocThreshold = mlocThreshold;
	}

	@Override
	public void detect(AST ast) {
		for (AbstractMethod method : ast.getMethods()) {
			int loc = (Integer) method.getMetrics().get(MetricID.LOC);
			if (loc > mlocThreshold)
				method.getCodeSmells().add(CodeSmellID.LONG_METHOD);
		}

		for (AbstractType type : ast.getTypes()) {
			for (AbstractMethod method : type.getMethods()) {
				int loc = (Integer) method.getMetrics().get(MetricID.LOC);
				if (loc > mlocThreshold)
					method.getCodeSmells().add(CodeSmellID.LONG_METHOD);
			}
		}
	}

	@Override
	public CodeSmellID getId() {
		return CodeSmellID.LONG_METHOD;
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

}