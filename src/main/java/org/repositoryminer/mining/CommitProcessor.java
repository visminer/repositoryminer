package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Diff;
import org.repositoryminer.model.Reference;
import org.repositoryminer.parser.Parser;
import org.repositoryminer.persistence.handler.CommitAnalysisDocumentHandler;
import org.repositoryminer.scm.DiffType;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.scm.ISCM;

public class CommitProcessor {

	private ISCM scm;
	private RepositoryMiner repositoryMiner;
	private String repositoryId;
	private String repositoryPath;
	private CommitAnalysisDocumentHandler persistenceCommit;

	private Map<String, Commit> commitsMap;
	private List<Reference> references;
	private Set<String> commitsProcessed;

	private Parser currParser;

	public CommitProcessor() {
		this.persistenceCommit = new CommitAnalysisDocumentHandler();
		this.commitsProcessed = new HashSet<String>();
	}

	public void setRepositoryData(String repositoryId, String repositoryPath) {
		this.repositoryId = repositoryId;
		this.repositoryPath = repositoryPath;
	}

	public void setCommitsMap(Map<String, Commit> commitsMap) {
		this.commitsMap = commitsMap;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public void setRepositoryMiner(RepositoryMiner repositoryMiner) {
		this.repositoryMiner = repositoryMiner;
	}

	public void setSCM(ISCM scm) {
		this.scm = scm;
	}

	public void start() throws IOException {
		processCommits();
	}

	private void processCommits() throws IOException {
		if (!repositoryMiner.hasClassMetrics() && !repositoryMiner.hasClassCodeSmells()) {
			return;
		}

		for (Reference ref : references) {
			if (ref.getType() == ReferenceType.TIME_TAG) {
				continue;
			}

			List<String> commits = ref.getCommits();
			int idx = 0;
			for (String hash : commits) {
				if (repositoryMiner.getMiningListener() != null) {
					repositoryMiner.getMiningListener().commitsProgressChange(ref.getName(), ++idx, commits.size());
				}

				// Avoids processing again some commits
				if (!commitsProcessed.contains(hash)) {
					commitsProcessed.add(hash);
				} else {
					continue;
				}

				Commit commit = commitsMap.get(hash);
				scm.checkout(hash);

				for (Parser parser : repositoryMiner.getParsers()) {
					parser.processSourceFolders(repositoryPath);
				}

				for (Diff diff : commit.getDiffs()) {
					if (diff.getType() != DiffType.DELETE) {
						processDiff(diff.getPath(), diff.getHash(), commit);
					}
				}
			}
		}
	}

	private void processDiff(String filePath, long fileHash, Commit commit) throws IOException {
		int index = filePath.lastIndexOf(".") + 1;
		String ext = filePath.substring(index);

		if (currParser == null || !currParser.getExtensions().contains(ext)) {
			for (Parser p : repositoryMiner.getParsers()) {
				if (p.getExtensions().contains(ext)) {
					currParser = p;
				}
			}
		}

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
		AST ast = currParser.generate(filePath, source);
		processFile(commit, filePath, fileHash, ast);
	}

	private void processFile(Commit commit, String file, long fileHash, AST ast) {
		Document doc = new Document();
		doc.append("commit", commit.getId());
		doc.append("commit_date", commit.getCommitDate());
		doc.append("package", ast.getDocument().getPackageDeclaration());
		doc.append("filename", file);
		doc.append("repository", new ObjectId(repositoryId));
		doc.append("file_hash", fileHash);

		List<AbstractTypeDeclaration> types = ast.getDocument().getTypes();
		List<Document> abstractTypeDocs = new ArrayList<Document>();
		for (AbstractTypeDeclaration type : types) {
			Document typeDoc = new Document();
			typeDoc.append("name", type.getName()).append("declaration", type.getArchetype().toString());

			processClassMetrics(ast, type, typeDoc);
			processClassCodeSmells(ast, type, typeDoc);

			abstractTypeDocs.add(typeDoc);
		}

		doc.append("abstract_types", abstractTypeDocs);
		persistenceCommit.insert(doc);
	}

	private void processClassMetrics(AST ast, AbstractTypeDeclaration type, Document typeDoc) {
		if (!repositoryMiner.hasClassMetrics()) {
			return;
		}

		List<Document> metricsDoc = new ArrayList<Document>();
		for (IClassMetric metric : repositoryMiner.getClassMetrics()) {
			Document mDoc = new Document();
			metric.calculate(type, ast, mDoc);
			if (!mDoc.isEmpty()) {
				metricsDoc.add(mDoc);
			}
		}
		typeDoc.append("metrics", metricsDoc);
	}

	private void processClassCodeSmells(AST ast, AbstractTypeDeclaration type, Document typeDoc) {
		if (!repositoryMiner.hasClassCodeSmells()) {
			return;
		}

		List<Document> codeSmellsDoc = new ArrayList<Document>();
		for (IClassCodeSmell codeSmell : repositoryMiner.getClassCodeSmells()) {
			Document cDoc = new Document();
			codeSmell.detect(type, ast, cDoc);
			if (!cDoc.isEmpty()) {
				codeSmellsDoc.add(cDoc);
			}
		}
		typeDoc.append("codesmells", codeSmellsDoc);
	}

}