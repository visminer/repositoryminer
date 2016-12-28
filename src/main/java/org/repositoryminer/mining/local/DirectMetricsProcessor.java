package org.repositoryminer.mining.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.codesmell.clazz.IClassCodeSmell;
import org.repositoryminer.metric.clazz.IClassMetric;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Diff;
import org.repositoryminer.model.Reference;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.persistence.handler.DirectCodeMetricsDocumentHandler;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.scm.DiffType;
import org.repositoryminer.scm.ISCM;

import com.mongodb.client.model.Projections;

public class DirectMetricsProcessor {

	private static final int COMMIT_RANGE = 3000;

	private ISCM scm;
	private RepositoryMiner repositoryMiner;
	private String repositoryId;
	private String repositoryPath;
	
	private DirectCodeMetricsDocumentHandler directMetricHandler;
	private CommitDocumentHandler commitPersistence;
	private ReferenceDocumentHandler referenceHandler;

	private List<Reference> references;
	private Set<String> visitedCommits;
	
	private Map<String, IParser> parsers;

	public DirectMetricsProcessor() {
		directMetricHandler = new DirectCodeMetricsDocumentHandler();
		referenceHandler = new ReferenceDocumentHandler();
		commitPersistence = new CommitDocumentHandler();
		visitedCommits = new HashSet<String>();
	}

