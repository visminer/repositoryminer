package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.MetricId;

@DirectCodeSmellProperties(id = CodeSmellId.BRAIN_CLASS, requisites = { CodeSmellId.BRAIN_METHOD }, metrics = {
		MetricId.WMC, MetricId.TCC, MetricId.LOC })
public class BrainClass implements IDirectCodeSmell {

	private int wmcThreshold = 47;
	private float tccThreshold = 0.5f;
	private int brainMethodThreshold = 1;
	private int locThreshold = 195;

	@Override
	public void detect(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			int wmc = (Integer) type.getMetrics().get(MetricId.WMC);
			float tcc = (Float) type.getMetrics().get(MetricId.TCC);
			int loc = (Integer) type.getMetrics().get(MetricId.LOC);
			int nbm = 0;

			for (AbstractMethod method : type.getMethods()) {
				if (method.getCodeSmells().contains(CodeSmellId.BRAIN_METHOD)) {
					nbm++;
				}
			}

			if (detect(nbm, loc, wmc, tcc)) {
				type.getCodeSmells().add(CodeSmellId.BRAIN_CLASS);
			}
		}
	}

	public boolean detect(int nbm, int loc, int wmc, float tcc) {
		// Class contains more than one Brain Method and is very large
		boolean exp1 = nbm > brainMethodThreshold && loc >= locThreshold;

		// Class contains only one BrainMethod but is extremely large and
		// complex
		boolean exp2 = nbm == brainMethodThreshold && loc >= (2 * locThreshold) && wmc >= (2 * wmcThreshold);

		// Class is very complex and non-cohesive
		boolean exp3 = wmc >= wmcThreshold && tcc < tccThreshold;

		return (exp1 || exp2) && exp3;
	}

}
