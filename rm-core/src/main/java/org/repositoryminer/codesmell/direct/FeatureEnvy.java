package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.MetricId;

@DirectCodeSmellProperties(id = CodeSmellId.FEATURE_ENVY, metrics = {MetricId.LAA, MetricId.ATFD, MetricId.FDP })
public class FeatureEnvy implements IDirectCodeSmell {

	private float laaThreshold = 0.33f;
	private int atfdThreshold = 5;
	private int fdpThreshold = 5;

	@Override
	public void detect(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			for (AbstractMethod method : type.getMethods()) {
				float laa = (Float) method.getMetrics().get(MetricId.LAA);
				int fdp = (Integer) method.getMetrics().get(MetricId.FDP);
				int atfd = (Integer) method.getMetrics().get(MetricId.ATFD);
				if (detect(laa, atfd, fdp)) {
					method.getCodeSmells().add(CodeSmellId.FEATURE_ENVY);
				}
			}
		}
	}

	/*
	 * Detection Strategy for Feature Envy (ATFD > FEW) AND (LAA < ONE THIRD) AND
	 * (FDP <= FEW)
	 */
	private boolean detect(float laaValue, int atfdValue, int fdpValue) {
		return (atfdValue > atfdThreshold) && (laaValue < laaThreshold) && (fdpValue <= fdpThreshold);
	}

}