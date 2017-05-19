package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.codemetric.direct.IDirectCodeMetric;
import org.repositoryminer.codesmell.direct.IDirectCodeSmell;
import org.repositoryminer.listener.mining.IMiningListener;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Diff;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.DirectCodeAnalysisDocumentHandler;
import org.repositoryminer.scm.DiffType;
import org.repositoryminer.scm.ISCM;

import com.mongodb.client.model.Projections;

public class DirectCodeAnalysisProcessor {

	private static final int COMMIT_RANGE = 3000;

	private ISCM scm;
	private RepositoryMiner repositoryMiner;
	private String repositoryId;
	private String repositoryPath;
	
	private List<String> selectedCommits;
	
	private IMiningListener listener;
	
	private DirectCodeAnalysisDocumentHandler directAnalysisHandler;
	private CommitDocumentHandler commitPersistence;
	
	private Map<String, IParser> parsers;

	public DirectCodeAnalysisProcessor() {
		directAnalysisHandler = new DirectCodeAnalysisDocumentHandler();
		commitPersistence = new CommitDocumentHandler();
	}

	public void setRepositoryData(String repositoryId, String repositoryPath) {
		this.repositoryId = repositoryId;
		this.repositoryPath = repositoryPath;
	}

	public void setRepositoryMiner(RepositoryMiner repositoryMiner) {
		this.repositoryMiner = repositoryMiner;
		listener = repositoryMiner.getMiningListener();
	}

	public void setSelectedCommits(List<String> selectedCommits) {
		this.selectedCommits = selectedCommits;
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

		int begin = 0;
		int end = Math.min(selectedCommits.size(), COMMIT_RANGE);

		listener.notifyDirectCodeAnalysisStart(selectedCommits.size());
		
		while (end < selectedCommits.size()) {
			processCommits(selectedCommits.subList(begin, end), begin, selectedCommits.size());
			begin = end;
			end = Math.min(selectedCommits.size(), COMMIT_RANGE + end);
		}

		processCommits(selectedCommits.subList(begin, end), begin, selectedCommits.size());
		listener.notifyDirectCodeAnalysisEnd(selectedCommits.size());
	}

	private void processCommits(List<String> commits, int progress, int totalCommits) throws IOException {
		for (Document doc : commitPersistence.findByIdColl(repositoryId, commits,
				Projections.include("diffs", "commit_date"))) {
			Commit commit = Commit.parseDocument(doc);
			
			listener.notifyDirectCodeAnalysisProgress(commit.getId(), progress++, totalCommits);
			
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