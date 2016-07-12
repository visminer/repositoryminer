package org.repositoryminer.parser;

import java.util.List;
import java.util.Set;

import org.repositoryminer.ast.AST;

public abstract class Parser {

	protected List<String> sourceFolders;
	protected Set<String> extensions;
	protected String charset;
	
	/**
	 * @param repositoryPath the repository path.
	 * Used to help the parser to process the AST detecting source folders.
	 * This method is called every time that snapshot is changed.
	 */
	public abstract void processSourceFolders(String repositoryPath);
	
	/**
	 * @param filePath the file path.
	 * @param source the source code.
	 * @return the AST representing the source code.
	 */
	public abstract AST generate(String filePath, String source);
	
	/**
	 * @return String representing the language.
	 */
	public abstract String getLanguage();
	
	public Set<String> getExtensions() {
		return extensions;
	}
	
	public void setCharSet(String charset){
		this.charset = charset;
	}
	
	public String getCharset() {
		return this.charset;
	}
	
	public List<String> getSourceFolders(){
		return this.sourceFolders;
	}
	
}