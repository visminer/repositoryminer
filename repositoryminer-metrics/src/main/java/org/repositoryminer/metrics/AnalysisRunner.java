package org.repositoryminer.metrics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.codemetric.CodeMetric;
import org.repositoryminer.metrics.codemetric.CodeMetricId;
import org.repositoryminer.metrics.codemetric.MetricFactory;
import org.repositoryminer.metrics.codesmell.CodeSmell;
import org.repositoryminer.metrics.codesmell.CodeSmellFactory;
import org.repositoryminer.metrics.codesmell.CodeSmellId;
import org.repositoryminer.metrics.parser.Language;
import org.repositoryminer.metrics.parser.Parser;
import org.repositoryminer.metrics.persistence.CodeAnalysisDAO;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;
import org.repositoryminer.util.RMFileUtils;

public class AnalysisRunner {

	private ProjectReport projectReport = new ProjectReport();

	private Map<CodeMetricId, CodeMetric> metricsToCalculate = new LinkedHashMap<>();
	private Map<CodeSmellId, CodeSmell> codeSmellsToDetect = new LinkedHashMap<>();
	private Map<String, Parser> parsersToUse = new LinkedHashMap<>();
	private Map<Language, String[]> sourceFolders = new LinkedHashMap<>();

	private String repository;

	public AnalysisRunner(String repository) {
		this.repository = repository;
	}

	public Collection<CodeMetric> getCalculatedMetrics() {
		return metricsToCalculate.values();
	}

	public Collection<CodeSmell> getDetectedCodeSmells() {
		return codeSmellsToDetect.values();
	}

	public void setCodeMetrics(List<CodeMetric> codeMetrics) {
		if (codeMetrics == null) {
			return;
		}

		for (CodeMetric metric : codeMetrics) {
			visitMetric(metric);
		}
	}

	public void setCodeSmells(List<CodeSmell> codeSmells) {
		if (codeSmells == null) {
			return;
		}

		for (CodeSmell codeSmell : codeSmells) {
			visitCodeSmell(codeSmell);
		}
	}

	public void setParsers(List<Parser> parsers) {
		for (Parser p : parsers) {
			if (p.getSourceFolders() == null || p.getSourceFolders().length == 0) {
				p.setSourceFolders(RMFileUtils.getAllDirsAsString(repository).toArray(new String[0]));
			} else {
				p.setSourceFolders(RMFileUtils.concatFilePath(repository, p.getSourceFolders()));
			}

			for (String ext : p.getExtensions()) {
				parsersToUse.put(ext, p);
			}
		}
	}

	public void run(ObjectId analysisReportId, Commit commit, ObjectId repoId, String reference) throws IOException {
		for (File file : FileUtils.listFiles(new File(repository), parsersToUse.keySet().toArray(new String[0]),
				true)) {
			analyzeFile(file, repository);
		}

		for (CodeMetric metric : metricsToCalculate.values()) {
			metric.clean(projectReport);
		}

		for (CodeSmell codeSmell : codeSmellsToDetect.values()) {
			for (FileReport fr : projectReport.getAllFiles()) {
				codeSmell.detect(fr, projectReport);
			}
		}

		persistData(analysisReportId, commit, repoId, reference);
	}

	private void analyzeFile(File file, String repository) throws IOException {
		Parser parser = parsersToUse.get(FilenameUtils.getExtension(file.getAbsolutePath()));
		if (parser == null) {
			return;
		}

		String filename = FilenameUtils.normalize(file.getAbsolutePath(), true).substring(repository.length() + 1);

		AST ast = parser.generate(filename, FileUtils.readFileToString(file, "UTF-8"),
				sourceFolders.get(parser.getId()));

		FileReport fr = new FileReport(ast.getName());
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = new ClassReport(type.getName(), type.getNodeType().toString());
			fr.getClassesReports().put(type.getName(), cr);
			for (AbstractMethod method : type.getMethods()) {
				MethodReport mr = new MethodReport(method.getName());
				cr.getMethodsReports().put(method.getName(), mr);
			}
		}
		projectReport.addFileReport(fr);

		for (CodeMetric metric : metricsToCalculate.values()) {
			metric.calculate(ast, fr, projectReport);
		}
	}

	private void persistData(ObjectId analysisReportId, Commit commit, ObjectId repoId, String reference) {
		CodeAnalysisDAO dao = new CodeAnalysisDAO();
		List<Document> documents = new ArrayList<>();

		int i = 0;
		for (FileReport fr : projectReport.getAllFiles()) {
			Document doc = fr.toDocument();
			doc.append("analysis_report", analysisReportId).
				append("reference", reference).
				append("commit", commit.getHash()).
				append("commit_date", commit.getCommitterDate()).
				append("repository", repoId);
			
			documents.add(doc);

			if (i == 1000) {
				i = 0;
				dao.insertMany(documents);
				documents.clear();
			}
			i++;
		}

		if (documents.size() > 0) {
			dao.insertMany(documents);
		}
	}

	// Check if the metric requisites are being calculated in the correct order.
	private void visitMetric(CodeMetric codeMetric) {
		if (codeMetric.getRequiredMetrics() != null) {
			for (CodeMetricId id : codeMetric.getRequiredMetrics()) {
				if (!metricsToCalculate.containsKey(id)) {
					visitMetric(MetricFactory.getMetric(id));
				}
			}
		}

		if (!metricsToCalculate.containsKey(codeMetric.getId())) {
			metricsToCalculate.put(codeMetric.getId(), codeMetric);
		}
	}

	// checks if the code smells are being detected in the correct order based on
	// theirs requisites, and also make sure that the needed metrics were calculated too.
	private void visitCodeSmell(CodeSmell codeSmellParam) {
		if (codeSmellParam.getRequiredMetrics() != null) {
			for (CodeMetricId id : codeSmellParam.getRequiredMetrics()) {
				visitMetric(MetricFactory.getMetric(id));
			}
		}

		if (codeSmellParam.getRequiredCodeSmells() != null) {
			for (CodeSmellId id : codeSmellParam.getRequiredCodeSmells()) {
				if (!codeSmellsToDetect.containsKey(id)) {
					visitCodeSmell(CodeSmellFactory.getCodeSmell(id));
				}
			}
		}

		if (!codeSmellsToDetect.containsKey(codeSmellParam.getId())) {
			codeSmellsToDetect.put(codeSmellParam.getId(), codeSmellParam);
		}
	}

}