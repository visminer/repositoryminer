package org.repositoryminer.pmd.cpd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.plugin.SnapshotAnalysisPlugin;
import org.repositoryminer.pmd.cpd.model.Match;
import org.repositoryminer.pmd.cpd.persistence.CPDDAO;

public class RepositoryMinerCPD extends SnapshotAnalysisPlugin<CPDConfig> {

	@Override
	public void run(String snapshot, CPDConfig config) {
		scm.checkout(snapshot);
		Commit commit = scm.resolve(snapshot);

		CPDDAO dao = new CPDDAO();
		dao.deleteByCommit(commit.getHash());

		CPDExecutor cpdExecutor = new CPDExecutor(tmpRepository);
		cpdExecutor.setCharset("UTF-8");

		if (config == null) {
			config = new CPDConfig();
		}

		cpdExecutor.setLanguages(config.getLanguages());
		cpdExecutor.setMinTokens(config.getTokensThreshold());

		List<Match> matches;
		try {
			matches = cpdExecutor.execute();
		} catch (IOException e) {
			throw new RepositoryMinerException("Can not execute PMD/CPD.", e); 
		}

		List<Document> documents = new ArrayList<Document>(matches.size());
		for (Match match : matches) {
			Document doc = new Document();
			doc.append("reference", snapshot).
				append("commit", commit.getHash()).
				append("commit_date", commit.getCommitterDate()).
				append("repository", repositoryId).
				append("tokens_threshold", config.getTokensThreshold());

			doc.putAll(match.toDocument());
			documents.add(doc);
		}

		dao.insertMany(documents);
	}

}