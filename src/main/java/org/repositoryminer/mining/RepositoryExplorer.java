package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.mining.model.Commit;
import org.repositoryminer.mining.model.Reference;
import org.repositoryminer.mining.model.Repository;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.persistence.handler.WorkingDirectoryDocumentHandler;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.utility.HashHandler;

public class RepositoryExplorer {

	public static Repository mineRepository(String path) throws IOException {
		File repoFolder = new File(path);
		String canonicalPath = repoFolder.getCanonicalPath();
		
		String id = HashHandler.SHA1(canonicalPath.replace("\\", "/"));
		
		RepositoryDocumentHandler repoDocHandler = new RepositoryDocumentHandler();
		Repository repository = new Repository(repoDocHandler.findById(id, null));
		
		ReferenceDocumentHandler refsDocHandler = new ReferenceDocumentHandler();
		List<Document> refsDocs = refsDocHandler.getAllByRepository(id);
		repository.setBranches(Reference.parseDocuments(refsDocs, ReferenceType.BRANCH));
		repository.setTags(Reference.parseDocuments(refsDocs, ReferenceType.TAG));
		repository.setCurrentReference(Reference.getMaster(repository.getBranches()));
		
		CommitDocumentHandler commitHandler = new CommitDocumentHandler();
		List<Document> commitsDocs = commitHandler.getAllByIds(repository.getCurrentReference()
				.getCommits());
		repository.setCommits(Commit.parseDocuments(commitsDocs));
		
		WorkingDirectoryDocumentHandler wdHandler = new WorkingDirectoryDocumentHandler();
		Document wdDoc = wdHandler.findById(repository.getCurrentCommit().getId());
		repository.setWorkingDirectory(Repository.parseWorkingDirectory(wdDoc));
		
		return repository;
	}
	
	// TODO: implement these methods
	public static void mineNextCommit(Repository repository) {}
	public static void minePrevCommit(Repository repository) {}
	public static void mineCurrentCommit(Repository repository) {}
	public static void mineCurrentReference(Repository repository) {}
	
}