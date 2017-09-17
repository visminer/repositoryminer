package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.CYCLO;

public class ComplexMethod implements IDirectCodeSmell {

	private CYCLO cycloMetric = new CYCLO();

	private int cycloThreshold = 10;

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
		int cyclo = (Integer) cycloMetric.calculateFromMethod(ast, type, method);
		return cyclo > cycloThreshold;
	}

	@Override
	public String getCodeSmell() {
		return "COMPLEX METHOD";
	}

}