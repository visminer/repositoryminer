package br.edu.ufba.softvis.visminer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;

/**
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
	
	/**
	 * @return Metric unique id
	 * @see MetricUid
	 */
	public MetricUid uid();
	
	/**
	 * @return Metric type
	 * @see MetricType
	 */
	public MetricType type();
	
	/**
	 * @return Requisited metrics
	 */
	public MetricUid[] requisites() default {};
	
}