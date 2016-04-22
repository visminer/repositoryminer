package org.repositoryminer.analyzer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.antipattern.IAntiPattern;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.metric.IMetric;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Diff;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.persistence.handler.CommitAnalysisDocumentHandler;
import org.repositoryminer.scm.SCM;
import org.repositoryminer.scm.SCMRepository;
import org.repositoryminer.utility.HashHandler;

public class SourceAnalyzer {

	private SCM scm;
	private SCMRepository repository;
	private Map<String, IParser> parserMap;
	private String repositoryId;
	private String repositoryPath;
	private CommitAnalysisDocumentHandler persistence;

	public SourceAnalyzer(SCMRepository repository, SCM scm, String repositoryId, String repositoryPath) {
		this.scm = scm;
		this.repository = repository;
		this.repositoryId = repositoryId;
		this.repositoryPath = repositoryPath;
		this.persistence = new CommitAnalysisDocumentHandler();

		parserMap = new HashMap<String, IParser>();
		for (IParser parser : repository.getParsers()){
			parser.setCharSet(repository.getCharset());
			for (String ext : parser.getExtensions())
				parserMap.put(ext, parser);
		}
	}

	public void analyze(List<Commit> commits) throws UnsupportedEncodingException {
		for (Commit commit : commits) {
			scm.checkout(commit.getId());

			for (IParser parser : repository.getParsers())
				parser.setSourceFolders(repositoryPath);

			for (Diff diff : commit.getDiffs()) 
				processAST(diff.getPath(), diff.getHash(), commit.getId());
		}
	}

	private void processAST(String file, String fileHash, String commit) throws UnsupportedEncodingException {
		int index = file.lastIndexOf(".") + 1;
		String ext = file.substring(index);

		IParser parser = parserMap.get(ext);
		if (parser == null)
			return;

		byte[] data = scm.getData(commit, file.replaceFirst(repositoryPath + "/", ""));
		if (data == null)
			return;

		String source = new String(data, repository.getCharset());
		AST ast = parser.generate(file, source);
		process(commit, file, fileHash, ast);
	}

	private void process(String commit, String file, String hash, AST ast) {
		Document doc = new Document();
		doc.append("commit", commit);
		doc.append("file", file);
		doc.append("repository", repositoryId);
		doc.append("file_hash", hash);

		List<AbstractTypeDeclaration> types = ast.getDocument().getTypes();
		List<Document> abstractTypeDocs = new ArrayList<Document>();
		for (AbstractTypeDeclaration type : types) {
			Document typeDoc = new Document();

			String typeHash = file + "/" + type.getName();
			typeDoc.append("name", type.getName()).append("declaration", type.getType().toString()).append("hash",
					HashHandler.SHA1(typeHash));

			if (repository.getMetrics() != null) {
				List<Document> metricsDoc = new ArrayList<Document>();
				for (IMetric metric : repository.getMetrics()) {
					Document mDoc = new Document();
					metric.calculate(type, ast, mDoc);
					metricsDoc.add(mDoc);
				}
				typeDoc.append("metrics", metricsDoc);
			}

			if (repository.getAntiPatterns() != null) {
				List<Document> antiPatternsDoc = new ArrayList<Document>();
				for (IAntiPattern antiPattern : repository.getAntiPatterns()) {
					Document apDoc = new Document();
					antiPattern.detect(type, ast, apDoc);
					antiPatternsDoc.add(apDoc);
				}
				typeDoc.append("antipatterns", antiPatternsDoc);
			}

			abstractTypeDocs.add(typeDoc);
		}

		doc.append("abstract_types", abstractTypeDocs);
		persistence.insert(doc);
	}

}