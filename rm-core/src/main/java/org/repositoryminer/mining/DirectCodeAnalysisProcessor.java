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
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.IDirectCodeMetric;
import org.repositoryminer.codesmell.direct.IDirectCodeSmell;
import org.repositoryminer.model.Change;
import org.repositoryminer.model.ChangeType;
import org.repositoryminer.model.Commit;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.persistence.dao.CommitDAO;
import org.repositoryminer.persistence.dao.DirectCodeAnalysisDAO;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.util.HashingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.model.Projections;

public class DirectCodeAnalysisProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DirectCodeAnalysisProcessor.class);
	
	private static final int COMMIT_RANGE = 1000;

	private ISCM scm;
	private RepositoryMiner repositoryMiner;
	private String repositoryId;
	private String repositoryPath;

	private List<String> selectedCommits;

	private DirectCodeAnalysisDAO directAnalysisHandler = new DirectCodeAnalysisDAO();
	private CommitDAO commitPersistence = new CommitDAO();

	public void setRepositoryData(String repositoryId, String repositoryPath) {
		this.repositoryId = repositoryId;
		this.repositoryPath = repositoryPath;
	}

	public void setRepositoryMiner(RepositoryMiner repositoryMiner) {
		this.repositoryMiner = repositoryMiner;
	}

	public void setSelectedCommits(List<String> selectedCommits) {
		this.selectedCommits = selectedCommits;
	}

	public void setSCM(ISCM scm) {
		this.scm = scm;
	}

	public void start() throws IOException {
		int begin = 0;
		int end = Math.min(selectedCommits.size(), COMMIT_RANGE);

		while (end < selectedCommits.size()) {
			processCommits(selectedCommits.subList(begin, end), begin, selectedCommits.size());
			begin = end;
			end = Math.min(selectedCommits.size(), COMMIT_RANGE + end);
		}

		processCommits(selectedCommits.subList(begin, end), begin, selectedCommits.size());
	}

	private void processCommits(List<String> commits, int progress, int totalCommits) throws IOException {
		for (Document doc : commitPersistence.findByIdList(commits, Projections.include("diffs"))) {
			Commit commit = Commit.parseDocument(doc);

			scm.checkout(commit.getId());

			for (IParser parser : repositoryMiner.getParsers()) {
				parser.scanRepository(repositoryPath);
			}

			for (Change diff : commit.getDiffs()) {
				if (diff.getType() != ChangeType.DELETE) {
					processDiff(diff.getPath(), commit);
				}
			}
		}
	}

	private void processDiff(String filePath, Commit commit) throws IOException {
		File f = new File(repositoryPath, filePath);
		if (f.isDirectory()) {
			return;
		}
		
		IParser parser = null;
		for (IParser p : repositoryMiner.getParsers()) {
			if (p.accept(filePath)) {
				parser = p;
				break;
			}
		}
		
		LOGGER.info("Processing file "+filePath+" at state "+commit.getId());
		if (parser == null) {
			return;
		}

		byte[] data = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
		if (data == null) {
			return;
		}

		String source = new String(data, "utf-8");
		AST ast = parser.generate(filePath, source);
		processFile(commit, filePath, ast);
	}

	private void processFile(Commit commit, String filename, AST ast) {
		Document doc = new Document();
		doc.append("commit", commit.getId());
		doc.append("package", ast.getPackageDeclaration());
		doc.append("filename", filename);
		doc.append("repository", new ObjectId(repositoryId));
		doc.append("filename_hash", HashingUtils.encodeToCRC32(filename));

		List<Document> docTypes = new ArrayList<Document>();
		for (AbstractType type : ast.getTypes()) {
			List<Document> docMethods = new ArrayList<Document>();
			for (AbstractMethod method : type.getMethods()) {
				docMethods.add(processMethod(ast, type, method));
			}
			docTypes.add(processClass(ast, type).append("methods", docMethods));
		}

		doc.putAll(processFile(ast));
		doc.append("types", docTypes);

		directAnalysisHandler.insert(doc);
	}

	private Document processFile(AST ast) {
		Document fileDoc = new Document();

		Document metricsDoc = new Document();
		for (IDirectCodeMetric metric : repositoryMiner.getDirectCodeMetrics()) {
			Object value = metric.calculateFromFile(ast);
			if (value != null) {
				metricsDoc.append(metric.getMetric(), value);
			}
		}
		fileDoc.append("metrics", metricsDoc);

		List<String> codeSmells = new ArrayList<String>();
		for (IDirectCodeSmell codeSmell : repositoryMiner.getDirectCodeSmells()) {
			if (codeSmell.calculateFromFile(ast)) {
				codeSmells.add(codeSmell.getCodeSmell());
			}
		}
		fileDoc.append("code_smells", codeSmells);

		return fileDoc;
	}

	private Document processClass(AST ast, AbstractType type) {
		Document clazzDoc = new Document();
		clazzDoc.append("name", type.getName());
		clazzDoc.append("type", type.getNodeType().toString());

		Document metricsDoc = new Document();
		for (IDirectCodeMetric metric : repositoryMiner.getDirectCodeMetrics()) {
			Object value = metric.calculateFromClass(ast, type);
			if (value != null) {
				metricsDoc.append(metric.getMetric(), value);
			}
		}
		clazzDoc.append("metrics", metricsDoc);

		List<String> codeSmells = new ArrayList<String>();
		for (IDirectCodeSmell codeSmell : repositoryMiner.getDirectCodeSmells()) {
			if (codeSmell.calculateFromClass(ast, type)) {
				codeSmells.add(codeSmell.getCodeSmell());
			}
		}
		clazzDoc.append("code_smells", codeSmells);

		return clazzDoc;
	}

	private Document processMethod(AST ast, AbstractType type, AbstractMethod method) {
		Document methodDoc = new Document();
		methodDoc.append("name", method.getName());

		Document metricsDoc = new Document();
		for (IDirectCodeMetric metric : repositoryMiner.getDirectCodeMetrics()) {
			Object value = metric.calculateFromMethod(ast, type, method);
			if (value != null) {
				metricsDoc.append(metric.getMetric(), value);
			}
		}
		methodDoc.append("metrics", metricsDoc);

		List<String> codeSmells = new ArrayList<String>();
		for (IDirectCodeSmell codeSmell : repositoryMiner.getDirectCodeSmells()) {
			if (codeSmell.calculateFromMethod(ast, type, method)) {
				codeSmells.add(codeSmell.getCodeSmell());
			}
		}
		methodDoc.append("code_smells", codeSmells);

		return methodDoc;
	}

}