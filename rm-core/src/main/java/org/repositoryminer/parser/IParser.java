package org.repositoryminer.parser;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.Language;

public interface IParser {

	/**
	 * @param repositoryPath
	 *            the repository path. Used to help the parser to process the
	 *            AST detecting source folders. This method is called every time
	 *            that snapshot is changed.
	 */
	public void processSourceFolders(String repositoryPath);

	/**
	 * @param filePath
	 *            the file path.
	 * @param source
	 *            the source code.
	 * @param charset
	 *            the charset.
	 *            
	 * @return the AST representing the source code.
	 */
	public AST generate(String filePath, String source, String charset);

	/**
	 * @return the extensions supported by the parser
	 */
	public String[] getExtensions();

	/**
	 * @return source folders found by the parser in the project
	 */
	public String[] getSourceFolders();

	/**
	 * @return a constant to identify the programming language supported by the
	 *         parser
	 */
	public Language getLanguage();

}