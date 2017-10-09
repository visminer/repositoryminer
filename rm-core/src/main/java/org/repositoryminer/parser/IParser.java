package org.repositoryminer.parser;

import org.repositoryminer.ast.AST;

/**
 * This interface defines a parser to a programming language.
 */
public interface IParser {

	/**
	 * @param filename
	 *            the file path.
	 * @param source
	 *            the source code.
	 * 
	 * @return the AST representing the source code.
	 */
	public AST generate(String filename, String source);

	/**
	 * @return true if the file is supported by the parser or false otherwise
	 */
	public boolean accept(String filepath);

	/**
	 * @return the programming language supported by the parser
	 */
	public Language getLanguage();

	/**
	 * Access the repository to do some preparations (e.g. scan the repository to find source folders).
	 * 
	 * @param repositoryPath the repository path.
	 */
	public void scanRepository(String repositoryPath);

}