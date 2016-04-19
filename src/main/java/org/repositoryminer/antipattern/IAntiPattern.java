package org.repositoryminer.antipattern;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.TypeDeclaration;

public interface IAntiPattern {
	public void detect(TypeDeclaration type, AST ast, Document document);
}
