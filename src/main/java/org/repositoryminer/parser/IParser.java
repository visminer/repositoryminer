package org.repositoryminer.parser;

import java.util.Set;

import org.repositoryminer.ast.AST;

public interface IParser {

	/**
	 * @return the extensions accepted by the parser.
	 */
	public Set<String> getExtensions();
	
	/**
	 * @param repositoryPath the repository path.
	 * Used to help the parser to process the AST detecting source folders.
	 * This method is called every time that snapshot is changed.
	 */
	public void setSourceFolders(String repositoryPath);
	
	/**
	 * @param filePath the file path.
	 * @param source the source code.
	 * @return the AST representing the source code.
	 */
	public AST generate(String filePath, String source);
	
}
