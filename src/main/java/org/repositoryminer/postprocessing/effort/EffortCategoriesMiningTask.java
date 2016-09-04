package org.repositoryminer.postprocessing.effort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.listener.IProgressListener;
import org.repositoryminer.model.Repository;
import org.repositoryminer.model.effort.Effort;
import org.repositoryminer.model.effort.EffortCategoriesByReference;
import org.repositoryminer.model.effort.EffortCategory;
import org.repositoryminer.model.effort.EffortsByReference;
import org.repositoryminer.persistence.handler.effort.EffortCategoriesDocumentHandler;
import org.repositoryminer.persistence.handler.effort.EffortsDocumentHandler;
import org.repositoryminer.postprocessing.IPostMiningTask;

/**
 * <h1>A task to agglomerate effort within categories</h1>
 * <p>
 * An instance of
 * {@link rg.repositoryminer.postprocessing.effort.IEffortCategorizer} must be
 * provided to furnish categories' names based on metrics' values obtained from
 * mined repositories. It is up to the categorizer to determine the best way
 * to calculate the threshold of each category.
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
	public void execute(Repository repository, IProgressListener progressListener) {
		EffortsDocumentHandler handler = new EffortsDocumentHandler();

		List<Document> effortDocs = handler.findAll();
		if (effortDocs != null) {
			List<EffortsByReference> effortsByReference = EffortsByReference.parseDocuments(effortDocs);
			if (effortsByReference != null) {
				processEfforts(effortsByReference, progressListener);
			}
		}
	}

	private void processEfforts(List<EffortsByReference> effortsByReference, IProgressListener progressListener) {
		int idx = 0;
		for (EffortsByReference effortsByRef : effortsByReference) {
			if (progressListener != null) {
				progressListener.postMiningTaskProgressChange(++idx, effortsByReference.size());
			}

			List<Effort> efforts = effortsByRef.getEfforts();
			if (efforts != null && !efforts.isEmpty()) {
				Collections.sort(efforts);

				double minEffort = efforts.get(0).calculateOverallEffort();
				double maxEffort = efforts.get(efforts.size() - 1).calculateOverallEffort();

				for (Effort effort : efforts) {
					String category = categorizer.getCategory(effort, minEffort, maxEffort);
					if (categoriesMap.containsKey(category)) {
						categoriesMap.get(category).add(effort);
					} else {
						List<Effort> effortz = new ArrayList<Effort>();
						effortz.add(effort);
						categoriesMap.put(category, effortz);
					}
				}

				save(effortsByRef);
				categoriesMap.clear();
			}
		}
	}

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
