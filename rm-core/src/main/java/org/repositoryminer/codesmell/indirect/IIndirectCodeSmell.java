package org.repositoryminer.codesmell.indirect;

import java.util.Map;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;

/**
 * This interface defines how to implement code smell detection in class level.
 */
public interface IIndirectCodeSmell {

	/**
	 * Activates the code smell detection
	 * 
	 * @param type
	 *            the abstract representation of a type (
	 *            {@link org.repositoryminer.ast.AbstractClassDeclaration}).
	 * @param ast
	 *            an instance of an abstract syntactic tree (
	 *            {@link org.repositoryminer.ast.AST})
	 */
	public void detect(AbstractClassDeclaration type, AST ast);
	
	/**
	 * @return The code smell ID
	 */
	public CodeSmellId getId();
	
	/**
	 * @return The thresholds used to detect the codesmell
	 */
	public Document getThresholds();
	
	/**
	 * @return The result of the metric after process all the source files. This
	 *         method ought to return a map containing the canonical class name as
	 *         key and a document with the metric value of the class used as key.
	 */
	public Map<String, Document> getResult();
	
}