	public void setRepositoryData(String repositoryId, String repositoryPath) {
		this.repositoryId = repositoryId;
		this.repositoryPath = repositoryPath;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public void setRepositoryMiner(RepositoryMiner repositoryMiner) {
		this.repositoryMiner = repositoryMiner;
	}

	public void setVisitedCommits(Set<String> visitedCommits) {
		this.visitedCommits = visitedCommits;
	}

	public void setSCM(ISCM scm) {
		this.scm = scm;
	}

	public void start() throws IOException {
		parsers = new HashMap<String, IParser>();
		for (IParser p : repositoryMiner.getParsers()) {
			for (String ext : p.getExtensions()) {
				parsers.put(ext, p);
			}
		}
		
		if (visitedCommits == null) {
			visitedCommits = new HashSet<String>();
		}
		
		processReferences();
	}

	public void startIncrementalAnalysis(List<String> commits) throws IOException {
		parsers = new HashMap<String, IParser>();
		for (IParser p : repositoryMiner.getParsers()) {
			for (String ext : p.getExtensions()) {
				parsers.put(ext, p);
			}
		}
		
		processIncrementalAnalysis(commits);
	}
	
	private void processIncrementalAnalysis(List<String> commits) throws IOException {
		int begin = 0;
		int end = Math.min(commits.size(), COMMIT_RANGE);

		while (end < commits.size()) {
			processNewCommits(commits.subList(begin, end), begin, commits.size());
			begin = end;
			end = Math.min(commits.size(), COMMIT_RANGE + end);
		}

		processNewCommits(commits.subList(begin, end), begin, commits.size());
	}
	
	private void processNewCommits(List<String> commits, int progress, int qtdCommits) throws IOException {
		for (Document doc : commitPersistence.findByIdColl(repositoryId, commits, Projections.include("diffs", "commit_date"))) {
			Commit commit = Commit.parseDocument(doc);
			
			repositoryMiner.getMiningListener().commitsProgressChange("", commit.getId(), ++progress, qtdCommits);

			scm.checkout(commit.getId());

			for (IParser parser : repositoryMiner.getParsers()) {
				parser.processSourceFolders(repositoryPath);
			}

			for (Diff diff : commit.getDiffs()) {
				if (diff.getType() != DiffType.DELETE) {
					processDiff(diff.getPath(), diff.getHash(), commit);
				}
			}
		}
	}
	
	@SuppressWarnings({"unchecked" })
	private void processReferences() throws IOException {
		for (Reference ref : references) {
			Document refDoc = referenceHandler.findById(ref.getId(), Projections.include("commits"));

			List<String> commits = (List<String>) refDoc.get("commits");
			int begin = 0;
			int end = Math.min(commits.size(), COMMIT_RANGE);

			while (end < commits.size()) {
				processCommits(commits.subList(begin, end), ref.getName(), begin, commits.size());
				begin = end;
				end = Math.min(commits.size(), COMMIT_RANGE + end);
			}

			processCommits(commits.subList(begin, end), ref.getName(), begin, commits.size());
		}
	}

	private void processCommits(List<String> commits, String refName, int progress, int qtdCommits) throws IOException {
		// not selects already processed commits
		List<String> newCommits = new ArrayList<String>();
		
		for (String commit : commits) {
			if (visitedCommits.contains(commit)) {
				repositoryMiner.getMiningListener().commitsProgressChange(refName, commit, ++progress, qtdCommits);
			} else {
				newCommits.add(commit);
			}
		}
		
		if (newCommits.size() == 0) {
			return;
		}
		
		for (Document doc : commitPersistence.findByIdColl(repositoryId, newCommits, Projections.include("diffs", "commit_date"))) {
			Commit commit = Commit.parseDocument(doc);
			
			repositoryMiner.getMiningListener().commitsProgressChange(refName, commit.getId(), ++progress, qtdCommits);

			visitedCommits.add(commit.getId());
			scm.checkout(commit.getId());

			for (IParser parser : repositoryMiner.getParsers()) {
				parser.processSourceFolders(repositoryPath);
			}

			for (Diff diff : commit.getDiffs()) {
				if (diff.getType() != DiffType.DELETE) {
					processDiff(diff.getPath(), diff.getHash(), commit);
				}
			}
		}
	}

	private void processDiff(String filePath, long fileHash, Commit commit) throws IOException {
		int index = filePath.lastIndexOf(".") + 1;
		String ext = filePath.substring(index);

		IParser currParser = parsers.get(ext);
		
		if (currParser == null) {
			return;
		}

		File f = new File(repositoryPath, filePath);

		// This used to treat links to folders
		if (f.isDirectory()) {
			return;
		}

		byte[] data = Files.readAllBytes(Paths.get(f.getCanonicalPath()));

		if (data == null) {
			return;
		}

		String source = new String(data, repositoryMiner.getCharset());
		AST ast = currParser.generate(filePath, source, repositoryMiner.getCharset());
		processFile(commit, filePath, fileHash, ast);
	}

	private void processFile(Commit commit, String file, long fileHash, AST ast) {
		Document doc = new Document();
		doc.append("commit", commit.getId());
		doc.append("commit_date", commit.getCommitDate());
		doc.append("package", ast.getDocument().getPackageDeclaration());
		doc.append("filename", file);
		doc.append("repository", new ObjectId(repositoryId));
		doc.append("filehash", fileHash);

		List<Document> thresholdsDoc = new ArrayList<Document>();
		for (IClassCodeSmell codeSmell : repositoryMiner.getClassCodeSmells()) {
			thresholdsDoc.add(codeSmell.getThresholds());
		}
		doc.append("codesmells_threshholds", thresholdsDoc);
		
		List<AbstractTypeDeclaration> types = ast.getDocument().getTypes();
		List<Document> abstractTypeDocs = new ArrayList<Document>();
		
		for (AbstractTypeDeclaration type : types) {
			Document typeDoc = new Document();
			typeDoc.append("name", type.getName()).append("declaration", type.getArchetype().toString());

			if (repositoryMiner.hasClassMetrics()) {
				processClassMetrics(ast, type, typeDoc);
			}
			
			if (repositoryMiner.hasClassCodeSmells()) {
				processClassCodeSmells(ast, type, typeDoc);
			}

			abstractTypeDocs.add(typeDoc);
		}

		doc.append("abstract_types", abstractTypeDocs);
		directMetricHandler.insert(doc);
	}

	private void processClassMetrics(AST ast, AbstractTypeDeclaration type, Document typeDoc) {
		List<Document> metricsDoc = new ArrayList<Document>();
		for (IClassMetric metric : repositoryMiner.getClassMetrics()) {
			Document mDoc = metric.calculate(type, ast);
			if (mDoc != null) {
				metricsDoc.add(mDoc);
			}
		}
		
		if (metricsDoc.size() > 0) {
			typeDoc.append("metrics", metricsDoc);
		}
	}

	private void processClassCodeSmells(AST ast, AbstractTypeDeclaration type, Document typeDoc) {
		List<Document> codeSmellsDoc = new ArrayList<Document>();
		for (IClassCodeSmell codeSmell : repositoryMiner.getClassCodeSmells()) {
			Document cDoc = codeSmell.detect(type, ast);
			if (cDoc != null) {
				codeSmellsDoc.add(cDoc);
			}
		}
		
		if (codeSmellsDoc.size() > 0) {
			typeDoc.append("codesmells", codeSmellsDoc);
		}
	}

}