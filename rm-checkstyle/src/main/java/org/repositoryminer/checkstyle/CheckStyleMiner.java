package org.repositoryminer.checkstyle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.checkstyle.model.StyleProblem;
import org.repositoryminer.checkstyle.persistence.CheckstyleAuditDAO;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.domain.Reference;
import org.repositoryminer.domain.ReferenceType;
import org.repositoryminer.domain.Repository;
import org.repositoryminer.exception.RepositoryMinerException;
import org.repositoryminer.persistence.dao.CommitDAO;
import org.repositoryminer.persistence.dao.ReferenceDAO;
import org.repositoryminer.persistence.dao.RepositoryDAO;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.util.HashingUtils;
import org.repositoryminer.util.RMFileUtils;

import com.mongodb.client.model.Projections;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public class CheckStyleMiner {

	private ISCM scm;
	private String tempRepository;
	
	private Repository repository;

	private CommitDAO commitPersist = new CommitDAO();
	private ReferenceDAO refPersist = new ReferenceDAO();
	private CheckstyleAuditDAO checkstylePersist = new CheckstyleAuditDAO();

	private CheckStyleExecutor checkstyleExecutor;

	private String propertiesFile;
	private String configFile;

	public CheckStyleMiner(Repository repository) {
		this.repository = repository;
	}
	
	public CheckStyleMiner(String repositoryId) {
		final RepositoryDAO repoHandler = new RepositoryDAO();
		this.repository = Repository
				.parseDocument(repoHandler.findById(repositoryId, Projections.include("path", "name", "scm")));
	}
	
	public void execute(String hash) {
		persistAnalysis(hash, null);
	}
	
	public void execute(String name, ReferenceType type) {
		final Document refDoc = refPersist.findByNameAndType(name, type, repository.getId(), Projections.slice("commits", 1));
		final Reference reference = Reference.parseDocument(refDoc);
		final String commitId = reference.getCommits().get(0);
		persistAnalysis(commitId, reference);
	}
	
	public void prepare() throws IOException {
		File repositoryFolder = new File(repository.getPath());
		tempRepository = RMFileUtils.copyFolderToTmp(repositoryFolder.getAbsolutePath(), repository.getName());
		
		scm = SCMFactory.getSCM(repository.getScm());
		scm.open(tempRepository);
		
		checkstyleExecutor = new CheckStyleExecutor(tempRepository);
	}
	
	public void dispose() throws IOException {
		scm.close();
		RMFileUtils.deleteFolder(tempRepository);
	}
	
	private void persistAnalysis(String commitId, Reference ref) {
		final Commit commit = Commit.parseDocument(commitPersist.findById(commitId, Projections.include("commit_date")));
		
		scm.checkout(commitId);
		
		configureCheckstyle();
		Map<String, List<StyleProblem>> result = null;
		
		try {
			result = checkstyleExecutor.execute();
		} catch (CheckstyleException e) {
			throw new RepositoryMinerException("Can not execute checkstyle", e);
		}
		
		List<Document> documents = new ArrayList<Document>(result.size());
		for (Entry<String, List<StyleProblem>> file : result.entrySet()) {
			Document doc = new Document();
			
			if (ref != null) {
				doc.append("reference", ref.getPath());
			}
			
			doc.append("commit", commit.getId());
			doc.append("commit_date", commit.getCommitDate());
			doc.append("repository", new ObjectId(repository.getId()));
			doc.append("filename", file.getKey());
			doc.append("filehash", HashingUtils.encodeToCRC32(file.getKey()));
			doc.append("style_problems", StyleProblem.toDocumentList(file.getValue()));
			
			documents.add(doc);
		}
		
		checkstylePersist.insertMany(documents);
	}
	
	private void configureCheckstyle() {
		checkstyleExecutor.setConfigFile(configFile);
		checkstyleExecutor.setPropertiesFile(propertiesFile);
	}
	
	public String getPropertiesFile() {
		return propertiesFile;
	}

	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

}