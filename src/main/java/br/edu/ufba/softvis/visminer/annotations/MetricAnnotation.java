package br.edu.ufba.softvis.visminer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.edu.ufba.softvis.visminer.constant.MetricId;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MetricAnnotation {

	public String name();

	public String description();

	public String acronym();
	
	public MetricId id();
	
}
