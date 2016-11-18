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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<Statement> getStatements() {
		return statements;
	}

	public void setStatements(List<Statement> statements) {
		this.statements = statements;
	}

	public List<ParameterDeclaration> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterDeclaration> parameters) {
		this.parameters = parameters;
	}

	public boolean isConstructor() {
		return isConstructor;
	}

	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}

	public boolean isVarargs() {
		return isVarargs;
	}

	public void setVarargs(boolean isVarargs) {
		this.isVarargs = isVarargs;
	}

	public List<String> getThrownsExceptions() {
		return thrownsExceptions;
	}

	public void setThrownsExceptions(List<String> thrownsExceptions) {
		this.thrownsExceptions = thrownsExceptions;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public int getStartPositionInSourceCode() {
		return startPositionInSourceCode;
	}

	public void setStartPositionInSourceCode(int startPositionInSourceCode) {
		this.startPositionInSourceCode = startPositionInSourceCode;
	}

	public int getEndPositionInSourceCode() {
		return endPositionInSourceCode;
	}

	public void setEndPositionInSourceCode(int endPositionInSourceCode) {
		this.endPositionInSourceCode = endPositionInSourceCode;
	}

	public void setMaxNestssdfsdfinsg(int maxNesting) {
		this.maxNesting = maxNesting;
	}

	public int getMaxsdfdsfdsfdsNesting() {
		return maxNesting;
	}

}