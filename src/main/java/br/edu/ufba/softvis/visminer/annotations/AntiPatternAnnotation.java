package br.edu.ufba.softvis.visminer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AntiPatternAnnotation {
	
	/**
	 * @return AntiPattern name.
	 */
	public String name();

	/**
	 * @return AntiPattern description.
	 */
	public String description();

}
