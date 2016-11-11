package org.repositoryminer.mining;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.codesmell.project.IProjectCodeSmell;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.parser.Parser;
import org.repositoryminer.persistence.handler.SnapshotAnalysisDocumentHandler;
import org.repositoryminer.scm.ISCM;

public class SnapshotProcessor {

	private ISCM scm;
	private RepositoryMiner repositoryMiner;
	private String repositoryId;
	private String repositoryPath;
	private SnapshotAnalysisDocumentHandler persistenceSnapshot;
	private List<Reference> references;
	private Map<String, Commit> commitsMap;

	public SnapshotProcessor() {
		this.persistenceSnapshot = new SnapshotAnalysisDocumentHandler();
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public void setRepositoryMiner(RepositoryMiner repositoryMiner) {
		this.repositoryMiner = repositoryMiner;
	}

	public void setCommitsMap(Map<String, Commit> commitsMap) {
		this.commitsMap = commitsMap;
	}

	public void setSCM(ISCM scm) {
		this.scm = scm;
	}

	public void setRepositoryData(String repositoryId, String repositoryPath) {
		this.repositoryId = repositoryId;
		this.repositoryPath = repositoryPath;
	}

	public void start() throws IOException {
		processSnapshots();
	}

	private void processSnapshots() {
		if (repositoryMiner.hasProjectsCodeSmells()) {
			int idx = 0;
			for (Reference ref : references) {
				if (repositoryMiner.getMiningListener() != null) {
					repositoryMiner.getMiningListener().tagsProgressChange(ref.getName(), ++idx, references.size());
				}

				String commitId = ref.getCommits().get(0);
				scm.checkout(commitId);

				for (Parser parser : repositoryMiner.getParsers()) {
					parser.processSourceFolders(repositoryPath);
				}

				processSnapshot(commitsMap.get(commitId), ref);
			}
		}
	}

	private void processSnapshot(Commit commit, Reference ref) {
		Document doc = new Document();
		doc.append("reference_name", ref.getName());
		doc.append("reference_type", ref.getType().toString());
		doc.append("commit", commit.getId());
		doc.append("commit_date", commit.getCommitDate());
		doc.append("repository", new ObjectId(repositoryId));

		processProjectCodeSmells(doc);
		persistenceSnapshot.insert(doc);
	}

	private void processProjectCodeSmells(Document tagDoc) {
		List<Document> codeSmellsDocs = new ArrayList<Document>();
		for (IProjectCodeSmell codeSmell : repositoryMiner.getProjectCodeSmells()) {
			Document doc = new Document();
			codeSmell.detect(repositoryMiner.getParsers(), repositoryPath, doc);
			if (!doc.isEmpty()) {
				codeSmellsDocs.add(doc);
			}
		}
		tagDoc.append("code_smells", codeSmellsDocs);
	}

}