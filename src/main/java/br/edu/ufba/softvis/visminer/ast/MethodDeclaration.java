package br.edu.ufba.softvis.visminer.ast;

import java.util.List;

public class MethodDeclaration {

	private int id;
	private String name;
	private List<Statement> statements;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<Statement> getStatements() {
		return statements;
	}

	public void setStatements(List<Statement> statements) {
		this.statements = statements;
	}
	
}
