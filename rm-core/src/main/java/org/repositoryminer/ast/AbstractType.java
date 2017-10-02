package org.repositoryminer.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.repositoryminer.codesmell.CodeSmellID;
import org.repositoryminer.metric.MetricID;

/**
 * This class represents an abstract type declaration.
 * 
 * @see AbstractClass
 */
public class AbstractType {

	private String name;
	private int startPosition;
	private int endPosition;
	private NodeType nodeType;
	private List<AbstractMethod> methods;
	private List<AbstractField> fields;
	private Map<MetricID, Object> metrics = new HashMap<MetricID, Object>();
	private Set<CodeSmellID> codeSmells = new HashSet<CodeSmellID>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public List<AbstractMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<AbstractMethod> methods) {
		this.methods = methods;
	}

	public List<AbstractField> getFields() {
		return fields;
	}

	public void setFields(List<AbstractField> fields) {
		this.fields = fields;
	}

	public Map<MetricID, Object> getMetrics() {
		return metrics;
	}

	public Map<String, Object> convertMetrics() {
		Map<String, Object> result = new HashMap<String, Object>();
		for (Entry<MetricID, Object> entry : metrics.entrySet()) {
			result.put(entry.getKey().toString(), entry.getValue());
		}
		return result;
	}
	
	public List<String> convertCodeSmells() {
		List<String> result = new ArrayList<String>();
		for (CodeSmellID codeSmell : codeSmells) {
			result.add(codeSmell.toString());
		}
		return result;
	}
	
	public void setMetrics(Map<MetricID, Object> metrics) {
		this.metrics = metrics;
	}

	public Set<CodeSmellID> getCodeSmells() {
		return codeSmells;
	}

	public void setCodeSmells(Set<CodeSmellID> codeSmells) {
		this.codeSmells = codeSmells;
	}

}