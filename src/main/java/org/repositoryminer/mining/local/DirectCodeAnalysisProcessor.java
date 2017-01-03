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

import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.codemetric.direct.IDirectCodeMetric;
import org.repositoryminer.codesmell.direct.IDirectCodeSmell;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Diff;
import org.repositoryminer.model.Reference;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.persistence.handler.DirectCodeAnalysisDocumentHandler;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.scm.DiffType;
import org.repositoryminer.scm.ISCM;

import com.mongodb.client.model.Projections;

public class DirectCodeAnalysisProcessor {

	private static final int COMMIT_RANGE = 3000;

	private ISCM scm;
	private RepositoryMiner repositoryMiner;
	private String repositoryId;
	private String repositoryPath;

	private DirectCodeAnalysisDocumentHandler directAnalysisHandler;
	private CommitDocumentHandler commitPersistence;
	private ReferenceDocumentHandler referenceHandler;

	private List<Reference> references;
	private Set<String> visitedCommits;

	private Map<String, IParser> parsers;

	public DirectCodeAnalysisProcessor() {
		directAnalysisHandler = new DirectCodeAnalysisDocumentHandler();
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
		for (Document doc : commitPersistence.findByIdColl(repositoryId, commits,
				Projections.include("diffs", "commit_date"))) {
			Commit commit = Commit.parseDocument(doc);

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

	@SuppressWarnings({ "unchecked" })
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
			if (!visitedCommits.contains(commit)) {
				newCommits.add(commit);
			}
		}

		if (newCommits.size() == 0) {
			return;
		}

		for (Document doc : commitPersistence.findByIdColl(repositoryId, newCommits,
				Projections.include("diffs", "commit_date"))) {
			Commit commit = Commit.parseDocument(doc);

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
		String ext = FilenameUtils.getExtension(filePath);
		IParser currParser = parsers.get(ext);

		if (currParser == null) {
			return;
		}

		File f = new File(repositoryPath, filePath);

		// This used to treat links to folders
		if (f.isDirectory()) {
			return;
		}

		byte[] data = Files.readAllBytes(Paths.get(f.getAbsolutePath()));

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
		for (IDirectCodeSmell codeSmell : repositoryMiner.getDirectCodeSmells()) {
			thresholdsDoc.add(codeSmell.getThresholds());
		}
		doc.append("codesmells_threshholds", thresholdsDoc);

		List<AbstractClassDeclaration> types = ast.getDocument().getTypes();
		List<Document> classesDocs = new ArrayList<Document>();

		for (AbstractClassDeclaration type : types) {
			Document typeDoc = new Document();
			typeDoc.append("name", type.getName()).append("type", type.getArchetype().toString());

			processDirectCodeMetrics(ast, type, typeDoc);
			processDirectCodeSmells(ast, type, typeDoc);

			classesDocs.add(typeDoc);
		}

		doc.append("classes", classesDocs);
		directAnalysisHandler.insert(doc);
	}

	private void processDirectCodeMetrics(AST ast, AbstractClassDeclaration cls, Document clsDoc) {
		List<Document> metricsDoc = new ArrayList<Document>();
		for (IDirectCodeMetric metric : repositoryMiner.getDirectCodeMetrics()) {
			Document mDoc = metric.calculate(cls, ast);
			if (mDoc != null) {
				metricsDoc.add(mDoc);
			}
		}

		clsDoc.append("metrics", metricsDoc);
	}

	private void processDirectCodeSmells(AST ast, AbstractClassDeclaration cls, Document clsDoc) {
		List<Document> codeSmellsDoc = new ArrayList<Document>();
		for (IDirectCodeSmell codeSmell : repositoryMiner.getDirectCodeSmells()) {
			Document cDoc = codeSmell.detect(cls, ast);
			if (cDoc != null) {
				codeSmellsDoc.add(cDoc);
			}
		}

		clsDoc.append("codesmells", codeSmellsDoc);
	}

}