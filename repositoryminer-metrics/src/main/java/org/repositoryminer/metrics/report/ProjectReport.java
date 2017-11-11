package org.repositoryminer.metrics.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProjectReport {

	private Map<String, FileReport> files = new HashMap<>();
	private Map<String, ClassReport> orphanClasses = new HashMap<>();
	
	public Collection<FileReport> getAllFiles() {
		return files.values();
	}
	
	public ClassReport getClassByName(String name) {
		for (FileReport fr : files.values()) {
			ClassReport cr = fr.getClassesReports().get(name);
			if (cr != null) {
				return cr;
			}
		}
		
		ClassReport cr = orphanClasses.get(name);
		if (cr == null) {
			cr = new ClassReport(name);
			orphanClasses.put(name, cr);
		}
		
		return cr;
	}
	
	public FileReport getFileByName(String name) {
		return files.get(name);
	}
	
	public void addFileReport(FileReport fileReport) {
		files.put(fileReport.getName(), fileReport);
		
		if (orphanClasses.isEmpty()) {
			return;
		}
		
		for (ClassReport cr : fileReport.getClassesReports().values()) {
			ClassReport orphanClass = orphanClasses.get(cr.getName());
			if (orphanClass != null) {
				orphanClasses.remove(cr.getName());
				cr.setMetricsReport(orphanClass.getMetricsReport());
				for (MethodReport mr : cr.getMethodsReports().values()) {
					MethodReport orphanMethod = orphanClass.getMethodsReports().get(mr.getName());
					if (orphanMethod != null) {
						orphanClass.getMethodsReports().remove(mr.getName());
						mr.setMetricsReport(orphanMethod.getMetricsReport());
					}
				}
			}
		}
	}
	
}