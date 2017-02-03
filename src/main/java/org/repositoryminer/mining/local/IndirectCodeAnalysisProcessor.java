package org.repositoryminer.mining.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.codemetric.indirect.IIndirectCodeMetric;
import org.repositoryminer.codesmell.indirect.IIndirectCodeSmell;
import org.repositoryminer.listener.mining.IMiningListener;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.IndirectCodeAnalysisDocumentHandler;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.utility.StringUtils;

import com.mongodb.client.model.Projections;

public class IndirectCodeAnalysisProcessor {

	// This class keeps the file path, class name and package name in a same
	// entry. This will be used to associate a file with its classes in a faster
	// way further in the mining process.
	private static class FileEntry {

		String filePath, packageName;
		Map<String, String> classes = new LinkedHashMap<String, String>();

		public FileEntry(String filePath, String packageName) {
			this.filePath = filePath;
			this.packageName = packageName;
		}

	}

	private ISCM scm;
	private RepositoryMiner repositoryMiner;
	private String repositoryId;
	private String repositoryPath;

	private IMiningListener listener;

	private List<Reference> references;
	private List<String> snapshots;

	List<FileEntry> processedFiles;

	private IndirectCodeAnalysisDocumentHandler indirectAnalysisHandler = new IndirectCodeAnalysisDocumentHandler();
	private CommitDocumentHandler commitHandler = new CommitDocumentHandler();

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public void setRepositoryMiner(RepositoryMiner repositoryMiner) {
		this.repositoryMiner = repositoryMiner;
		listener = repositoryMiner.getMiningListener();
	}

	public void setSCM(ISCM scm) {
		this.scm = scm;
	}

	public void setSnapshots(List<String> snapshots) {
		this.snapshots = snapshots;
	}

	public void setRepositoryData(String repositoryId, String repositoryPath) {
		this.repositoryId = repositoryId;
		this.repositoryPath = repositoryPath;
	}

	public void start() throws IOException {
		for (Reference ref : references) {
			// avoids to process some commit again
			snapshots.remove(ref.getCommits().get(0));
		}

		int total = references.size() + snapshots.size();
		listener.notifyIndirectCodeAnalysisStart(total);

		processReferences();
		processCommits();

		listener.notifyIndirectCodeAnalysisEnd(total);
	}

	public void startIncrementalAnalysis() throws IOException {
		for (int i = 0; i < references.size(); i++) {
			String commitId = references.get(i).getCommits().get(0);
			if (indirectAnalysisHandler.hasRecord(repositoryId, commitId)) {
				updateReferenceInIndirectAnalysis(commitId, references.get(i));
				snapshots.remove(commitId);
				references.remove(i);
				i--;
			}
		}

		for (int i = 0; i < snapshots.size(); i++) {
			if (indirectAnalysisHandler.hasRecord(repositoryId, snapshots.get(i))) {
				snapshots.remove(i);
				i--;
			}
		}

		start();
	}

	private void updateReferenceInIndirectAnalysis(String commitId, Reference reference) {
		Document doc = indirectAnalysisHandler.find(repositoryId, commitId, Projections.include("_id"));
		indirectAnalysisHandler.updateOnlyReference(doc.getObjectId("_id"), reference.getName(), reference.getType());
	}

	private void processReferences() throws IOException {
		int index = 1;
		int total = references.size() + snapshots.size();

		for (Reference ref : references) {
			String commitId = ref.getCommits().get(0);

			listener.notifyIndirectCodeAnalysisProgress(ref.getName(), index, total);

			Commit commit = Commit.parseDocument(commitHandler.findById(commitId, Projections.include("commit_date")));
			scm.checkout(commitId);

			processFiles(commit, ref);
		}
	}

	private void processCommits() throws IOException {
		int index = references.size() + 1;
		int total = references.size() + snapshots.size();

		for (String snapshot : snapshots) {
			listener.notifyIndirectCodeAnalysisProgress(snapshot, index, total);

			Commit commit = Commit.parseDocument(commitHandler.findById(snapshot, Projections.include("commit_date")));
			scm.checkout(snapshot);
			processFiles(commit, null);
		}
	}

