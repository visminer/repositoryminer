package org.repositoryminer.effort.postprocessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.effort.model.Effort;
import org.repositoryminer.effort.model.EffortCategoriesByReference;
import org.repositoryminer.effort.model.EffortCategory;
import org.repositoryminer.effort.model.EffortsByReference;
import org.repositoryminer.effort.persistence.EffortCategoriesDocumentHandler;
import org.repositoryminer.effort.persistence.EffortsDocumentHandler;
import org.repositoryminer.listener.postmining.IPostMiningListener;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.model.Repository;
import org.repositoryminer.postmining.IPostMiningTask;

/**
 * <h1>A task to agglomerate effort within categories</h1>
 * <p>
 * An instance of
 * {@link rg.repositoryminer.postprocessing.effort.IEffortCategorizer} must be
 * provided to furnish categories' names based on the measured efforts. It's up
 * to the categorizer to determine the best way to calculate the threshold of
 * each category. In this case, prior to mining categories of efforts, one must
 * implement and inject an instance of a categorizer.
 * <p>
 * Prior to the execution of this task, {@link EffortsMiningTask} must be called
 * to insert a basic set of information related to efforts.
 * <p>
 * After the execution of this task it is expected the production of a list of
 * documents relating all efforts to a set of categories as in
 * {@link EffortCategoriesByReference}
 * <p>
 */
public class EffortCategoriesMiningTask implements IPostMiningTask {

	private static final String TASK_NAME = "Efforts by Category Mining";

	private Map<String, List<Effort>> categoriesMap = new HashMap<String, List<Effort>>();
	private IEffortCategorizer categorizer;

	public EffortCategoriesMiningTask(IEffortCategorizer categorizer) {
		this.categorizer = categorizer;
	}

	@Override
	public String getTaskName() {
		return TASK_NAME;
	}

	@Override
	public void execute(RepositoryMiner repositoryMiner, Repository repository, IPostMiningListener listener) {
		EffortsDocumentHandler handler = new EffortsDocumentHandler();
		// let's get all efforts previously save in the database...
		List<Document> effortDocs = handler.findAll();
		if (effortDocs != null) {
			List<EffortsByReference> effortsByReference = EffortsByReference.parseDocuments(effortDocs);
			if (effortsByReference != null) {
				// and process each effort
				processEfforts(effortsByReference, listener);
			}
		}
	}

	/**
	 * Performs the action classification of efforts in categories
	 * <p>
	 * It uses a instance of categorizer to obtain the name of the category
	 * which the effort belongs to. The classification if based on how the
	 * overall calculated effort fits within a min and a max thresholds.
	 * 
	 * @param effortsByReference
	 *            collection of efforts by ref previously mined/saved
	 * @param progressListener
	 *            instance of progress listener to provide info about the
	 *            processing steps
	 */
	private void processEfforts(List<EffortsByReference> effortsByReference, IPostMiningListener listener) {
		int idx = 0;
		for (EffortsByReference effortsByRef : effortsByReference) {
			listener.notifyTaskProgress("effort categories", ++idx, effortsByReference.size());
			// let's get the list of efforts...
			List<Effort> efforts = effortsByRef.getEfforts();
			if (efforts != null && !efforts.isEmpty()) {
				// ...and sort them based on the overall effort value
				Collections.sort(efforts);

				// after sorting, the min effort value is the obtained from the
				// firts element...
				double minEffort = efforts.get(0).calculateOverallEffort();
				// ...and the max from the last item
				double maxEffort = efforts.get(efforts.size() - 1).calculateOverallEffort();

				for (Effort effort : efforts) {
					// that given, let's categorize each effort within the min
					// and max thresholds
					String category = categorizer.getCategory(effort, minEffort, maxEffort);
					if (categoriesMap.containsKey(category)) {
						categoriesMap.get(category).add(effort);
					} else {
						List<Effort> effortz = new ArrayList<Effort>();
						effortz.add(effort);
						categoriesMap.put(category, effortz);
					}
				}
				// and save the whole collection of categories in the end
				save(effortsByRef);
				categoriesMap.clear();
			}
		}
	}

	/**
	 * saves the list of efforts per category
	 * 
	 * @param effortsByReference
	 */
	private void save(EffortsByReference effortsByReference) {
		EffortCategoriesByReference effortCategoriesByRef = new EffortCategoriesByReference();
		effortCategoriesByRef.setRepository(effortsByReference.getRepository());
		effortCategoriesByRef.setReference(effortsByReference.getReference());
		effortCategoriesByRef.setReferenceName(effortsByReference.getReferenceName());

		List<EffortCategory> categories = new ArrayList<EffortCategory>();
		for (Map.Entry<String, List<Effort>> entry : categoriesMap.entrySet()) {
			categories.add(new EffortCategory(entry.getKey(), entry.getValue()));
		}
		effortCategoriesByRef.setCategories(categories);

		EffortCategoriesDocumentHandler handler = new EffortCategoriesDocumentHandler();
		handler.insert(effortCategoriesByRef.toDocument());
	}

}
