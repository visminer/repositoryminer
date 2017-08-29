package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class MAXNESTING implements IDirectCodeMetric {

	@Override
	public Object calculateFromFile(AST ast) {
		return null;
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		return null;
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return method.getMaxDepth();
	}

	@Override
	public String getMetric() {
		return "MAXNESTING";
	}

}