package org.repositoryminer.mining.local;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.metric.project.IProjectMetric;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.ProjectMetricsDocumentHandler;
import org.repositoryminer.scm.ISCM;

import com.mongodb.client.model.Projections;

public class ProjectMetricsProcessor {

	private ISCM scm;
	private RepositoryMiner repositoryMiner;
	private String repositoryId;
	private String repositoryPath;
	private List<Reference> references;
	private List<String> snapshots;

	private ProjectMetricsDocumentHandler projectMetricsHandler;
	private ReferenceDocumentHandler referencePersistence;
	private CommitDocumentHandler commitPersistence;

	public ProjectMetricsProcessor() {
		projectMetricsHandler = new ProjectMetricsDocumentHandler();
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

	public void startIncrementalAnalysis() throws IOException {
		for (int i = 0; i < references.size(); i++){
			Document doc = referencePersistence.findById(references.get(i).getId(),
					Projections.fields(Projections.include("commits"), Projections.slice("commits", 1)));

			@SuppressWarnings("unchecked")
			String commitId = ((List<String>) doc.get("commits")).get(0);
			
			if (projectMetricsHandler.hasRecord(repositoryId, commitId)) {
				snapshots.remove(commitId);
				references.remove(i);
				i--;
			}
		}
		
		for (int i = 0; i < snapshots.size(); i++) {
			if (projectMetricsHandler.hasRecord(repositoryId, snapshots.get(i))) {
				snapshots.remove(i);
				i--;
			}
		}
		
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

		if (repositoryMiner.hasProjectMetrics()) {
			processProjectMetrics(doc);
		}

		projectMetricsHandler.insert(doc);
	}

	private void processProjectMetrics(Document tagDoc) {
		List<Document> metricDocs = new ArrayList<Document>();
		for (IProjectMetric metric : repositoryMiner.getProjectMetrics()) {
			Document doc = metric.calculate(repositoryMiner.getParsers(), repositoryPath, repositoryMiner.getCharset());
			if (doc != null)
				metricDocs.add(doc);
		}

		if (metricDocs.size() > 0) {
			tagDoc.append("metrics", metricDocs);
		}
	}

}