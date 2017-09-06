package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public interface IDirectCodeSmell {

	public boolean calculateFromFile(AST ast);
	public boolean calculateFromClass(AST ast, AbstractType type);
	public boolean calculateFromMethod(AST ast, AbstractType type, AbstractMethod method);
	public String getCodeSmell();
	
}