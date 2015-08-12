package br.edu.ufba.softvis.visminer.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import br.edu.ufba.softvis.visminer.constant.Language;

@Retention(RetentionPolicy.RUNTIME)
public @interface ASTGeneratorAnnotation {

	public Language language();

	public String[] extensions();
	
}
