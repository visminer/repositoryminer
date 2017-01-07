package org.repositoryminer.pmd.cpd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private Set<Language> languages;

	private CPDMiner() {
		languages = new HashSet<Language>(1);
		languages.add(Language.JAVA);
	}
	
	public CPDMiner(Repository repository) {
		this();
		this.repository = repository;
	}

	public CPDMiner(String repositoryId) {
		this();
		RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
		this.repository = Repository.parseDocument(repoHandler.findById(repositoryId, Projections.include("scm", "path")));
	}

	public int getMinTokens() {
		return minTokens;
	}

	public void setMinTokens(int minTokens) {
		this.minTokens = minTokens;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Set<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Set<Language> languages) {
		this.languages = languages;
	}

	public void detectCopyPaste(String hash) throws IOException {
		persistAnalysis(hash);
	}

	public void detectCopyPaste(String name, ReferenceType type) throws IOException {
		Document refDoc = refPersist.findByNameAndType(name, type, repository.getId(), Projections.slice("commits", 1));
		Reference reference = Reference.parseDocument(refDoc);

		String commitId = reference.getCommits().get(0);
		persistAnalysis(commitId);
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

	private void persistAnalysis(String commitId) throws IOException {
		Document commitDoc = commitPersist.findById(commitId, Projections.include("commit_date"));
		Commit commit = Commit.parseDocument(commitDoc);

		checkout(commitId);

		configureCPD();
		List<Occurrence> occurrences = cpdExecutor.execute();

		List<Document> documents = new ArrayList<Document>(occurrences.size());
		for (Occurrence occurence : occurrences) {
			Document doc = new Document();
			doc.append("commit", commit.getId());
			doc.append("commit_date", commit.getCommitDate());
			doc.append("repository", new ObjectId(repository.getId()));
			doc.append("tokens_threshold", minTokens);
			doc.putAll(occurence.toDocument());
			
			documents.add(doc);
		}

		cpdPersist.insertMany(documents);
	}

	private void checkout(String ref) {
		scm.checkout(ref);
	}

	private void configureCPD() {
		cpdExecutor.setCharset(charset);
		cpdExecutor.setLanguages(languages);
		cpdExecutor.setMinTokens(minTokens);
	}

}