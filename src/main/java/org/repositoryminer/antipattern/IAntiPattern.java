package org.repositoryminer.antipattern;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;

public interface IAntiPattern {
	public void detect(AbstractTypeDeclaration type, AST ast, Document document);
}
