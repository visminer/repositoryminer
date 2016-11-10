package org.repositoryminer.codesmell.project;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.parser.Parser;

/**
 * This interface defines how to implement code smell detection in project level.
 */
public interface IProjectCodeSmell {

	/**
	 * Activates the code smell detection
	 * 
	 * @param parsers
	 * 				the source code parsers ({@link org.repositoryminer.parser.Parser}})
	 * @param repositoryPath
	 * 				the repository path
	 * @param document
	 * 				the root of mongodb document in which metrics values must be
	 *            	filled
	 */
	public void detect(List<Parser> parsers, String repositoryPath, Document document);

	/**
	 * @return The code smell ID
	 */
	public String getId();
	
}