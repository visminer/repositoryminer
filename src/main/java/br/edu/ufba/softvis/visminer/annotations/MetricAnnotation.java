package br.edu.ufba.softvis.visminer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version 0.9
 * Annotations to define informations about metrics.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MetricAnnotation {

	/**
	 * @return Metric name.
	 */
	public String name();

	/**
	 * @return Metric description.
	 */
	public String description();

	/**
	 * @return Metric acronym
	 */
	public String acronym();
}
