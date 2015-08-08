package br.edu.ufba.softvis.visminer.ast.generator;

import br.edu.ufba.softvis.visminer.ast.generator.cpp.CPPASTGenerator;
import br.edu.ufba.softvis.visminer.ast.generator.java.JavaASTGenerator;

public abstract class ASTGeneratorFactory {

	public static IASTGenerator create(Language language) {
		IASTGenerator generator = null;

		if (language == Language.JAVA) {
			generator = new JavaASTGenerator();
		} else if (language == Language.CPP) {
			generator = new CPPASTGenerator();
		}

		return generator;
	}

}
