package org.repositoryminer.codesmell;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.metric.MetricID;

public class FeatureEnvy implements ICodeSmell {

	private static final MetricID[] REQUIRED_METRICS = { MetricID.LAA, MetricID.ATFD, MetricID.FDP };

	private float laaThreshold = 0.33f;
	private int atfdThreshold = 5;
	private int fdpThreshold = 5;

	public FeatureEnvy() {
	}

	public FeatureEnvy(float laaThreshold, int atfdThreshold, int fdpThreshold) {
		this.laaThreshold = laaThreshold;
		this.atfdThreshold = atfdThreshold;
		this.fdpThreshold = fdpThreshold;
	}

	@Override
	public void detect(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			for (AbstractMethod method : type.getMethods()) {
				float laa = (Float) method.getMetrics().get(MetricID.LAA);
				int fdp = (Integer) method.getMetrics().get(MetricID.FDP);
				int atfd = (Integer) method.getMetrics().get(MetricID.ATFD);
				if (detect(laa, atfd, fdp))
					method.getCodeSmells().add(CodeSmellID.FEATURE_ENVY);
			}
		}
	}

	private boolean detect(float laaValue, int atfdValue, int fdpValue) {
		return (atfdValue > atfdThreshold) && (laaValue < laaThreshold) && (fdpValue <= fdpThreshold);
	}

	@Override
	public CodeSmellID getId() {
		return CodeSmellID.FEATURE_ENVY;
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