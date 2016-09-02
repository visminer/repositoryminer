package org.repositoryminer.postprocessing.effort;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bson.Document;
import org.repositoryminer.listener.IProgressListener;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Diff;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.Repository;
import org.repositoryminer.model.effort.Effort;
import org.repositoryminer.model.effort.EffortsByReference;
import org.repositoryminer.persistence.handler.CommitAnalysisDocumentHandler;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.effort.EffortsDocumentHandler;
import org.repositoryminer.postprocessing.IPostMiningTask;

/**
 * <h1>A task to mine effort</h1>
 * <p>
 * It provides ways to calculate effort based on metrics related to the amount
 * of commits that affected files and the code churn (number of lines
 * added/removed).
 */
public class EffortsMiningTask implements IPostMiningTask {

	private static final String TASK_NAME = "Efforts by Reference Mining";

	private Map<String, Effort> effortsMap = new TreeMap<String, Effort>();
	private FileFilter fileFilter;

	public EffortsMiningTask(FileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}

	@Override
	public String getTaskName() {
		return TASK_NAME;
	}

	@Override
	public void execute(Repository repository, IProgressListener progressListener) {
		ReferenceDocumentHandler handler = new ReferenceDocumentHandler();

		List<Document> refDocs = handler.getByRepository(repository.getId());
		if (refDocs != null) {
			List<Reference> refs = Reference.parseDocuments(refDocs);
			if (refs != null) {
				int idx = 0;
				for (Reference ref : refs) {
					if (progressListener != null) {
						progressListener.postMiningTaskProgressChange(++idx, refs.size());
					}

					processReference(ref);
					save(repository.getId(), ref);
					effortsMap.clear();
				}
			}
		}
	}

	private void save(String repositoryId, Reference reference) {
		EffortsByReference effortsByRef = new EffortsByReference();

		effortsByRef.setRepository(repositoryId);
		effortsByRef.setReference(reference.getId());
		effortsByRef.setReferenceName(reference.getFullName());
		effortsByRef.setEfforts(new ArrayList<Effort>(effortsMap.values()));

		EffortsDocumentHandler handler = new EffortsDocumentHandler();
		Document doc = effortsByRef.toDocument();
		handler.insert(doc);
	}

	private void processReference(Reference reference) {
		CommitDocumentHandler handler = new CommitDocumentHandler();

		List<String> commits = reference.getCommits();
		if (commits != null) {
			for (String id : commits) {
				Document doc = handler.findById(id);
				if (doc != null && !doc.isEmpty()) {
					Commit commit = Commit.parseDocument(doc);

					processCommit(commit);
				}
			}
		}
	}

	private void processCommit(Commit commit) {
		List<Diff> diffs = commit.getDiffs();
		if (diffs != null) {
			for (Diff diff : diffs) {
				processDiff(commit, diff);
			}
		}
	}

	private void processDiff(Commit commit, Diff diff) {
		String effortKey = diff.getPath();

		if (fileFilter.accept(new File(effortKey))) {
			List<String> smells = getCodeSmells(diff.getHash(), commit.getId());

			if (effortsMap.containsKey(effortKey)) {
				Effort effort = effortsMap.get(effortKey);
				effort.incCommits();
				effort.incModifications(diff.getLinesAdded() + diff.getLinesRemoved());
				mergeCodeSmells(effort.getCodeSmells(), smells);
			} else {
				Effort effort = new Effort(effortKey, 1, diff.getLinesAdded() + diff.getLinesRemoved(), smells);
				effortsMap.put(effortKey, effort);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<String> getCodeSmells(String fileHash, String commitHash) {
		List<String> smells = new ArrayList<String>();

		CommitAnalysisDocumentHandler handler = new CommitAnalysisDocumentHandler();
		Document doc = handler.getCodeSmellsMeasures(fileHash, commitHash);
		if (doc != null && !doc.isEmpty()) {
			List<Document> typeDocs = (List<Document>) doc.get("abstract_types");
			if (typeDocs != null && !typeDocs.isEmpty()) {
				for (Document type : typeDocs) {
					List<Document> smellDocs = (List<Document>) type.get("codesmells");
					if (smellDocs != null && !smellDocs.isEmpty()) {
						for (Document smellDoc : smellDocs) {
							if (smellDoc.getBoolean("value", false)) {
								smells.add(smellDoc.getString("name"));
							}
						}
					}
				}
			}
		}

		return smells;
	}

	private void mergeCodeSmells(List<String> codeSmells, List<String> newCodeSmells) {
		for (String smell : newCodeSmells) {
			if (!codeSmells.contains(smell)) {
				codeSmells.add(smell);
			}
		}
	}

}
