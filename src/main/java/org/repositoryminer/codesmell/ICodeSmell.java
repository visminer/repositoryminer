package org.repositoryminer.codesmell;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;

public interface ICodeSmell {
	
	public void detect(AbstractTypeDeclaration type, AST ast, Document document);
}
