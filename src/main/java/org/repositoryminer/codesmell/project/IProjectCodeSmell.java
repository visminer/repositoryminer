package org.repositoryminer.codesmell.project;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.parser.IParser;

/**
 * This interface defines how to implement code smell detection in project
 * level.
 */
public interface IProjectCodeSmell {

	/**
	 * Activates the code smell detection
	 * 
	 * @param parsers
	 *            the source code parsers
	 *            ({@link org.repositoryminer.parser.IParser}}).
	 * @param repositoryPath
	 *            the repository path.
	 * @param charset
	 *            the charset.
	 * @param document
	 *            the root of mongodb document in which metrics values must be
	 *            filled.
	 */
	public void detect(List<IParser> parsers, String repositoryPath, String charset, Document document);

	/**
	 * @return The code smell ID
	 */
	public String getId();

}