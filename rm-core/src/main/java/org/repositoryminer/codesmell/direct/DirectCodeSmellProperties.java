package org.repositoryminer.codesmell.direct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.repositoryminer.codemetric.direct.MetricId;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DirectCodeSmellProperties {

	CodeSmellId id();
	CodeSmellId[] requisites() default {};
	MetricId[] metrics() default {};
	
}