	private void processFiles(Commit commit, Reference ref) throws IOException {
		for (IParser parser : repositoryMiner.getParsers()) {
			processedFiles = new ArrayList<FileEntry>();
			parser.processSourceFolders(repositoryPath);

			File directory = new File(repositoryPath);
			Collection<File> files = FileUtils.listFiles(directory, parser.getExtensions(), true);

			for (File f : files) {
				byte[] data = Files.readAllBytes(Paths.get(f.getAbsolutePath()));

				if (data == null) {
					continue;
				}

				String source = new String(data, repositoryMiner.getCharset());

				String filePath = FilenameUtils.normalize(f.getAbsolutePath());
				filePath = filePath.substring(repositoryPath.length() + 1);

				AST ast = parser.generate(filePath, source, repositoryMiner.getCharset());
				processFile(filePath, ast);
			}

			saveAnalysis(commit, ref);
		}
	}

	private void processFile(String filePath, AST ast) {
		List<AbstractClassDeclaration> types = ast.getDocument().getTypes();
		FileEntry fEntry = new FileEntry(filePath, ast.getDocument().getPackageDeclaration());

		for (AbstractClassDeclaration type : types) {
			fEntry.classes.put(type.getName(), type.getArchetype().toString());

			for (IIndirectCodeMetric metric : repositoryMiner.getIndirectCodeMetrics()) {
				metric.calculate(type, ast);
			}

			for (IIndirectCodeSmell codesmell : repositoryMiner.getIndirectCodeSmell()) {
				codesmell.detect(type, ast);
			}
		}

		processedFiles.add(fEntry);
	}

	private void saveAnalysis(Commit commit, Reference ref) {
		Document doc = new Document();

		if (ref != null) {
			doc.append("reference_name", ref.getName());
			doc.append("reference_type", ref.getType().toString());
		}

		doc.append("commit", commit.getId());
		doc.append("commit_date", commit.getCommitDate());

		List<Map<String, Document>> codeMetricsResults = new ArrayList<Map<String, Document>>();
		List<Map<String, Document>> codeSmellsResults = new ArrayList<Map<String, Document>>();

		for (IIndirectCodeMetric metric : repositoryMiner.getIndirectCodeMetrics()) {
			codeMetricsResults.add(metric.getResult());
		}

		for (IIndirectCodeSmell codesmell : repositoryMiner.getIndirectCodeSmell()) {
			codeSmellsResults.add(codesmell.getResult());
		}

		for (FileEntry entry : processedFiles) {
			createDocument(doc, entry, codeMetricsResults, codeSmellsResults);
			doc.remove("_id");
		}
	}

	private void createDocument(Document doc, FileEntry fileEntry, List<Map<String, Document>> codeMetricsResults,
			List<Map<String, Document>> codeSmellsResults) {
		doc.append("package", fileEntry.packageName);
		doc.append("filename", fileEntry.filePath);
		doc.append("filehash", StringUtils.encodeToCRC32(fileEntry.filePath));
		doc.append("repository", new ObjectId(repositoryId));

		List<Document> thresholdsDoc = new ArrayList<Document>();
		for (IIndirectCodeSmell codeSmell : repositoryMiner.getIndirectCodeSmell()) {
			thresholdsDoc.add(codeSmell.getThresholds());
		}
		doc.append("codesmells_threshholds", thresholdsDoc);

		List<Document> classesDocs = new ArrayList<Document>();
		for (Entry<String, String> entry : fileEntry.classes.entrySet()) {
			Document clsDoc = new Document();
			clsDoc.append("name", entry.getKey()).append("type", entry.getValue());

			addIndirectCodeMetrics(entry.getKey(), codeMetricsResults, clsDoc);
			addIndirectCodeSmells(entry.getKey(), codeSmellsResults, clsDoc);

			classesDocs.add(clsDoc);
		}

		doc.append("classes", classesDocs);
		indirectAnalysisHandler.insert(doc);
	}

	private void addIndirectCodeMetrics(String cls, List<Map<String, Document>> codeMetricsResults, Document clsDoc) {
		List<Document> metricsDoc = new ArrayList<Document>();
		for (Map<String, Document> map : codeMetricsResults) {
			Document mDoc = map.get(cls);
			if (mDoc != null) {
				metricsDoc.add(mDoc);
			}
		}

		clsDoc.append("metrics", metricsDoc);
	}

	private void addIndirectCodeSmells(String cls, List<Map<String, Document>> codeSmellsResults, Document clsDoc) {
		List<Document> smellsDoc = new ArrayList<Document>();
		for (Map<String, Document> map : codeSmellsResults) {
			Document sDoc = map.get(cls);
			if (sDoc != null) {
				smellsDoc.add(sDoc);
			}
		}

		clsDoc.append("codesmells", smellsDoc);
	}

}