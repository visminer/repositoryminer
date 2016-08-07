package org.repositoryminer.codesmell.clazz;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;

/**
 * This interface defines how to implement code smell detection in class level.
 */
public interface IClassCodeSmell {
	
	public void detect(AbstractTypeDeclaration type, AST ast, Document document);
}
