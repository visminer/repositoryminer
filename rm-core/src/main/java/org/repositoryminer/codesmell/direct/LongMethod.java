package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.LOC;

public class LongMethod implements IDirectCodeSmell {

	private LOC locMetric = new LOC();
	
	private int mlocThreshold = 65;
	
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
		int loc = (Integer) locMetric.calculateFromMethod(ast, type, method);
		return loc > mlocThreshold;
	}

	@Override
	public String getCodeSmell() {
		return "LONG METHOD";
	}

}
