package org.repositoryminer.parser;

import org.repositoryminer.ast.AST;

/**
 * This interface defines a parser to a programming language.
 */
public interface IParser {

	/**
	 * @param project
	 *            the project path.
	 * @param filename
	 *            the file path.
	 * @param source
	 *            the source code.
	 * @param charset
	 *            the source code charset.
	 * 
	 * @return the AST representing the source code.
	 */
	public AST generate(String filename, String source, String charset);

	/**
	 * @return true if the file is supported by the parser or false otherwise
	 */
	public boolean accept(String filepath);

	/**
	 * @return the programming language supported by the parser
	 */
	public String getLanguage();

	/**
	 * Scans the repository to find source folders.
	 * 
	 * @param project
	 *            the project path.
	 */
	public void scanRepository(String repositoryPath);

}