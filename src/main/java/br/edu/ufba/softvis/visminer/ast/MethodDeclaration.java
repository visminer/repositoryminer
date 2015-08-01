package br.edu.ufba.softvis.visminer.ast;

import java.util.List;

public class MethodDeclaration {

	private int id;
	private String name;
	private String modifier;
	private String returnType;
	private List<Statement> statements;
	private List<ParameterDeclaration> parameters;
	private boolean isConstructor;
	private boolean isVarargs;
	private List<String> thrownsExceptions;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}
	/**
	 * @param modifier the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	/**
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}
	/**
	 * @param returnType the returnType to set
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
	 * @param statements the statements to set
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
	 * @param parameters the parameters to set
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
	 * @param isConstructor the isConstructor to set
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
	 * @param isVarargs the isVarargs to set
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
	 * @param thrownsExceptions the thrownsExceptions to set
	 */
	public void setThrownsExceptions(List<String> thrownsExceptions) {
		this.thrownsExceptions = thrownsExceptions;
	}

}
