package org.repositoryminer.ast;

import java.util.List;

public class MethodDeclaration {

	private String name;
	private List<String> modifiers;
	private String returnType;
	private List<Statement> statements;
	private List<ParameterDeclaration> parameters;
	private int startPositionInSourceCode;
	private int endPositionInSourceCode;
	private boolean isConstructor;
	private boolean isVarargs;
	private List<String> thrownsExceptions;
	private int maxNesting;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * @param returnType
	 *            the returnType to set
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the statements
	 */
	public List<Statement> getStatements() {
		return statements;
	}

	/**
	 * @param statements
	 *            the statements to set
	 */
	public void setStatements(List<Statement> statements) {
		this.statements = statements;
	}

	/**
	 * @return the parameters
	 */
	public List<ParameterDeclaration> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(List<ParameterDeclaration> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the isConstructor
	 */
	public boolean isConstructor() {
		return isConstructor;
	}

	/**
	 * @param isConstructor
	 *            the isConstructor to set
	 */
	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}

	/**
	 * @return the isVarargs
	 */
	public boolean isVarargs() {
		return isVarargs;
	}

	/**
	 * @param isVarargs
	 *            the isVarargs to set
	 */
	public void setVarargs(boolean isVarargs) {
		this.isVarargs = isVarargs;
	}

	/**
	 * @return the thrownsExceptions
	 */
	public List<String> getThrownsExceptions() {
		return thrownsExceptions;
	}

	/**
	 * @param thrownsExceptions
	 *            the thrownsExceptions to set
	 */
	public void setThrownsExceptions(List<String> thrownsExceptions) {
		this.thrownsExceptions = thrownsExceptions;
	}

	/**
	 * @return the modifiers
	 */
	public List<String> getModifiers() {
		return modifiers;
	}

	/**
	 * @param modifiers
	 *            the modifiers to set
	 */
	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	/**
	 * @return the start position of the method declartion in the source code
	 */
	public int getStartPositionInSourceCode() {
		return startPositionInSourceCode;
	}

	/**
	 * @param startPositionInSourceCode
	 *            index to the start position of the method declartion in the
	 *            source code
	 * 
	 */
	public void setStartPositionInSourceCode(int startPositionInSourceCode) {
		this.startPositionInSourceCode = startPositionInSourceCode;
	}

	/**
	 * @return the final position of the method declaration in the source code
	 */
	public int getEndPositionInSourceCode() {
		return endPositionInSourceCode;
	}

	/**
	 * @param endPositionInSourceCode
	 *            final position of the method declaration in the source code
	 * 
	 */
	public void setEndPositionInSourceCode(int endPositionInSourceCode) {
		this.endPositionInSourceCode = endPositionInSourceCode;
	}
	

	/**
	 * @param maxNesting
	 *            the max nesting of statements within the method
	 */
	public void setMaxNesting(int maxNesting) {
		this.maxNesting = maxNesting;
	}

	/**
	 * @return the max nesting of statements within the method
	 */
	public int getMaxNesting() {
		return maxNesting;
	}

}
