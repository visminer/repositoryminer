package br.edu.ufba.softvis.visminer.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import br.edu.ufba.softvis.visminer.constant.LanguageType;

@Retention(RetentionPolicy.RUNTIME)
public @interface ASTGeneratorAnnotation {

	public LanguageType language();

	public String[] extensions();
	
}
