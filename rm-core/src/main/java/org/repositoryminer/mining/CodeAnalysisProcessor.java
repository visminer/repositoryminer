package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codesmell.CodeSmellFactory;
import org.repositoryminer.codesmell.CodeSmellID;
import org.repositoryminer.codesmell.ICodeSmell;
import org.repositoryminer.domain.Change;
import org.repositoryminer.domain.ChangeType;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.metric.IMetric;
import org.repositoryminer.metric.MetricFactory;
import org.repositoryminer.metric.MetricID;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.persistence.dao.CodeAnalysisDAO;
import org.repositoryminer.persistence.dao.CommitDAO;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.util.HashingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.model.Projections;

public class CodeAnalysisProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeAnalysisProcessor.class);

	private ISCM scm;
	private RepositoryMiner rm;
	private String repoId;
	private String repoPath;
	private Set<String> selectedCommits;

	private CodeAnalysisDAO codeAnalysisDAO = new CodeAnalysisDAO();
	private CommitDAO commitDAO = new CommitDAO();

	private Map<MetricID, IMetric> metrics = new LinkedHashMap<MetricID, IMetric>();
	private Map<CodeSmellID, ICodeSmell> codeSmells = new LinkedHashMap<CodeSmellID, ICodeSmell>();

	public void start() throws IOException {
		if (rm.hasMetrics()) {
			for (IMetric metric : rm.getMetrics())
				visitMetric(metric);
		}

		if (rm.hasCodeSmells()) {
			for (ICodeSmell codeSmell : rm.getCodeSmells())
				visitCodeSmell(codeSmell);
		}
		
		for (String commitName : selectedCommits) {
			Commit commit = Commit.parseDocument(commitDAO.findById(commitName, Projections.include("diffs")));
			scm.checkout(commit.getId());

			for (IParser parser : rm.getParsers())
				parser.scanRepository(repoPath);

			for (Change diff : commit.getDiffs())
				if (diff.getType() != ChangeType.DELETE)
					processDiff(diff.getPath(), commit);
		}
	}

	private void processDiff(String filePath, Commit commit) throws IOException {
		File f = new File(repoPath, filePath);
		if (f.isDirectory()) {
			return;
		}

		IParser parser = null;
		for (IParser p : rm.getParsers()) {
			if (p.accept(filePath)) {
				parser = p;
				break;
			}
		}

		LOGGER.info("Processing file " + filePath + " at state " + commit.getId());
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
		doc.append("repository", new ObjectId(repoId));
		doc.append("filename_hash", HashingUtils.encodeToCRC32(filename));

		for (IMetric metric : metrics.values()) {
			metric.calculate(ast);
		}

		for (ICodeSmell codeSmell : codeSmells.values()) {
			codeSmell.detect(ast);
		}

		doc.append("metrics", ast.convertMetrics());
		List<Document> docMethods1 = new ArrayList<Document>();
		for (AbstractMethod method : ast.getMethods()) {
			docMethods1.add(new Document("name", method.getName()).append("metrics", method.convertMetrics())
					.append("code_smells", method.convertCodeSmells()));
		}
		doc.append("methods", docMethods1);

		List<Document> docTypes = new ArrayList<Document>();
		for (AbstractType type : ast.getTypes()) {
			List<Document> docMethods2 = new ArrayList<Document>();
			for (AbstractMethod method : type.getMethods()) {
				docMethods2.add(new Document("name", method.getName()).append("metrics", method.convertMetrics())
						.append("code_smells", method.convertCodeSmells()));
			}
			docTypes.add(new Document("name", type.getName()).append("metrics", type.convertMetrics())
					.append("code_smells", type.convertCodeSmells()).append("methods", docMethods2));
		}
		doc.append("types", docTypes);

		codeAnalysisDAO.insert(doc);
	}

	private void visitMetric(IMetric metricParam) {
		if (metricParam.getRequiredMetrics() != null) {
			for (MetricID id: metricParam.getRequiredMetrics()) {
				if (!metrics.containsKey(id)) {
					IMetric metric = MetricFactory.getMetric(id);
					if (metric.getRequiredMetrics() == null)
						metrics.put(id, metric);
					else
						visitMetric(metric);
				}
			}
		}

		if (!metrics.containsKey(metricParam.getId()))
			metrics.put(metricParam.getId(), metricParam);
	}

	private void visitCodeSmell(ICodeSmell codeSmellParam) {
		if (codeSmellParam.getRequiredMetrics() != null) {
			for (MetricID id : codeSmellParam.getRequiredMetrics()) {
				IMetric metric = MetricFactory.getMetric(id);
				visitMetric(metric);
			}
		}
		
		if (codeSmellParam.getRequiredCodeSmells() != null) {
			for (CodeSmellID id : codeSmellParam.getRequiredCodeSmells()) {
				if (!codeSmells.containsKey(id)) {
					ICodeSmell codeSmell = CodeSmellFactory.getCodeSmell(id);
					if (codeSmell.getRequiredCodeSmells() == null)
						codeSmells.put(id, codeSmell);
					else
						visitCodeSmell(codeSmell);
				}
			}
		}

		if (!codeSmells.containsKey(codeSmellParam.getId()))
			codeSmells.put(codeSmellParam.getId(), codeSmellParam);
	}

	/*** GETTERS AND SETTERS ***/

	public void setRm(RepositoryMiner rm) {
		this.rm = rm;
		this.scm = rm.getScm();
	}

	public void setRepoId(String repoId) {
		this.repoId = repoId;
	}

	public void setRepoPath(String repoPath) {
		this.repoPath = repoPath;
	}

	public void setSelectedCommits(Set<String> selectedCommits) {
		this.selectedCommits = selectedCommits;
	}

}