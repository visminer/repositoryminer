package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.codesmell.clazz.IClassCodeSmell;
import org.repositoryminer.codesmell.project.IProjectCodeSmell;
import org.repositoryminer.listener.IProgressListener;
import org.repositoryminer.metric.clazz.IClassMetric;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Diff;
import org.repositoryminer.model.Reference;
import org.repositoryminer.parser.Parser;
import org.repositoryminer.persistence.handler.CommitAnalysisDocumentHandler;
import org.repositoryminer.persistence.handler.TagAnalysisDocumentHandler;
import org.repositoryminer.scm.DiffType;
import org.repositoryminer.scm.SCM;
import org.repositoryminer.technicaldebt.ITechnicalDebt;

public class SourceAnalyzer {

	private SCM scm;
	private RepositoryMiner repositoryMiner;
	private List<Parser> parsers;
	private String repositoryId;
	private String repositoryPath;
	private CommitAnalysisDocumentHandler persistenceCommit;
	private TagAnalysisDocumentHandler persistenceTag;

	private List<Commit> commits;
	private List<Reference> tags;

	private Parser parser;
	private IProgressListener progressListener;

	public SourceAnalyzer(RepositoryMiner repositoryMiner, SCM scm, String repositoryId, String repositoryPath) {
		this.scm = scm;
		this.repositoryMiner = repositoryMiner;
		this.repositoryId = repositoryId;
		this.repositoryPath = repositoryPath;
		this.persistenceCommit = new CommitAnalysisDocumentHandler();
		this.persistenceTag = new TagAnalysisDocumentHandler();
		this.parsers = repositoryMiner.getParsers();

		for (Parser parser : repositoryMiner.getParsers()) {
			parser.setCharSet(repositoryMiner.getCharset());
		}
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	public void setTags(List<Reference> tags) {
		this.tags = tags;
	}

	public void analyze() throws IOException {
		progressListener = repositoryMiner.getProgressListener();

		if (progressListener != null) {
			progressListener.initSourceAnalysisProgress();
		}

		analyzeCommits();
		analyzeTags();
	}

	private void analyzeCommits() throws IOException {
		if (repositoryMiner.hasClassMetrics() || repositoryMiner.hasClassCodeSmells()
				|| repositoryMiner.hasTechnicalDebts()) {
			int idx = 0;
			for (Commit commit : commits) {
				if (progressListener != null) {
					progressListener.commitsProgressChange(++idx, commits.size());
				}

				scm.checkout(commit.getId());

				for (Parser parser : repositoryMiner.getParsers()) {
					parser.processSourceFolders(repositoryPath);
				}

				for (Diff diff : commit.getDiffs()) {
					if (diff.getType() != DiffType.DELETE) {
						processAST(diff.getPath(), diff.getHash(), commit);
					}
				}

				scm.reset();
			}
		}
	}

	private void analyzeTags() {
		if (repositoryMiner.hasProjectsCodeSmells()) {
			int idx = 0;
			for (Reference tag : tags) {
				if (progressListener != null) {
					progressListener.tagsProgressChange(++idx, tags.size());
				}

				String commitId = tag.getCommits().get(0);
				scm.checkout(commitId);

				for (Parser parser : repositoryMiner.getParsers()) {
					parser.processSourceFolders(repositoryPath);
				}

				int index = commits.indexOf(new Commit(commitId));
				Commit commit = commits.get(index);
				processTag(commit, tag);

				scm.reset();
			}
		}
	}

	private void processAST(String filePath, long fileHash, Commit commit) throws IOException {
		int index = filePath.lastIndexOf(".") + 1;
		String ext = filePath.substring(index);

		if (parser == null || !parser.getExtensions().contains(ext)) {
			for (Parser p : parsers) {
				if (p.getExtensions().contains(ext)) {
					parser = p;
				}
			}
		}

		if (parser == null) {
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
		AST ast = parser.generate(filePath, source);
		processCommit(commit, filePath, fileHash, ast);
	}

	private void processTag(Commit commit, Reference tag) {
		Document doc = new Document();
		doc.append("tag", tag.getName());
		doc.append("tag_type", tag.getType().toString());
		doc.append("commit", commit.getId());
		doc.append("commit_date", commit.getCommitDate());
		doc.append("repository", new ObjectId(repositoryId));

		processProjectCodeSmells(doc);
		persistenceTag.insert(doc);
	}

	private void processProjectCodeSmells(Document tagDoc) {
		List<Document> codeSmellsDocs = new ArrayList<Document>();
		for (IProjectCodeSmell codeSmell : repositoryMiner.getProjectCodeSmells()) {
			Document doc = new Document();
			codeSmell.detect(parsers, repositoryPath, doc);
			if (!doc.isEmpty()) {
				codeSmellsDocs.add(doc);
			}
		}
		tagDoc.append("code_smells", codeSmellsDocs);
	}

	private void processCommit(Commit commit, String file, long fileHash, AST ast) {
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

			List<Document> codeSmellsDocs = new ArrayList<Document>();
			processCommitMetrics(ast, type, typeDoc);
			processCommitCodeSmells(codeSmellsDocs, ast, type, typeDoc);
			processTechnicalDebts(codeSmellsDocs, ast, type, typeDoc);

			abstractTypeDocs.add(typeDoc);
		}

		doc.append("abstract_types", abstractTypeDocs);
		persistenceCommit.insert(doc);
	}

	private void processCommitMetrics(AST ast, AbstractTypeDeclaration type, Document typeDoc) {
		if (repositoryMiner.hasClassMetrics()) {
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
	}

	private void processCommitCodeSmells(List<Document> codeSmellsDocs, AST ast, AbstractTypeDeclaration type,
			Document typeDoc) {
		if (repositoryMiner.hasClassCodeSmells()) {
			for (IClassCodeSmell codeSmell : repositoryMiner.getClassCodeSmells()) {
				Document cDoc = new Document();
				codeSmell.detect(type, ast, cDoc);
				if (!cDoc.isEmpty()) {
					codeSmellsDocs.add(cDoc);
				}
			}
			typeDoc.append("codesmells", codeSmellsDocs);
		}
	}

	/** FIXME: Needs revision **/
	private void processTechnicalDebts(List<Document> codeSmellsDoc, AST ast, AbstractTypeDeclaration type,
			Document typeDoc) {
		if (repositoryMiner.hasTechnicalDebts()) {
			List<Document> technicalDebtsDoc = new ArrayList<Document>();
			for (ITechnicalDebt td : repositoryMiner.getTechnicalDebts()) {
				Document tdDoc = new Document();
				td.detect(type, ast, codeSmellsDoc, tdDoc);
				if (!tdDoc.isEmpty()) {
					technicalDebtsDoc.add(tdDoc);
				}
			}
			typeDoc.append("technicaldebts", technicalDebtsDoc);
		}
	}

}