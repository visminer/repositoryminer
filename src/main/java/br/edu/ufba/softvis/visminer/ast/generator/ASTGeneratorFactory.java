package br.edu.ufba.softvis.visminer.ast.generator;

import br.edu.ufba.softvis.visminer.ast.generator.cpp.CPPASTGenerator;
import br.edu.ufba.softvis.visminer.ast.generator.java.JavaASTGenerator;
import br.edu.ufba.softvis.visminer.ast.generator.none.NoneASTGenerator;
import br.edu.ufba.softvis.visminer.constant.LanguageType;

public abstract class ASTGeneratorFactory {

	public static IASTGenerator create(LanguageType language) {
		IASTGenerator generator = null;

		if (language == LanguageType.JAVA) {
			generator = new JavaASTGenerator();
		} else if (language == LanguageType.CPP) {
			generator = new CPPASTGenerator();
		} else if (language == LanguageType.NONE) {
			generator = new NoneASTGenerator();
		}

		return generator;
	}

}
