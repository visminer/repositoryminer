package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.LOC;
import org.repositoryminer.codemetric.direct.TCC;
import org.repositoryminer.codemetric.direct.WMC;

public class BrainClass implements IDirectCodeSmell {

	private WMC wmcMetric = new WMC();
	private BrainMethod brainMethodCodeSmell = new BrainMethod();
	private TCC tccMetric = new TCC();
	private LOC locMetric = new LOC();

	private int wmcThreshold = 47;
	private float tccThreshold = 0.5f;
	private int brainMethodThreshold = 1;
	private int locThreshold = 195;

	@Override
	public boolean calculateFromFile(AST ast) {
		return false;
	}

	@Override
	public boolean calculateFromClass(AST ast, AbstractType type) {
		int wmc = (Integer) wmcMetric.calculateFromClass(ast, type);
		float tcc = (Float) tccMetric.calculateFromClass(ast, type);
		int loc = (Integer) locMetric.calculateFromClass(ast, type);

		int nbm = 0;
		for (AbstractMethod method : type.getMethods()) {
			if (brainMethodCodeSmell.calculateFromMethod(ast, type, method)) {
				nbm++;
			}

			// Only two brain method are enough to satisfy the code smell detection, more
			// than this is an overhead
			if (nbm == 2) {
				break;
			}
		}
		
		return detect(nbm, loc, wmc, tcc);
	}

	@Override
	public boolean calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return false;
	}

	@Override
	public String getCodeSmell() {
		return "BRAIN CLASS";
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
