package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.mining.model.Commit;
import org.repositoryminer.mining.model.Reference;
import org.repositoryminer.mining.model.Repository;
import org.repositoryminer.persistence.handler.CommitAnalysisDocumentHandler;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.persistence.handler.WorkingDirectoryDocumentHandler;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.utility.HashHandler;

public class RepositoryExplorer {

	public static void mineRepository(Repository repository) throws IOException {
		File repoFolder = new File(repository.getPath());
		String canonicalPath = repoFolder.getCanonicalPath();
		
		String id = HashHandler.SHA1(canonicalPath.replace("\\", "/"));
		
		RepositoryDocumentHandler repoDocHandler = new RepositoryDocumentHandler();
		Repository.initRepository(repoDocHandler.findById(id, null), repository);
		
		ReferenceDocumentHandler refsDocHandler = new ReferenceDocumentHandler();
		List<Document> refsDocs = refsDocHandler.getByRepository(id);
		repository.setBranches(Reference.parseDocuments(refsDocs, ReferenceType.BRANCH));
		repository.setTags(Reference.parseDocuments(refsDocs, ReferenceType.TAG));
		repository.setCurrentReference(Reference.getMaster(repository.getBranches()));
		
		mineCurrentReference(repository);
	}
	
	public static void mineCurrentCommit(Repository repository) {
		WorkingDirectoryDocumentHandler wdHandler = new WorkingDirectoryDocumentHandler();
		Document wdDoc = wdHandler.findById(repository.getCurrentCommit().getId());
		repository.setWorkingDirectory(Repository.parseWorkingDirectory(wdDoc));
	}
	
	public static void mineCurrentReference(Repository repository) {
		CommitDocumentHandler commitHandler = new CommitDocumentHandler();
		List<Document> commitsDocs = commitHandler.getAllByIds(repository.getCurrentReference()
				.getCommits());
		repository.setCommits(Commit.parseDocuments(commitsDocs));
		repository.lastCommit();
		mineCurrentCommit(repository);
	}
	
	public static Document getAllMeasures(String file, String commit) {
		CommitAnalysisDocumentHandler docHandler = new CommitAnalysisDocumentHandler();
		String fileHash = HashHandler.SHA1(file);
		return docHandler.getAllMeasures(fileHash, commit);
	}
	
	public static Document getMetricMeasures(String file, String commit) {
		CommitAnalysisDocumentHandler docHandler = new CommitAnalysisDocumentHandler();
		String fileHash = HashHandler.SHA1(file);
		return docHandler.getMetricsMeasures(fileHash, commit);
	}
	
	public static Document getCodeSmellsMeasures(String file, String commit) {
		CommitAnalysisDocumentHandler docHandler = new CommitAnalysisDocumentHandler();
		String fileHash = HashHandler.SHA1(file);
		return docHandler.getCodeSmellsMeasures(fileHash, commit);
	}
	
	public static Document getTechnicalDebtsMeasures(String file, String commit) {
		CommitAnalysisDocumentHandler docHandler = new CommitAnalysisDocumentHandler();
		String fileHash = HashHandler.SHA1(file);
		return docHandler.getTechnicalDebtsMeasures(fileHash, commit);
	}
	
}