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
	 * @return the document with the data to persist in database
	 */
	public Document detect(AbstractTypeDeclaration type, AST ast);
	
	/**
	 * @return The code smell ID
	 */
	public CodeSmellId getId();
	
	/**
	 * @return The thresholds used to detect the codesmell
	 */
	public Document getThresholds();
	
}