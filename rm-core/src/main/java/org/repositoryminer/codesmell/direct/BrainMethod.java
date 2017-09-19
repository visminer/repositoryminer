package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.MetricId;

@DirectCodeSmellProperties(id = CodeSmellId.BRAIN_METHOD, metrics = {MetricId.LOC, MetricId.CYCLO, MetricId.NOAV, MetricId.MAXNESTING})
public class BrainMethod implements IDirectCodeSmell {

	private int mlocThreshold = 65;
	private float ccThreshold = 7;
	private int maxNestingThreshold = 5;
	private int noavThreshold = 8;

	@Override
	public void detect(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			for (AbstractMethod method : type.getMethods()) {
				int mloc = (Integer) method.getMetrics().get(MetricId.LOC);
				int cyclo = (Integer) method.getMetrics().get(MetricId.CYCLO);
				int noav = (Integer) method.getMetrics().get(MetricId.NOAV);
				int maxnesting= (Integer) method.getMetrics().get(MetricId.MAXNESTING);
				if (detect(cyclo, mloc, noav, maxnesting)) {
					method.getCodeSmells().add(CodeSmellId.BRAIN_METHOD);
				}
			}
		}
	}
	
	public boolean detect(int cc, int mloc, int noav, int maxNesting) {
		return mloc > mlocThreshold && cc > ccThreshold && maxNesting >= maxNestingThreshold
				&& noav > noavThreshold;
	}

}