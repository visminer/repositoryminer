package org.repositoryminer.codesmell.project;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.codesmell.CodeSmellId;
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
	 * @return the document with the data to persist in database
	 */
	public Document detect(List<IParser> parsers, String repositoryPath, String charset);

	/**
	 * @return The code smell ID
	 */
	public CodeSmellId getId();

}