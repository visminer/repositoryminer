package org.repositoryminer.technicaldebt;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.checkstyle.persistence.CheckstyleAuditDAO;
import org.repositoryminer.findbugs.persistence.FindBugsDAO;
import org.repositoryminer.metrics.persistence.CodeAnalysisConfigDAO;
import org.repositoryminer.metrics.persistence.CodeAnalysisDAO;
import org.repositoryminer.pmd.cpd.persistence.CPDDAO;
import org.repositoryminer.technicaldebt.model.TDIndicator;
import org.repositoryminer.technicaldebt.model.TDItem;

import com.mongodb.client.model.Projections;

public class TDFinder {

	private Map<String, TDItem> tdItems = new HashMap<>();
	private Set<TDIndicator> tdFilter;

	public Collection<TDItem> find(String commit, Set<TDIndicator> tdFilter) {
		this.tdFilter = tdFilter;

		if (hasAtLeastOneIndicator(Arrays.asList(TDIndicator.GOD_CLASS, TDIndicator.COMPLEX_METHOD,
				TDIndicator.FEATURE_ENVY, TDIndicator.BRAIN_METHOD, TDIndicator.DATA_CLASS))) {
			findCodeSmells(commit);
		}

		if (hasAtLeastOneIndicator(Arrays.asList(TDIndicator.CODE_WITHOUT_STANDARDS))) {
			findStyleProblems(commit);
		}

		if (hasAtLeastOneIndicator(Arrays.asList(TDIndicator.AUTOMATIC_STATIC_ANALYSIS_ISSUES,
				TDIndicator.SLOW_ALGORITHM, TDIndicator.MULTITHREAD_CORRECTNESS))) {
			findBugs(commit);
		}

		if (hasAtLeastOneIndicator(Arrays.asList(TDIndicator.DUPLICATED_CODE))) {
			findDuplicatedCode(commit);
		}

		return tdItems.values();
	}

	@SuppressWarnings("unchecked")
	private void findCodeSmells(String commit) {
		Document configDoc = new CodeAnalysisConfigDAO().findByCommitHash(commit, Projections.include("_id"));

		if (configDoc == null) {
			return;
		}

		CodeAnalysisDAO dao = new CodeAnalysisDAO();
		List<Document> analysisDoc = dao.findByConfig(configDoc.getObjectId("_id"),
				Projections.include("filename", "classes.codesmells", "classes.methods.codesmells"));

		for (Document fileDoc : analysisDoc) {
			TDItem tdItem = searchFile(fileDoc.getString("filename"));

			for (Document clsDoc : (List<Document>) fileDoc.get("classes", List.class)) {
				for (String codesmell : (List<String>) clsDoc.get("codesmells", List.class)) {
					addTDIndicator(tdItem, TDIndicator.getTDIndicator(codesmell), 1);
				}

				for (Document methodDoc : (List<Document>) clsDoc.get("methods", List.class)) {
					for (String codesmell : (List<String>) methodDoc.get("codesmells", List.class)) {
						addTDIndicator(tdItem, TDIndicator.getTDIndicator(codesmell), 1);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void findStyleProblems(String commit) {
		CheckstyleAuditDAO dao = new CheckstyleAuditDAO();
		List<Document> analysisDoc = dao.findByCommit(commit, Projections.include("filename", "style_problems.line"));

		for (Document fileDoc : analysisDoc) {
			TDItem tdItem = searchFile(fileDoc.getString("filename"));
			addTDIndicator(tdItem, TDIndicator.CODE_WITHOUT_STANDARDS,
					((List<Document>) fileDoc.get("style_problems")).size());
		}
	}

	@SuppressWarnings("unchecked")
	private void findDuplicatedCode(String commit) {
		CPDDAO dao = new CPDDAO();
		List<Document> analysisDoc = dao.findByCommit(commit, Projections.include("filename", "occurrences.filename"));

		for (Document doc : analysisDoc) {
			for (Document occurrence : (List<Document>) doc.get("occurrences", List.class)) {
				TDItem tdItem = searchFile(occurrence.getString("filename"));
				addTDIndicator(tdItem, TDIndicator.DUPLICATED_CODE, 1);

			}
		}
	}

	@SuppressWarnings("unchecked")
	private void findBugs(String commit) {
		FindBugsDAO dao = new FindBugsDAO();
		List<Document> analysisDoc = dao.findByCommit(commit, Projections.include("filename", "bugs.category"));

		for (Document fileDoc : analysisDoc) {
			TDItem tdItem = searchFile(fileDoc.getString("filename"));
			List<Document> bugs = (List<Document>) fileDoc.get("bugs");

			int specificBugs = 0;
			for (Document bug : bugs) {
				String category = bug.getString("category");
				if (category.equals("MT_CORRECTNESS")) {
					addTDIndicator(tdItem, TDIndicator.MULTITHREAD_CORRECTNESS, 1);
					specificBugs++;
				} else if (category.equals("PERFORMANCE")) {
					addTDIndicator(tdItem, TDIndicator.SLOW_ALGORITHM, 1);
					specificBugs++;
				}
			}

			if ((bugs.size() - specificBugs) > 0) {
				addTDIndicator(tdItem, TDIndicator.AUTOMATIC_STATIC_ANALYSIS_ISSUES, bugs.size() - specificBugs);
			}
		}
	}

	private TDItem searchFile(String filename) {
		TDItem tdItem = tdItems.get(filename);
		if (tdItem == null) {
			tdItem = new TDItem(filename);
			tdItems.put(filename, tdItem);
		}

		return tdItem;
	}

	/*
	 * Checks if the TD indicator is accepted by the filter. If the indicator is
	 * accepted by the filter its occurrences are added to the item.
	 */
	private void addTDIndicator(TDItem tdItem, TDIndicator indicator, int occurrences) {
		if (indicator == null || !tdFilter.contains(indicator)) {
			return;
		}

		tdItem.addToIndicator(indicator, occurrences);
	}

	/*
	 * Checks if the TD indicators filter has at lest one of the indicators passed
	 * as parameter.
	 */
	private boolean hasAtLeastOneIndicator(List<TDIndicator> indicators) {
		boolean result = false;
		for (TDIndicator i : indicators) {
			if (tdFilter.contains(i)) {
				result = true;
				break;
			}
		}
		return result;
	}

}