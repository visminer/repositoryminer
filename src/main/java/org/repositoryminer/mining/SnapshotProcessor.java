package org.repositoryminer.mining;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.codesmell.project.IProjectCodeSmell;
import org.repositoryminer.metric.project.IProjectMetric;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.SnapshotAnalysisDocumentHandler;
import org.repositoryminer.scm.ISCM;

import com.mongodb.client.model.Projections;

public class SnapshotProcessor {

	private ISCM scm;
	private RepositoryMiner repositoryMiner;
	private String repositoryId;
	private String repositoryPath;
	private List<Reference> references;
	private List<String> snapshots;

	private SnapshotAnalysisDocumentHandler snapshotPersistence;
	private ReferenceDocumentHandler referencePersistence;
	private CommitDocumentHandler commitPersistence;

	public SnapshotProcessor() {
		snapshotPersistence = new SnapshotAnalysisDocumentHandler();
		referencePersistence = new ReferenceDocumentHandler();
		commitPersistence = new CommitDocumentHandler();
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

	public void setSnapshots(List<String> snapshots) {
		this.snapshots = snapshots;
	}

	public void setRepositoryData(String repositoryId, String repositoryPath) {
		this.repositoryId = repositoryId;
		this.repositoryPath = repositoryPath;
	}

	public void start() throws IOException {
		processReferences();
		processCommits();
	}

	public void startUpdate() throws IOException {
		List<String> commitsToRemove = new ArrayList<String>();
		commitsToRemove.addAll(snapshots);

		for (Reference ref : references) {
			Document doc = referencePersistence.findById(ref.getId(),
					Projections.fields(Projections.include("commits"), Projections.slice("commits", 1)));

			@SuppressWarnings("unchecked")
			String commitId = ((List<String>) doc.get("commits")).get(0);
			commitsToRemove.add(commitId);
		}
		
		snapshotPersistence.deleteByCommits(repositoryId, commitsToRemove);
		start();
	}

	private void processCommits() {
		for (String snapshot : snapshots) {
			Commit commit = Commit
					.parseDocument(commitPersistence.findById(snapshot, Projections.include("commit_date")));

			scm.checkout(snapshot);
			for (IParser parser : repositoryMiner.getParsers()) {
				parser.processSourceFolders(repositoryPath);
			}

			processSnapshot(commit, null);
		}
	}

	private void processReferences() {
		int idx = 0;
		for (Reference ref : references) {
			repositoryMiner.getMiningListener().tagsProgressChange(ref.getName(), ++idx, references.size());

			Document doc = referencePersistence.findById(ref.getId(),
					Projections.fields(Projections.include("commits"), Projections.slice("commits", 1)));

			@SuppressWarnings("unchecked")
			String commitId = ((List<String>) doc.get("commits")).get(0);

			snapshots.remove(commitId); // avoids to process some commit again

			Commit commit = Commit
					.parseDocument(commitPersistence.findById(commitId, Projections.include("commit_date")));

			scm.checkout(commitId);
			for (IParser parser : repositoryMiner.getParsers()) {
				parser.processSourceFolders(repositoryPath);
			}

			processSnapshot(commit, ref);
		}
	}

	private void processSnapshot(Commit commit, Reference ref) {
		Document doc = new Document();

		if (ref != null) {
			doc.append("reference_name", ref.getName());
			doc.append("reference_type", ref.getType().toString());
		}

		doc.append("commit", commit.getId());
		doc.append("commit_date", commit.getCommitDate());
		doc.append("repository", new ObjectId(repositoryId));

		if (repositoryMiner.hasProjectsCodeSmells())
			processProjectCodeSmells(doc);

		if (repositoryMiner.hasProjectMetrics())
			processProjectMetrics(doc);

		snapshotPersistence.insert(doc);
	}

	private void processProjectCodeSmells(Document tagDoc) {
		List<Document> codeSmellsDocs = new ArrayList<Document>();
		for (IProjectCodeSmell codeSmell : repositoryMiner.getProjectCodeSmells()) {
			Document doc = codeSmell.detect(repositoryMiner.getParsers(), repositoryPath, repositoryMiner.getCharset());
			if (doc != null)
				codeSmellsDocs.add(doc);
		}

		if (codeSmellsDocs.size() > 0)
			tagDoc.append("code_smells", codeSmellsDocs);
	}

	private void processProjectMetrics(Document tagDoc) {
		List<Document> metricDocs = new ArrayList<Document>();
		for (IProjectMetric metric : repositoryMiner.getProjectMetrics()) {
			Document doc = metric.calculate(repositoryMiner.getParsers(), repositoryPath, repositoryMiner.getCharset());
			if (doc != null)
				metricDocs.add(doc);
		}

		if (metricDocs.size() > 0)
			tagDoc.append("metrics", metricDocs);
	}

}