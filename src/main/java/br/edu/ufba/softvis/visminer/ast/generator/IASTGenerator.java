package br.edu.ufba.softvis.visminer.ast.generator;

import br.edu.ufba.softvis.visminer.ast.AST;

public interface IASTGenerator {

	public AST generate(String filePath, String source, String[] sourceFolders);

}
