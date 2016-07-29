package org.repositoryminer.codesmell.clazz;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;

public interface IClassCodeSmell {
	
	public void detect(AbstractTypeDeclaration type, AST ast, Document document);
}
