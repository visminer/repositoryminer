package org.repositoryminer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.domain.Developer;
import org.repositoryminer.domain.Reference;
import org.repositoryminer.domain.Repository;
import org.repositoryminer.persistence.CommitDAO;
import org.repositoryminer.persistence.ReferenceDAO;
import org.repositoryminer.persistence.RepositoryDAO;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.scm.SCMFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.model.Projections;

public class IncrementalRepositoryExtractor {

	private static final Logger LOG = LoggerFactory.getLogger(IncrementalRepositoryExtractor.class);
	
	private static Repository repository;
	private static Set<Developer> contributos;
	private static ISCM scm;

	public static void run(String key) {
		LOG.info("Starting extraction process.");
		
		RepositoryDAO repoDAO = new RepositoryDAO();
		repository = Repository.parseDocument(repoDAO.findByKey(key, null));

		if (repository == null) {
			throw new RepositoryMinerException("Repository with the key " + key + " was not found.");
		}

		scm = SCMFactory.getSCM(repository.getScm());
		scm.open(repository.getPath());
		contributos = new HashSet<Developer>(repository.getContributors());
		
		updateReferences();
		updateCommits();
		
		repoDAO.updateOnlyContributors(repository.getId(), Developer.toDocumentList(contributos));
		
		scm.close();
		LOG.info("Extraction finished.");
	}

	private static void updateReferences() {
		LOG.info("Start references extraction process.");
		List<Reference> references = scm.getReferences();

		ReferenceDAO refDao = new ReferenceDAO();
		List<Reference> dbReferences = Reference
				.parseDocuments(refDao.findByRepository(repository.getId(), Projections.exclude("commits")));

		Map<String, Reference> dbRefsMap = new HashMap<>();
		for (Reference ref : dbReferences) {
			dbRefsMap.put(ref.getPath(), ref);
		}

		for (Reference ref : references) {
			Reference tempRef = dbRefsMap.get(ref.getPath());
			if (tempRef == null) {
				ref.setCommits(scm.getCommitsNames(ref));
				refDao.insert(ref.toDocument());
				ref.setCommits(null);
			} else {
				refDao.updateCommitsAndLastCommitDate(tempRef.getId(), scm.getCommitsNames(ref),
						ref.getLastCommitDate());
				dbRefsMap.remove(ref.getPath());
			}
		}

		for (Reference ref : dbRefsMap.values()) {
			refDao.delete(ref.getId());
		}
		LOG.info("References update process finished.");
	}

	private static void updateCommits() {
		LOG.info("Start commits extraction process.");
		Set<String> commits = new HashSet<String>(scm.getCommitsNames());
		List<String> commitsDb = new ArrayList<>();
		
		CommitDAO commitDao = new CommitDAO();
		for (Document doc : commitDao.findByRepository(repository.getId(), Projections.include("hash"))) {
			commitsDb.add(doc.getString("hash"));
		}

		// removes the commits already saved and delete the commits that were modified.
		for (String commitName : commitsDb) {
			if (!commits.remove(commitName)) {
				commitDao.delete(commitName, repository.getId());
			}
		}
		
		// saves the new/modified commits
		if (commits.size() > 0) {
			List<Document> documents = new ArrayList<>();
			for (Commit commit : scm.getCommits(commits)) {
				documents.add(commit.toDocument());
				contributos.add(commit.getAuthor());
				contributos.add(commit.getCommitter());
			}
			commitDao.insertMany(documents);
		}
		LOG.info("Commits extraction process finished.");
	}

}
