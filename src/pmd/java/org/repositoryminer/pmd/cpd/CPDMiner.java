package org.repositoryminer.pmd.cpd;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.ast.Language;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.Repository;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.pmd.cpd.model.Occurrence;
import org.repositoryminer.pmd.cpd.persistence.CPDDocumentHandler;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.utility.FileUtils;

import com.mongodb.client.model.Projections;

public class CPDMiner {

	private Repository repository;
	private ISCM scm;
	private String tmpRepository;
	private CPDExecutor cpdExecutor;
	
	private CPDDocumentHandler cpdPersist;
	private CommitDocumentHandler commitPersist;
	private ReferenceDocumentHandler refPersist;
	
	private int minTokens = 100;
	private String charset = "UTF-8";
	private List<Language> languages = Arrays.asList(Language.JAVA);

	public void setMinTokens(int minTokens) {
		this.minTokens = minTokens;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	
	public void setRepository(String repositoryId) {
		RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
		this.repository = Repository.parseDocument(repoHandler.findById(repositoryId, Projections.include("scm")));
	}
	
	public void detect(String hash) throws IOException {
		Document commitDoc = commitPersist.findById(hash, Projections.include("commit_date"));
		Commit commit = Commit.parseDocument(commitDoc);

		configureCPD();
		List<Occurrence> occurrences = cpdExecutor.execute();

		Document doc = new Document();
		doc.append("commit", commit.getId());
		doc.append("commit_date", commit.getCommitDate());
		doc.append("repository", new ObjectId(repository.getId()));
		doc.append("occurrences", Occurrence.toDocumentList(occurrences));

		cpdPersist.insert(doc);
	}

	public void detect(String name, ReferenceType type) throws IOException {
		Document refDoc = refPersist.findByNameAndType(name, type, repository.getId(), Projections.slice("commits", 1));
		Reference reference = Reference.parseDocument(refDoc);

		configureCPD();
		List<Occurrence> occurrences = cpdExecutor.execute();
		
		String commitId = reference.getCommits().get(0);
		Commit commit = Commit.parseDocument(commitPersist.findById(commitId, Projections.include("commit_date")));

		Document doc = new Document();
		doc.append("reference_name", reference.getName());
		doc.append("reference_type", reference.getType().toString());
		doc.append("commit", commit.getId());
		doc.append("commit_date", commit.getCommitDate());
		doc.append("repository", new ObjectId(repository.getId()));
		doc.append("occurrences", Occurrence.toDocumentList(occurrences));

		cpdPersist.insert(doc);
	}
	
	public void configure() throws IOException {
		tmpRepository = FileUtils.copyFolderToTmp(repository.getPath(), repository.getId());
		cpdExecutor = new CPDExecutor(tmpRepository);

		cpdPersist = new CPDDocumentHandler();
		commitPersist = new CommitDocumentHandler();
		refPersist = new ReferenceDocumentHandler();

		scm = SCMFactory.getSCM(repository.getScm());
		scm.open(tmpRepository);
	}
	
	public void dispose() throws IOException {
		scm.close();
		FileUtils.deleteFolder(tmpRepository);
	}
	
	public void checkout(String ref) {
		scm.checkout(ref);
	}
	
	public void checkout(String reference, ReferenceType type) {
		List<Reference> references = scm.getReferences();
		for (Reference r : references) {
			if (r.getName().equals(reference) && r.getType().equals(type)) {
				scm.checkout(r.getPath());
				break;
			}
		}
	}
	
	public void configureCPD() {
		cpdExecutor.setCharset(charset);
		cpdExecutor.setLanguages(languages);
		cpdExecutor.setMinTokens(minTokens);
	}
	
}