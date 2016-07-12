package org.repositoryminer.mining;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.codesmell.commit.ICommitCodeSmell;
import org.repositoryminer.metric.IMetric;
import org.repositoryminer.parser.Parser;
import org.repositoryminer.persistence.handler.CommitAnalysisDocumentHandler;
import org.repositoryminer.persistence.model.CommitDB;
import org.repositoryminer.persistence.model.DiffDB;
import org.repositoryminer.scm.SCM;
import org.repositoryminer.technicaldebt.ITechnicalDebt;
import org.repositoryminer.utility.HashHandler;

public class SourceAnalyzer {

	private SCM scm;
	private SCMRepository repository;
	private Map<String, Parser> parserMap;
	private String repositoryId;
	private String repositoryPath;
	private CommitAnalysisDocumentHandler persistence;

	public SourceAnalyzer(SCMRepository repository, SCM scm, String repositoryId, String repositoryPath) {
		this.scm = scm;
		this.repository = repository;
		this.repositoryId = repositoryId;
		this.repositoryPath = repositoryPath;
		this.persistence = new CommitAnalysisDocumentHandler();

		parserMap = new HashMap<String, Parser>();
		for (Parser parser : repository.getParsers()){
			parser.setCharSet(repository.getCharset());
			for (String ext : parser.getExtensions())
				parserMap.put(ext, parser);
		}
	}

	public void analyze(List<CommitDB> commits) throws UnsupportedEncodingException {
		for (CommitDB commit : commits) {
			scm.checkout(commit.getId());

			for (Parser parser : repository.getParsers())
				parser.processSourceFolders(repositoryPath);

			for (DiffDB diff : commit.getDiffs()) 
				processAST(diff.getPath(), diff.getHash(), commit);
			
			scm.reset();
		}
	}

	private void processAST(String file, String fileHash, CommitDB commit) throws UnsupportedEncodingException {
		int index = file.lastIndexOf(".") + 1;
		String ext = file.substring(index);

		Parser parser = parserMap.get(ext);
		if (parser == null)
			return;

		byte[] data = scm.getData(commit.getId(), file.replaceFirst(repositoryPath + "/", ""));
		if (data == null)
			return;

		String source = new String(data, repository.getCharset());
		AST ast = parser.generate(file, source);
		process(commit, file, fileHash, ast);
	}

	private void process(CommitDB commit, String file, String hash, AST ast) {
		Document doc = new Document();
		doc.append("commit", commit.getId());
		doc.append("commit_date", commit.getCommitDate());
		doc.append("package", ast.getDocument().getPackageDeclaration());
		doc.append("file", file);
		doc.append("repository", repositoryId);
		doc.append("file_hash", hash);

		List<AbstractTypeDeclaration> types = ast.getDocument().getTypes();
		List<Document> abstractTypeDocs = new ArrayList<Document>();
		for (AbstractTypeDeclaration type : types) {
			Document typeDoc = new Document();
			String typeHash = file + "/" + type.getName();
			typeDoc.append("name", type.getName()).append("declaration", type.getArchetype().toString()).append("hash",
					HashHandler.SHA1(typeHash));
			processMetrics(ast, type, typeDoc);		

			List<Document> codeSmellsDoc = new ArrayList<Document>();
			processCodeSmells(codeSmellsDoc ,ast, type, typeDoc);
			processTechnicalDebts(codeSmellsDoc, ast, type, typeDoc);
			abstractTypeDocs.add(typeDoc);
		}

		doc.append("abstract_types", abstractTypeDocs);
		persistence.insert(doc);
	}
	
	private void processMetrics(AST ast, AbstractTypeDeclaration type, Document typeDoc) {
		if (repository.getMetrics() != null) {
			List<Document> metricsDoc = new ArrayList<Document>();
			for (IMetric metric : repository.getMetrics()) {
				Document mDoc = new Document();
				metric.calculate(type, ast, mDoc);
				metricsDoc.add(mDoc);
			}
			typeDoc.append("metrics", metricsDoc);
		}		
	}
	
	private void processCodeSmells(List<Document> codeSmellsDoc, AST ast, AbstractTypeDeclaration type, Document typeDoc) {
		if (repository.getCodeSmells() != null) {
			for (ICommitCodeSmell codeSmell : repository.getCodeSmells()) {
				Document apDoc = new Document();
				codeSmell.detect(type, ast, apDoc);
				codeSmellsDoc.add(apDoc);
			}
			typeDoc.append("codesmells", codeSmellsDoc);
		}
	}
	
	private void processTechnicalDebts(List<Document> codeSmellsDoc, AST ast, AbstractTypeDeclaration type, Document typeDoc) {
		if (repository.getTechnicalDebts() != null) {
			if (codeSmellsDoc.isEmpty()) {
				processCodeSmells(codeSmellsDoc, ast, type, typeDoc);
			}
			
			List<Document> technicalDebtsDoc = new ArrayList<Document>();
			for (ITechnicalDebt td : repository.getTechnicalDebts()) {
				Document tdDoc = new Document();
				td.detect(type, ast, codeSmellsDoc, tdDoc);
				technicalDebtsDoc.add(tdDoc);
			}
			typeDoc.append("technicaldebts", technicalDebtsDoc);
		}
	}
}