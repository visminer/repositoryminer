package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public interface IDirectCodeMetric {

	public Object calculateFromFile(AST ast);
	public Object calculateFromClass(AST ast, AbstractType type);
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method);
	public String getMetric();

}