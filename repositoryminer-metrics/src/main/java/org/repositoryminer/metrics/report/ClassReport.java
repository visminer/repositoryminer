package org.repositoryminer.metrics.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

public class ClassReport {

	private String name;
	private String type;
	private Map<String, MethodReport> methodsReports = new HashMap<>();
	private MetricsReport metricsReport = new MetricsReport();

	public ClassReport(String name) {
		this.name = name;
	}

	public ClassReport(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public Collection<MethodReport> getMethods() {
		return methodsReports.values();
	}
	
	public MethodReport getMethodBySignature(String signature) {
		MethodReport mr = methodsReports.get(signature);
		if (mr == null) {
			mr = new MethodReport(signature);
			methodsReports.put(signature, mr);
		}
		return mr;
	}

	public Document toDocument() {
		Document doc = new Document("name", name).
				append("type", type).
				append("metrics", metricsReport.toMetricsDocument()).
				append("codesmells", metricsReport.getCodeSmellsAsString()).
				append("methods", MethodReport.toDocumentList(methodsReports.values()));
		return doc;
	}
	
	public static Object toDocumentList(Collection<ClassReport> values) {
		List<Document> docs = new ArrayList<>();
		for (ClassReport cr : values) {
			docs.add(cr.toDocument());
		}
		return docs;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, MethodReport> getMethodsReports() {
		return methodsReports;
	}

	public void setMethodsReports(Map<String, MethodReport> methodsReports) {
		this.methodsReports = methodsReports;
	}

	public MetricsReport getMetricsReport() {
		return metricsReport;
	}

	public void setMetricsReport(MetricsReport metricsReport) {
		this.metricsReport = metricsReport;
	}

}