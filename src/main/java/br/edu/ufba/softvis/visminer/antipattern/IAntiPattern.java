package br.edu.ufba.softvis.visminer.antipattern;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;

public interface IAntiPattern {
	
	public void detect(TypeDeclaration type, AST ast, Document document);
}
