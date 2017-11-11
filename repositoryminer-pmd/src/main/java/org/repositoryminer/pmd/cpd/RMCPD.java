package org.repositoryminer.pmd.cpd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.plugin.SnapshotAnalysisPlugin;
import org.repositoryminer.pmd.cpd.model.Occurrence;
import org.repositoryminer.pmd.cpd.persistence.CPDDAO;

public class RMCPD extends SnapshotAnalysisPlugin<CPDConfig> {

	@Override
	public boolean run(String snapshot, CPDConfig config) {
		scm.checkout(snapshot);

		CPDExecutor cpdExecutor = new CPDExecutor(tmpRepository);
		cpdExecutor.setCharset("UTF-8");
		
		if (config == null) {
			config = new CPDConfig();
		}
		
		cpdExecutor.setLanguages(config.getLanguages());
		cpdExecutor.setMinTokens(config.getTokensThreshold());

		List<Occurrence> occurrences;
		try {
			occurrences = cpdExecutor.execute();
		} catch (IOException e) {
			throw new RepositoryMinerException("Can not execute PMD/CPD.", e); 
		}

		Commit commit = scm.getHEAD();

		List<Document> documents = new ArrayList<Document>(occurrences.size());
		for (Occurrence occurence : occurrences) {
			Document doc = new Document("commit", commit.getId()).
					append("commit_date", commit.getCommitterDate()).
					append("repository", repositoryId).
					append("tokens_threshold", config.getTokensThreshold());

			doc.putAll(occurence.toDocument());
			documents.add(doc);
		}

		new CPDDAO().insertMany(documents);

		return true;
	}

}