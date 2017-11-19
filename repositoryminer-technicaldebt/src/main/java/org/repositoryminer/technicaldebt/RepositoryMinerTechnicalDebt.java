package org.repositoryminer.technicaldebt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.plugin.SnapshotAnalysisPlugin;
import org.repositoryminer.technicaldebt.model.TDIndicator;
import org.repositoryminer.technicaldebt.model.TDItem;
import org.repositoryminer.technicaldebt.persistence.TechnicalDebtDAO;

public class RepositoryMinerTechnicalDebt extends SnapshotAnalysisPlugin<List<TDIndicator>>{

	// TODO: indicators filter is not implemented yet.
	@Override
	public void run(String snapshot, List<TDIndicator> config) {
		Commit commit = scm.resolve(snapshot);

		TechnicalDebtDAO dao = new TechnicalDebtDAO();
		dao.deleteByCommit(commit.getHash());

		Collection<TDItem> items = new TDFinder().find(commit.getHash());
		List<Document> documents = new ArrayList<>(items.size());

		for (TDItem item : items) {
			if (item.isDebt()) {
				Document doc = new Document("commit", commit.getHash()).
						append("commit_date", commit.getCommitterDate()).
						append("repository", repositoryId);

				doc.putAll(item.toDocument());
				documents.add(doc);
			}
		}

		dao.insertMany(documents);
	}

}