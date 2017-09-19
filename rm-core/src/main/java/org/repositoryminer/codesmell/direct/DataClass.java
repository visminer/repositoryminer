package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.MetricId;

@DirectCodeSmellProperties(id = CodeSmellId.DATA_CLASS, metrics = {MetricId.WOC, MetricId.NOPA, MetricId.NOAM, MetricId.WMC})
public class DataClass implements IDirectCodeSmell {

	private float wocThreshold = 0.33f;
	private int wmcThreshold1 = 31;
	private int wmcThreshold2 = 47;
	private int publicMembersThreshold1 = 5;
	private int publicMembersThreshold2 = 8;
	
	@Override
	public void detect(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			float woc = (Float) type.getMetrics().get(MetricId.WOC);
			int nopa = (Integer) type.getMetrics().get(MetricId.NOPA);
			int noam = (Integer) type.getMetrics().get(MetricId.NOAM);
			int wmc = (Integer) type.getMetrics().get(MetricId.WMC);
			if (detect(woc, nopa, noam, wmc)) {
				type.getCodeSmells().add(CodeSmellId.DATA_CLASS);
			}
		}
	}
	
	private boolean detect(float woc, int nopa, int noam, int wmc) {
		int publicMembers = nopa + noam;
		boolean offerManyData = woc < wocThreshold;
		boolean isNotComplex = (publicMembers > publicMembersThreshold1 && wmc < wmcThreshold1)
				|| (publicMembers > publicMembersThreshold2 && wmc < wmcThreshold2);
		return offerManyData && isNotComplex;
	}

}
