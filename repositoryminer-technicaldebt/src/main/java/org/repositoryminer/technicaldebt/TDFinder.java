package org.repositoryminer.technicaldebt;

import static org.repositoryminer.technicaldebt.model.TDIndicator.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.checkstyle.persistence.CheckstyleAuditDAO;
import org.repositoryminer.excomment.persistence.ExCommentDAO;
import org.repositoryminer.findbugs.persistence.FindBugsDAO;
import org.repositoryminer.metrics.persistence.CodeAnalysisReportDAO;
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

		if (hasAtLeastOneIndicator(Arrays.asList(GOD_CLASS, COMPLEX_METHOD, FEATURE_ENVY, BRAIN_METHOD, DATA_CLASS))) {
			findCodeSmells(commit);
		}

		if (tdFilter.contains(CODE_WITHOUT_STANDARDS)) {
			findStyleProblems(commit);
		}

		if (hasAtLeastOneIndicator(Arrays.asList(AUTOMATIC_STATIC_ANALYSIS_ISSUES, SLOW_ALGORITHM, 
				MULTITHREAD_CORRECTNESS))) {
			findBugs(commit);
		}

		if (tdFilter.contains(DUPLICATED_CODE)) {
			findDuplicatedCode(commit);
		}

		if (hasAtLeastOneIndicator(Arrays.asList(COMMENT_ANALYSIS_ARCHITECTURE_DEBT, COMMENT_ANALYSIS_BUILD_DEBT,
				COMMENT_ANALYSIS_CODE_DEBT, COMMENT_ANALYSIS_DEFECT_DEBT, COMMENT_ANALYSIS_DESIGN_DEBT, 
				COMMENT_ANALYSIS_DOCUMENTATION_DEBT, COMMENT_ANALYSIS_PEOPLE_DEBT,
				COMMENT_ANALYSIS_REQUIREMENT_DEBT, COMMENT_ANALYSIS_TEST_DEBT, COMMENT_ANALYSIS_UNKNOWN_DEBT))) {
			findComments(commit);
		}
		
		return tdItems.values();
	}

	@SuppressWarnings("unchecked")
	private void findCodeSmells(String commit) {
		Document reportDoc = new CodeAnalysisReportDAO().findByCommitHash(commit, Projections.include("_id"));

		if (reportDoc == null) {
			return;
		}

		CodeAnalysisDAO dao = new CodeAnalysisDAO();
		List<Document> analysisDoc = dao.findByReport(reportDoc.getObjectId("_id"),
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
			addTDIndicator(tdItem, CODE_WITHOUT_STANDARDS,
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
				addTDIndicator(tdItem, DUPLICATED_CODE, 1);

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
					addTDIndicator(tdItem, MULTITHREAD_CORRECTNESS, 1);
					specificBugs++;
				} else if (category.equals("PERFORMANCE")) {
					addTDIndicator(tdItem, SLOW_ALGORITHM, 1);
					specificBugs++;
				}
			}

			if ((bugs.size() - specificBugs) > 0) {
				addTDIndicator(tdItem, AUTOMATIC_STATIC_ANALYSIS_ISSUES, bugs.size() - specificBugs);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void findComments(String commit) {
		ExCommentDAO dao = new ExCommentDAO();
		List<Document> analysisDoc = dao.findByCommit(commit, Projections.include("filename",
				"comments.patterns.tdtype"));
		
		for (Document fileDoc : analysisDoc) {
			TDItem tdItem = searchFile(fileDoc.getString("filename"));
		
			for (Document comment : (List<Document>) fileDoc.get("comments")) {
				for (Document pattern : (List<Document>) comment.get("patterns")) {
					String tdtype = pattern.getString("tdtype").replace(' ', '_').toUpperCase();

					if (tdtype.length() == 0) {
						addTDIndicator(tdItem, COMMENT_ANALYSIS_UNKNOWN_DEBT, 1);
					} else {
						addTDIndicator(tdItem, TDIndicator.getTDIndicator("COMMENT_ANALYSIS_"+tdtype), 1);
					}
				}
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