package org.repositoryminer.ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.repositoryminer.codemetric.direct.MetricId;

/**
 * This class represents the file and is also the Abstract Syntax Tree root
 * node.
 */
public class AST {

	private String name;
	private String source;
	private String language;
	private List<AbstractType> types;
	private List<AbstractMethod> methods;
	private List<AbstractImport> imports;
	private String packageDeclaration;
	private Map<MetricId, Object> metrics = new HashMap<MetricId, Object>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<AbstractType> getTypes() {
		return types;
	}

	public void setTypes(List<AbstractType> types) {
		this.types = types;
	}

	public List<AbstractMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<AbstractMethod> methods) {
		this.methods = methods;
	}

	public List<AbstractImport> getImports() {
		return imports;
	}

	public void setImports(List<AbstractImport> imports) {
		this.imports = imports;
	}

	public String getPackageDeclaration() {
		return packageDeclaration;
	}

	public void setPackageDeclaration(String packageDeclaration) {
		this.packageDeclaration = packageDeclaration;
	}

	public Map<MetricId, Object> getMetrics() {
		return metrics;
	}

	public void setMetrics(Map<MetricId, Object> metrics) {
		this.metrics = metrics;
	}

	public Map<String, Object> convertMetrics() {
		Map<String, Object> result = new HashMap<String, Object>();
		for (Entry<MetricId, Object> entry : metrics.entrySet()) {
			result.put(entry.getKey().toString(), entry.getValue());
		}
		return result;
	}
	
}