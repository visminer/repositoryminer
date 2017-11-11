package org.repositoryminer.metrics;

import java.util.List;

import org.repositoryminer.metrics.codemetric.CodeMetric;
import org.repositoryminer.metrics.codesmell.CodeSmell;
import org.repositoryminer.metrics.parser.Parser;

public class MetricsConfig {

	private List<Parser> parsers;
	private List<CodeMetric> codeMetrics;
	private List<CodeSmell> codeSmells;

	public MetricsConfig() {}

	public MetricsConfig(List<Parser> parsers, List<CodeMetric> codeMetrics, List<CodeSmell> codeSmells) {
		this.parsers = parsers;
		this.codeMetrics = codeMetrics;
		this.codeSmells = codeSmells;
	}

	public boolean isValid() {
		return isValidParam(parsers) && (isValidParam(codeMetrics) || isValidParam(codeSmells));
	}

	private boolean isValidParam(List<?> list) {
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	public List<Parser> getParsers() {
		return parsers;
	}

	public void setParsers(List<Parser> parsers) {
		this.parsers = parsers;
	}

	public List<CodeMetric> getCodeMetrics() {
		return codeMetrics;
	}

	public void setCodeMetrics(List<CodeMetric> codeMetrics) {
		this.codeMetrics = codeMetrics;
	}

	public List<CodeSmell> getCodeSmells() {
		return codeSmells;
	}

	public void setCodeSmells(List<CodeSmell> codeSmells) {
		this.codeSmells = codeSmells;
	}

}