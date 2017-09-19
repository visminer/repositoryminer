package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.DirectMetricProperties;
import org.repositoryminer.codemetric.direct.IDirectCodeMetric;
import org.repositoryminer.codemetric.direct.MetricFactory;
import org.repositoryminer.codemetric.direct.MetricId;
import org.repositoryminer.codesmell.direct.CodeSmellFactory;
import org.repositoryminer.codesmell.direct.CodeSmellId;
import org.repositoryminer.codesmell.direct.DirectCodeSmellProperties;
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

	private Map<MetricId, IDirectCodeMetric> metrics = new LinkedHashMap<MetricId, IDirectCodeMetric>();
	private Map<CodeSmellId, IDirectCodeSmell> codeSmells = new LinkedHashMap<CodeSmellId, IDirectCodeSmell>();

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

		sortCodeSmells();
		sortMetrics();

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

	private void sortCodeSmells() {
		processCodeSmellsOrder(repositoryMiner.getDirectCodeSmells());
	}

	private void processCodeSmellsOrder(List<IDirectCodeSmell> directCodeSmells) {
		for (IDirectCodeSmell codeSmell : directCodeSmells) {
			DirectCodeSmellProperties props = codeSmell.getClass().getAnnotation(DirectCodeSmellProperties.class);
			verifyCodeSmellBeforeCalculation(codeSmell, props);
		}
	}

	private void verifyCodeSmellBeforeCalculation(IDirectCodeSmell codeSmell, DirectCodeSmellProperties props) {
		if (props.requisites().length == 0 && !codeSmells.containsKey(props.id())) {
			codeSmells.put(props.id(), codeSmell);
		} else if (!codeSmells.containsKey(props.id())) {
			processCodeSmellRequisites(props.requisites());
			codeSmells.put(props.id(), codeSmell);
		}
	}

	private void processCodeSmellRequisites(CodeSmellId[] requisites) {
		for (CodeSmellId codeSmellId : requisites) {
			IDirectCodeSmell codeSmell = CodeSmellFactory.getDirectCodeSmell(codeSmellId);
			DirectCodeSmellProperties props = codeSmell.getClass().getAnnotation(DirectCodeSmellProperties.class);
			verifyCodeSmellBeforeCalculation(codeSmell, props);
		}
	}

	private void sortMetrics() {
		processMetricsOrder(repositoryMiner.getDirectCodeMetrics());
		List<IDirectCodeMetric> directMetrics = new ArrayList<IDirectCodeMetric>();
		
		for (IDirectCodeSmell codeSmell : codeSmells.values()) {
			DirectCodeSmellProperties props = codeSmell.getClass().getAnnotation(DirectCodeSmellProperties.class);
			for (MetricId metricId : props.metrics()) {
				directMetrics.add(MetricFactory.getDirectCodeMetric(metricId));
			}
		}
		processMetricsOrder(directMetrics);
	}

	private void processMetricsOrder(List<IDirectCodeMetric> directMetrics) {
		for (IDirectCodeMetric metric : directMetrics) {
			DirectMetricProperties props = metric.getClass().getAnnotation(DirectMetricProperties.class);
			verifyMetricBeforeCalculation(metric, props);
		}
	}

	private void processMetricRequisites(MetricId[] requisites) {
		for (MetricId metricId : requisites) {
			IDirectCodeMetric metric = MetricFactory.getDirectCodeMetric(metricId);
			DirectMetricProperties props = metric.getClass().getAnnotation(DirectMetricProperties.class);
			verifyMetricBeforeCalculation(metric, props);
		}
	}

	private void verifyMetricBeforeCalculation(IDirectCodeMetric metric, DirectMetricProperties props) {
		if (props.requisites().length == 0 && !metrics.containsKey(props.id())) {
			metrics.put(props.id(), metric);
		} else if (!metrics.containsKey(props.id())) {
			processMetricRequisites(props.requisites());
			metrics.put(props.id(), metric);
		}
	}

	private void processFile(Commit commit, String filename, AST ast) {
		Document doc = new Document();
		doc.append("commit", commit.getId());
		doc.append("package", ast.getPackageDeclaration());
		doc.append("filename", filename);
		doc.append("repository", new ObjectId(repositoryId));
		doc.append("filename_hash", HashingUtils.encodeToCRC32(filename));

		for (IDirectCodeMetric metric : metrics.values()) {
			metric.calculate(ast);
		}

		for (IDirectCodeSmell codeSmell : codeSmells.values()) {
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

		directAnalysisHandler.insert(doc);
	}

}