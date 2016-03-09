package br.edu.ufba.softvis.visminer.metric;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;

/**
 * Metrics calculations definition.
 */
public interface IMetric {

	public void calculate(TypeDeclaration type, AST ast, Document document);

}
