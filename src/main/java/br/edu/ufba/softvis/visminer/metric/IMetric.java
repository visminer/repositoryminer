package br.edu.ufba.softvis.visminer.metric;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.ast.AST;

/**
 * Metrics calculations definition.
 */
public interface IMetric {

	public void calculate(AST ast, Document document);

}
