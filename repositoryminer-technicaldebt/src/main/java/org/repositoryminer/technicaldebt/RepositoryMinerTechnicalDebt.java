package org.repositoryminer.technicaldebt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.plugin.SnapshotAnalysisPlugin;
import org.repositoryminer.technicaldebt.model.TDIndicator;
import org.repositoryminer.technicaldebt.model.TDItem;
import org.repositoryminer.technicaldebt.model.TDType;
import org.repositoryminer.technicaldebt.persistence.TechnicalDebtReportDAO;
import org.repositoryminer.technicaldebt.persistence.TechnicalDebtDAO;

import com.mongodb.client.model.Projections;

public class RepositoryMinerTechnicalDebt extends SnapshotAnalysisPlugin<Set<TDIndicator>>{

	@Override
	public void run(String snapshot, Set<TDIndicator> indicators) {
		if (indicators == null || indicators.isEmpty()) {
			throw new RepositoryMinerException("Invalid configuration, select at least one indicator.");
		}
		
		Commit commit = scm.resolve(snapshot);
		checkDuplicatedAnalysis(commit.getHash());
		
		ObjectId reportId = persistAnalysisReport(snapshot, commit, indicators);
		
		Collection<TDItem> items = new TDFinder().find(commit.getHash(), indicators);
		List<Document> documents = new ArrayList<>(items.size());

		for (TDItem item : items) {
			if (item.isDebt()) {
				Document doc = new Document();
				doc.append("reference", snapshot).
					append("commit", commit.getHash()).
					append("commit_date", commit.getCommitterDate()).
					append("repository", repositoryId).
					append("analysis_report", reportId);

				doc.putAll(item.toDocument());
				documents.add(doc);
			}
		}

		new TechnicalDebtDAO().insertMany(documents);
	}
	
	private ObjectId persistAnalysisReport(String reference, Commit commit, Set<TDIndicator> indicators) {
		TechnicalDebtReportDAO configDao = new TechnicalDebtReportDAO();
		
		List<String> indicatorsList = new ArrayList<String>();
		for (TDIndicator indicator : indicators) {
			indicatorsList.add(indicator.name());
		}
		
		Set<String> typesList = new HashSet<String>();
		for (TDIndicator indicator : indicators) {
			for (TDType type : indicator.getTypes()) {
				typesList.add(type.name());
			}
		}
		
		Document doc = new Document();
		doc.append("reference", reference)
			.append("commit", commit.getHash())
			.append("commit_date", commit.getCommitterDate())
			.append("analysis_date", new Date(System.currentTimeMillis()))
			.append("repository", repositoryId)
			.append("indicators", indicatorsList)
			.append("types", typesList);
		
		configDao.insert(doc);
		return doc.getObjectId("_id");
	}

	private void checkDuplicatedAnalysis(String hash) {
		TechnicalDebtReportDAO configDao = new TechnicalDebtReportDAO();
		Document doc = configDao.findByCommitHash(hash, Projections.include("_id"));
		if (doc != null) {
			configDao.deleteById(doc.getObjectId("_id"));
			new TechnicalDebtDAO().deleteByConfig(doc.getObjectId("_id"));
		}
	}

}
