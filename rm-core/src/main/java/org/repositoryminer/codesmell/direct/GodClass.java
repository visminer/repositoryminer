package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.MetricId;

@DirectCodeSmellProperties(id = CodeSmellId.GOD_CLASS, metrics = { MetricId.ATFD, MetricId.WMC, MetricId.TCC })
public class GodClass implements IDirectCodeSmell {

	private int atfdThreshold = 5;
	private int wmcThreshold = 47;
	private float tccThreshold = 0.33f;

	@Override
	public void detect(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			int atfd = (Integer) type.getMetrics().get(MetricId.ATFD);
			int wmc = (Integer) type.getMetrics().get(MetricId.WMC);
			float tcc = (Float) type.getMetrics().get(MetricId.TCC);
			if (detect(atfd, wmc, tcc)) {
				type.getCodeSmells().add(CodeSmellId.GOD_CLASS);
			}
		}
	}

	public boolean detect(int atfd, int wmc, float tcc) {
		return (atfd > atfdThreshold) && (wmc >= wmcThreshold) && (tcc < tccThreshold);
	}

}