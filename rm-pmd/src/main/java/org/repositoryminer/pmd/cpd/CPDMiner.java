package org.repositoryminer.pmd.cpd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.domain.Reference;
import org.repositoryminer.domain.ReferenceType;
import org.repositoryminer.domain.Repository;
import org.repositoryminer.parser.Language;
import org.repositoryminer.persistence.dao.CommitDAO;
import org.repositoryminer.persistence.dao.ReferenceDAO;
import org.repositoryminer.persistence.dao.RepositoryDAO;
import org.repositoryminer.pmd.cpd.model.Occurrence;
import org.repositoryminer.pmd.cpd.persistence.CPDDAO;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.util.RMFileUtils;

import com.mongodb.client.model.Projections;

public class CPDMiner {

	private ISCM scm;
	private String tempRepository;
	
	private Repository repository;
	private CPDExecutor cpdExecutor;

	private CPDDAO cpdPersist = new CPDDAO();
	private CommitDAO commitPersist = new CommitDAO();
	private ReferenceDAO refPersist = new ReferenceDAO();

	private int minTokens = 100;
	private String charset = "UTF-8";
	
	@SuppressWarnings("serial")
	private Set<Language> languages = new HashSet<Language>() {{
		add(Language.JAVA); 
	}};

	public CPDMiner(Repository repository) {
		this.repository = repository;
	}

	public CPDMiner(String repositoryId) {
		RepositoryDAO repoHandler = new RepositoryDAO();
		this.repository = Repository.parseDocument(repoHandler.findById(repositoryId, Projections.include("path", "name", "scm")));
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

	public void execute(String hash) throws IOException {
		persistAnalysis(hash, null);
	}

	public void prepare() throws IOException {
		File repositoryFolder = new File(repository.getPath());
		tempRepository = RMFileUtils.copyFolderToTmp(repositoryFolder.getAbsolutePath(), repository.getName());
		
		scm = SCMFactory.getSCM(repository.getScm());
		scm.open(tempRepository);
		
		cpdExecutor = new CPDExecutor(tempRepository);
	}
	
	public void dispose() throws IOException {
		scm.close();
		RMFileUtils.deleteFolder(tempRepository);
	}
	
	public void execute(String name, ReferenceType type) throws IOException {
		Document refDoc = refPersist.findByNameAndType(name, type, repository.getId(), Projections.slice("commits", 1));
		Reference reference = Reference.parseDocument(refDoc);

		String commitId = reference.getCommits().get(0);
		persistAnalysis(commitId, reference);
	}
	
	private void persistAnalysis(String commitId, Reference ref) throws IOException {
		Document commitDoc = commitPersist.findById(commitId, Projections.include("commit_date"));
		Commit commit = Commit.parseDocument(commitDoc);
		
		scm.checkout(commitId);
		
		configureCPD();
		List<Occurrence> occurrences = cpdExecutor.execute();

		List<Document> documents = new ArrayList<Document>(occurrences.size());
		for (Occurrence occurence : occurrences) {
			Document doc = new Document();

			if (ref != null) {
				doc.append("reference", ref.getPath());
			}

			doc.append("commit", commit.getId());
			doc.append("commit_date", commit.getCommitDate());
			doc.append("repository", new ObjectId(repository.getId()));
			doc.append("tokens_threshold", minTokens);
			doc.putAll(occurence.toDocument());

			documents.add(doc);
		}

		cpdPersist.insertMany(documents);
	}

	private void configureCPD() {
		cpdExecutor.setCharset(charset);
		cpdExecutor.setLanguages(languages);
		cpdExecutor.setMinTokens(minTokens);
	}

}