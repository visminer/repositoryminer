package org.repositoryminer.codesmell.clazz;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;

/**
 * This interface defines how to implement code smell detection in class level.
 */
public interface IClassCodeSmell {
	
	/**
	 * Activates the code smell detection
	 * 
	 * @param type
	 *            the abstract representation of a type (
	 *            {@link org.repositoryminer.ast.AbstractTypeDeclaration}).
	 * @param ast
	 *            an instance of an abstract syntactic tree (
	 *            {@link org.repositoryminer.ast.AST})
	 * @param document
	 *            the root of mongodb document in which metrics values must be
	 *            filled
	 */
	public void detect(AbstractTypeDeclaration type, AST ast, Document document);
	
	/**
	 * @return The code smell ID
	 */
	public CodeSmellId getId();
	
}