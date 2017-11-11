package org.repositoryminer.metrics.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;

public class MethodReport {

	private String name;
	private MetricsReport metricsReport = new MetricsReport();
	
	public MethodReport(String signature) {
		this.name = signature;
	}

	public Document toDocument() {
		Document doc = new Document("name", name).
				append("metrics", metricsReport.toMetricsDocument()).
				append("codesmells", metricsReport.getCodeSmellsAsString());
		return doc;
	}
	
	public static Object toDocumentList(Collection<MethodReport> values) {
		List<Document> docs = new ArrayList<>();
		for (MethodReport mr : values) {
			docs.add(mr.toDocument());
		}
		return docs;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MetricsReport getMetricsReport() {
		return metricsReport;
	}

	public void setMetricsReport(MetricsReport metricsReport) {
		this.metricsReport = metricsReport;
	}
	
}