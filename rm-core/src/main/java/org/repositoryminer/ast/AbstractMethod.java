package org.repositoryminer.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.repositoryminer.codesmell.CodeSmellID;
import org.repositoryminer.metric.MetricID;

/**
 * This class represents a method declaration.
 */
public class AbstractMethod {

	private String name;
	private int startPosition;
	private int endPosition;
	private String returnType;
	private boolean isConstructor;
	private boolean isVarargs;
	private List<String> modifiers;
	private List<AbstractStatement> statements;
	private List<AbstractParameter> parameters;
	private List<String> thrownsExceptions;
	private int maxDepth;
	private boolean accessor;
	private String accessoredField;
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

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public boolean isConstructor() {
		return isConstructor;
	}

	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}

	public boolean isVarargs() {
		return isVarargs;
	}

	public void setVarargs(boolean isVarargs) {
		this.isVarargs = isVarargs;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public List<AbstractStatement> getStatements() {
		return statements;
	}

	public void setStatements(List<AbstractStatement> statements) {
		this.statements = statements;
	}

	public List<AbstractParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<AbstractParameter> parameters) {
		this.parameters = parameters;
	}

	public List<String> getThrownsExceptions() {
		return thrownsExceptions;
	}

	public void setThrownsExceptions(List<String> thrownsExceptions) {
		this.thrownsExceptions = thrownsExceptions;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public boolean isAccessor() {
		return accessor;
	}

	public void setAccessor(boolean accessor) {
		this.accessor = accessor;
	}

	public String getAccessoredField() {
		return accessoredField;
	}

	public void setAccessoredField(String accessoredField) {
		this.accessoredField = accessoredField;
	}

	public Map<MetricID, Object> getMetrics() {
		return metrics;
	}

	public void setMetrics(Map<MetricID, Object> metrics) {
		this.metrics = metrics;
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
	
	public Set<CodeSmellID> getCodeSmells() {
		return codeSmells;
	}

	public void setCodeSmells(Set<CodeSmellID> codeSmells) {
		this.codeSmells = codeSmells;
	}

}