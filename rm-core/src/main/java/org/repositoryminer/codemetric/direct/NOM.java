package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class NOM implements IDirectCodeMetric {

	@Override
	public Object calculateFromFile(AST ast) {
		return ast.getMethods().size();
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		return type.getMethods().size();
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return null;
	}

	@Override
	public String getMetric() {
		return "NOM";
	}

}