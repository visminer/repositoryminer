package org.repositoryminer.metrics.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.metrics.codemetric.CodeMetricId;
import org.repositoryminer.metrics.codesmell.CodeSmellId;

public class MetricsReport {

	private Map<CodeMetricId, Number> codeMetrics = new HashMap<>();
	private Set<CodeSmellId> codeSmells = new HashSet<>();

	public List<Document> toMetricsDocument() {
		List<Document> documents = new ArrayList<Document>();
		for (Entry<CodeMetricId, Number> entry : codeMetrics.entrySet()) {
			documents.add(new Document("name", entry.getKey().name()).append("value", entry.getValue()));
		}
		return documents;
	}

	public List<String> getCodeSmellsAsString() {
		List<String> list = new ArrayList<String>();
		for (CodeSmellId id : codeSmells) {
			list.add(id.name());
		}
		return list;
	}

	public boolean hasCodeSmell(CodeSmellId id) {
		return codeSmells.contains(id);
	}

	public boolean hasCodeMetric(CodeMetricId id) {
		return codeMetrics.containsKey(id);
	}

	public Number getCodeMetric(CodeMetricId id) {
		return codeMetrics.get(id);
	}

	public <T extends Number> T getCodeMetric(CodeMetricId id, Class<T> type) {
		return type.cast(codeMetrics.get(id));
	}

	public void setCodeMetric(CodeMetricId id, Number value) {
		codeMetrics.put(id, value);
	}

	public void setCodeSmell(CodeSmellId id) {
		codeSmells.add(id);
	}

	public void addOneToCodeMetric(CodeMetricId id) {
		updateMetric(id, 1);
	}

	public void updateMetric(CodeMetricId id, int value) {
		int newValue = value;
		if (codeMetrics.containsKey(id)) {
			newValue += codeMetrics.get(id).intValue();
		}
		codeMetrics.put(id, newValue);
	}

	public void updateMetric(CodeMetricId id, long value) {
		long newValue = value;
		if (codeMetrics.containsKey(id)) {
			newValue += codeMetrics.get(id).longValue();
		}
		codeMetrics.put(id, newValue);
	}

	public void updateMetric(CodeMetricId id, float value) {
		float newValue = value;
		if (codeMetrics.containsKey(id)) {
			newValue += codeMetrics.get(id).floatValue();
		}
		codeMetrics.put(id, newValue);
	}

	public void updateMetric(CodeMetricId id, double value) {
		double newValue = value;
		if (codeMetrics.containsKey(id)) {
			newValue += codeMetrics.get(id).doubleValue();
		}
		codeMetrics.put(id, newValue);
	}

	public Map<CodeMetricId, Number> getCodeMetrics() {
		return codeMetrics;
	}

	public void setCodeMetrics(Map<CodeMetricId, Number> codeMetrics) {
		this.codeMetrics = codeMetrics;
	}

	public Set<CodeSmellId> getCodeSmells() {
		return codeSmells;
	}

	public void setCodeSmells(Set<CodeSmellId> codeSmells) {
		this.codeSmells = codeSmells;
	}

}