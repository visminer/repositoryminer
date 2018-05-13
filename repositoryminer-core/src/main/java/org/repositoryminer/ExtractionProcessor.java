package org.repositoryminer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
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


/**
 * This class is responsible for extract informations from a SCM.
 */
public class ExtractionProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(ExtractionProcessor.class);
	private static final int MAX_COMMITS = 1000; 
	
	private static ISCM scm;

	/**
	 * Starts the mining process
	 * 
	 * @param rm
	 *            instance of {@link org.repositoryminer.RepositoryMiner} .
	 *            It must <b>NEVER<b> be null, since it will provide important
	 *            parameters for the source-code analysis and persistence
	 * @throws IOException
	 */
	public static void extract(RepositoryMiner rm) throws IOException {
		LOG.info("Starting extraction process.");
		
		File repositoryFolder = new File(rm.getPath());
		scm = SCMFactory.getSCM(rm.getSCM());
		scm.open(rm.getPath());

		Repository repository = new Repository(null, rm.getKey(), rm.getName(),
				rm.getPath(), rm.getSCM(), rm.getDescription(),
				new ArrayList<Developer>());

		repository.setPath(repositoryFolder.getAbsolutePath().replace("\\", "/"));

		RepositoryDAO repoHandler = new RepositoryDAO();
		Document repoDoc = repository.toDocument();
		repoHandler.insert(repoDoc);
		repository.setId(repoDoc.getObjectId("_id"));

		extractReferences(repository.getId());
		repoHandler.updateOnlyContributors(repository.getId(),
				Developer.toDocumentList(extractCommits(repository.getId())));

		scm.close();
		LOG.info("Extraction finished.");
	}

	private static void extractReferences(ObjectId repository) {
		LOG.info("Start references extraction process.");
		
		ReferenceDAO refDocumentHandler = new ReferenceDAO();
		List<Reference> references = scm.getReferences();

		for (Reference ref : references) {
			List<String> commits = scm.getCommitsNames(ref);
			ref.setRepository(repository);
			ref.setCommits(commits);
			Document refDoc = ref.toDocument();
			refDocumentHandler.insert(refDoc);
		}
		
		LOG.info("References extraction process Finished.");
	}

	private static Set<Developer> extractCommits(ObjectId repository) {
		LOG.info("Start commits extraction process.");
		
		CommitDAO documentHandler = new CommitDAO();
		Set<Developer> contributors = new HashSet<Developer>();

		int skip = 0;
		List<Commit> commits = scm.getCommits(skip, MAX_COMMITS);
		while (commits.size() > 0) {
			for (Commit commit : commits) {
				commit.setRepository(repository);
				contributors.add(commit.getCommitter());
				documentHandler.insert(commit.toDocument());
			}
			
			skip += MAX_COMMITS;
			commits = scm.getCommits(skip, MAX_COMMITS);
		}
		
		LOG.info("Commits extraction process Finished.");
		return contributors;
	}

}
