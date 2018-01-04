package org.repositoryminer.metrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.metrics.codemetric.CodeMetric;
import org.repositoryminer.metrics.codesmell.CodeSmell;
import org.repositoryminer.metrics.parser.Parser;
import org.repositoryminer.metrics.persistence.CodeAnalysisReportDAO;
import org.repositoryminer.metrics.persistence.CodeAnalysisDAO;
import org.repositoryminer.plugin.SnapshotAnalysisPlugin;

import com.mongodb.client.model.Projections;

public class RepositoryMinerMetrics extends SnapshotAnalysisPlugin<MetricsConfig> {

	@Override
	public void run(String snapshot, MetricsConfig config) {
		if (!config.isValid()) {
			throw new RepositoryMinerException(
					"Invalid configuration, check if has parser and code metrics or codes mells.");
		}

		scm.checkout(snapshot);
		Commit commit = scm.getHEAD();
		
		checkDuplicatedAnalysis(commit.getHash());
		
		AnalysisRunner runner = new AnalysisRunner(tmpRepository);
		runner.setCodeMetrics(config.getCodeMetrics());
		runner.setCodeSmells(config.getCodeSmells());
		runner.setParsers(config.getParsers());

		ObjectId reportId = persistAnalysisReport(snapshot, config.getParsers(), runner.getCalculatedMetrics(),
				runner.getDetectedCodeSmells(), commit);
		try {
			runner.run(reportId);
		} catch (IOException e) {
			throw new RepositoryMinerException(e);
		}
	}

	private void checkDuplicatedAnalysis(String hash) {
		CodeAnalysisReportDAO configDao = new CodeAnalysisReportDAO();
		Document doc = configDao.findByCommitHash(hash, Projections.include("_id"));
		if (doc != null) {
			configDao.deleteById(doc.getObjectId("_id"));
			new CodeAnalysisDAO().deleteByConfig(doc.getObjectId("_id"));
		}
	}

	private ObjectId persistAnalysisReport(String reference, List<Parser> usedParsers,
			Collection<CodeMetric> calculatedMetrics, Collection<CodeSmell> detectedCodeSmells, Commit commit) {
		CodeAnalysisReportDAO configDao = new CodeAnalysisReportDAO();

		List<String> metricsNames = new ArrayList<>();
		for (CodeMetric cm : calculatedMetrics) {
			metricsNames.add(cm.getId().name());
		}

		List<String> parsersNames = new ArrayList<>();
		for (Parser p : usedParsers) {
			parsersNames.add(p.getId().name());
		}

		List<Document> codeSmellsDoc = new ArrayList<>();
		for (CodeSmell codeSmell : detectedCodeSmells) {
			codeSmellsDoc.add(new Document("codesmell", codeSmell.getId().name()).append("thresholds",
					codeSmell.getThresholds()));
		}

		Document doc = new Document();
		doc.append("reference", reference).
			append("commit", commit.getHash()).
			append("commit_date", commit.getCommitterDate()).
			append("analysis_date", new Date(System.currentTimeMillis())).
			append("repository", repositoryId).
			append("parsers", parsersNames).
			append("metrics", metricsNames).
			append("codesmells", codeSmellsDoc);

		configDao.insert(doc);

		return doc.getObjectId("_id");
	}

}