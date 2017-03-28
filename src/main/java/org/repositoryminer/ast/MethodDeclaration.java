package org.repositoryminer.ast;

import java.util.ArrayList;
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
	
	private boolean returnPrimitive;
	private boolean returnArray;
	private boolean returnGeneric;
	private boolean returnParametrized;
	
	private String returnArrayType;
	
	
	
	public String getReturnArrayType() {
		return returnArrayType;
	}

	public void setReturnArrayType(String returnArrayType) {
		this.returnArrayType = returnArrayType;
	}

	private List<String> returnParameters = new ArrayList<>();
	

	public boolean isReturnPrimitive() {
		return returnPrimitive;
	}

	public void setReturnPrimitive(boolean returnPrimitive) {
		this.returnPrimitive = returnPrimitive;
	}

	public boolean isReturnArray() {
		return returnArray;
	}

	public void setReturnArray(boolean returnArray) {
		this.returnArray = returnArray;
	}

	public boolean isReturnGeneric() {
		return returnGeneric;
	}

	public void setReturnGeneric(boolean returnGeneric) {
		this.returnGeneric = returnGeneric;
	}

	public boolean isReturnParametrized() {
		return returnParametrized;
	}

	public void setReturnParametrized(boolean returnParametrized) {
		this.returnParametrized = returnParametrized;
	}

	public List<String> getReturnParameters() {
		return returnParameters;
	}

	public void setReturnParameters(List<String> returnParameters) {
		this.returnParameters = returnParameters;
	}

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
	
	public void addReturnParameterType(String qualifiedName) {
		this.returnParameters.add(qualifiedName);
	}


}