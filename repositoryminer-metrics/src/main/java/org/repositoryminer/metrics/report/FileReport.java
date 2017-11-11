package org.repositoryminer.metrics.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

public class FileReport {

	private String name;
	private Map<String, ClassReport> classesReports = new HashMap<>();
	private MetricsReport metricsReport = new MetricsReport();

	public ClassReport getClass(String name) {
		ClassReport cr = classesReports.get(name);
		if (cr == null) {
			cr = new ClassReport(name);
			classesReports.put(name, cr);
		}
		return cr;
	}

	public Collection<ClassReport> getClasses() {
		return classesReports.values();
	}
	
	public Document toDocument() {
		Document doc = new Document("filename", name).
				append("metrics", metricsReport.toMetricsDocument()).
				append("classes", ClassReport.toDocumentList(classesReports.values()));
		return doc;
	}

	public FileReport(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, ClassReport> getClassesReports() {
		return classesReports;
	}

	public void setClassesReports(Map<String, ClassReport> classesReports) {
		this.classesReports = classesReports;
	}

	public MetricsReport getMetricsReport() {
		return metricsReport;
	}

	public void setMetricsReport(MetricsReport metricsReport) {
		this.metricsReport = metricsReport;
	}

}