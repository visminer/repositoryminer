package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.CYCLO;
import org.repositoryminer.codemetric.direct.LOC;
import org.repositoryminer.codemetric.direct.NOAV;

public class BrainMethod implements IDirectCodeSmell {

	private LOC locMetric = new LOC();
	private CYCLO ccMetric = new CYCLO();
	private NOAV noavMetric = new NOAV();

	private int mlocThreshold = 65;
	private float ccThreshold = 7;
	private int maxNestingThreshold = 5;
	private int noavThreshold = 8;

	@Override
	public boolean calculateFromFile(AST ast) {
		return false;
	}

	@Override
	public boolean calculateFromClass(AST ast, AbstractType type) {
		return false;
	}

	@Override
	public boolean calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		int mloc = (Integer) locMetric.calculateFromMethod(ast, type, method);
		int cyclo = (Integer) ccMetric.calculateFromMethod(ast, type, method);
		int noav = (Integer) noavMetric.calculateFromMethod(ast, type, method);
		int maxnesting = method.getMaxDepth();
		return detect(cyclo, mloc, noav, maxnesting);
	}

	@Override
	public String getCodeSmell() {
		return "BRAIN METHOD";
	}

	public boolean detect(int cc, int mloc, int noav, int maxNesting) {
		return mloc > mlocThreshold && cc > ccThreshold && maxNesting >= maxNestingThreshold
				&& noav > noavThreshold;
	}

}
