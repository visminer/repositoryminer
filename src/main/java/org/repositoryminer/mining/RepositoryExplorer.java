package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.mining.model.Repository;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.utility.HashHandler;

public class RepositoryExplorer {

	public static Repository getRepository(String path) throws IOException {
		File repoFolder = new File(path);
		String canonicalPath = repoFolder.getCanonicalPath();
		
		String id = HashHandler.SHA1(canonicalPath);
		RepositoryDocumentHandler repoDocHandler = new RepositoryDocumentHandler();
		Repository repository = new Repository(repoDocHandler.findById(id, null));
		
		ReferenceDocumentHandler refsDocHandler = new ReferenceDocumentHandler();
		List<Document> refsDocs = refsDocHandler.getAllByRepository(id);
		repository.parserReferenceDocs(refsDocs);
		
		return repository;
	}
	
}
