package br.edu.ufba.softvis.visminer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface VisMinerMetric {

	public enum Target {
		FILE, CLASS, METHOD
	}

	public String name();

	public String description();

	public boolean on();

	public Target target();

}
