package org.repositoryminer.effort.postprocessing;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bson.Document;
import org.repositoryminer.effort.model.Effort;
import org.repositoryminer.effort.model.EffortsByReference;
import org.repositoryminer.effort.persistence.handler.EffortsDocumentHandler;
import org.repositoryminer.listener.IPostMiningListener;
import org.repositoryminer.miner.RepositoryMiner;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Diff;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.Repository;
import org.repositoryminer.persistence.handler.CommitAnalysisDocumentHandler;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.postprocessing.IPostMiningTask;
import org.repositoryminer.scm.ReferenceType;

/**
 * <h1>A task to mine effort</h1>
 * <p>
 * It provides ways to calculate effort based on metrics obtained by previous
 * file-abstract-type mining. That's to say, being a post mining task, effort
 * mining relies on the metrics calculated from the source-code.
 * <p>
 * It is important that this task is executed previous to any other
 * effort-related task, since it provides all the basic elements related to
 * effort mining/(re)mining. For instance, it should be called before the
 * execution of {@link EffortCategoriesMiningTask}.
 * <p>
 * After the execution of this task, it is expected that a set of basic elements
 * related to effort ({@link EffortsByReference}) are pushed to the database
 */
public class EffortsMiningTask implements IPostMiningTask {

	private static final String TASK_NAME = "Efforts by Reference Mining";

	private Map<String, Effort> effortsMap = new TreeMap<String, Effort>();
	private FileFilter fileFilter;

	private double smellsImpactFactor = 1.0;

	public EffortsMiningTask(FileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}

	public EffortsMiningTask(FileFilter fileFilter, double smellsImpactFactor) {
		this.fileFilter = fileFilter;
		this.smellsImpactFactor = smellsImpactFactor;
	}

	@Override
	public String getTaskName() {
		return TASK_NAME;
	}

	@Override
	public void execute(RepositoryMiner repositoryMiner, Repository repository, IPostMiningListener listener) {
		ReferenceDocumentHandler handler = new ReferenceDocumentHandler();
		// prior to calculating effort, we must retrieve selected references
		// from the miner
		List<Entry<String, ReferenceType>> refs = repositoryMiner.getReferences();
		if (!refs.isEmpty()) {
			int idx = 0;
			// for each reference of the repository...
			for (Entry<String, ReferenceType> ref : refs) {
				listener.postMiningTaskProgressChange("efforts", ++idx, refs.size());
				// we must retrieve the reference from the database prior to processing it
				Document refDoc = handler.findByNameAndType(ref.getKey(), ref.getValue(), repository.getId(), null);
				Reference reference = Reference.parseDocument(refDoc);
				// let's process the reference...
				processReference(reference);
				// ...and save the calculated efforts
				save(repository.getId(), reference);

				effortsMap.clear();
			}
		}
	}

	/**
	 * Saves a mined effort to the database
	 * 
	 * @param repositoryId
	 *            the repository id being examined
	 * @param reference
	 *            a reference of the repository
	 */
	private void save(String repositoryId, Reference reference) {
		EffortsByReference effortsByRef = new EffortsByReference();

		effortsByRef.setRepository(repositoryId);
		effortsByRef.setReference(reference.getId());
		effortsByRef.setReferenceName(reference.getPath());
		effortsByRef.setEfforts(new ArrayList<Effort>(effortsMap.values()));

		EffortsDocumentHandler handler = new EffortsDocumentHandler();
		Document doc = effortsByRef.toDocument();
		handler.insert(doc);
	}

	/**
	 * Process a reference found in the repository
	 * 
	 * @param reference
	 */
	private void processReference(Reference reference) {
		CommitDocumentHandler handler = new CommitDocumentHandler();
		// as we process a reference, we must recover all commits contained in
		// it...
		List<String> commits = reference.getCommits();
		if (commits != null) {
			for (String id : commits) {
				// ...lets retrieve each commit document based on the id...
				Document doc = handler.findById(id);
				if (doc != null && !doc.isEmpty()) {
					Commit commit = Commit.parseDocument(doc);
					// ...and process it
					processCommit(commit);
				}
			}
		}
	}

	/**
	 * Process a commit found in the reference
	 * 
	 * @param commit
	 */
	private void processCommit(Commit commit) {
		// processing of commits requires navigation through its diffs...
		List<Diff> diffs = commit.getDiffs();
		if (diffs != null) {
			for (Diff diff : diffs) {
				// ...and processing each of them
				processDiff(commit, diff);
			}
		}
	}

	/**
	 * Process a collection of diff found in the commit
	 * <p>
	 * A map is used to accumulate efforts by each file affected by the commmits
	 * found in the reference. Each time a certain file is referenced by diff:
	 * <ul>
	 * <li>the number of commits that affect the file is increased
	 * <li>the amount of modification is increased by the number of added and
	 * removed lines
	 * <li>the codesmells that affect the file
	 * </ul>
	 * 
	 * @param commit
	 *            the commit being examined
	 * @param diff
	 *            the diff which belongs to the commit
	 */
	private void processDiff(Commit commit, Diff diff) {
		String effortKey = diff.getPath();

		if (fileFilter.accept(new File(effortKey))) {
			List<String> smells = getCodeSmells(diff.getHash(), commit.getId());
			// if a file was previously added to the mapping...
			if (effortsMap.containsKey(effortKey)) {
				// ...let's retrieve its instance..
				Effort effort = effortsMap.get(effortKey);
				// ...to increase affecting commits...
				effort.incCommits();
				// ...and modification...
				effort.incModifications(diff.getLinesAdded() + diff.getLinesRemoved());
				// ...and to merge the smells that affect the file
				mergeCodeSmells(effort.getCodeSmells(), smells);
			} else {
				// otherwise (no previous instance added), we create a new entry
				// for the file
				Effort effort = new Effort(effortKey, 1, diff.getLinesAdded() + diff.getLinesRemoved(), smells);
				effort.setCodeSmellsImpactFactor(smellsImpactFactor);
				effortsMap.put(effortKey, effort);
			}
		}
	}

	/**
	 * Retrieves all codesmells that effects a file
	 * 
	 * @param fileHash
	 *            the hash of the file
	 * @param commitHash
	 *            the hash of the commit
	 * @return a list of code smells' names
	 */
	@SuppressWarnings("unchecked")
	private List<String> getCodeSmells(long fileHash, String commitHash) {
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
