package org.repositoryminer.technicaldebt;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public Collection<TDItem> find(String commit) {
		findCodeSmells(commit);
		findStyleProblems(commit);
		findBugs(commit);
		findDuplicatedCode(commit);

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
					tdItem.addOneToIndicator(TDIndicator.getTDIndicator(codesmell));
				}

				for (Document methodDoc : (List<Document>) clsDoc.get("methods", List.class)) {
					for (String codesmell : (List<String>) methodDoc.get("codesmells", List.class)) {
						tdItem.addOneToIndicator(TDIndicator.getTDIndicator(codesmell));
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
			tdItem.addToIndicator(TDIndicator.CODE_WITHOUT_STANDARDS,
					((List<Document>) fileDoc.get("style_problems")).size());
		}
	}

	@SuppressWarnings("unchecked")
	private void findDuplicatedCode(String commit) {
		CPDDAO dao = new CPDDAO();
		List<Document> analysisDoc = dao.findByCommit(commit, Projections.include("filename", "occurrences.filename"));

		for (Document doc : analysisDoc) {
			for (Document occurrence : (List<Document>) doc.get("occurrences", List.class)) {
				searchFile(occurrence.getString("filename")).addOneToIndicator(TDIndicator.DUPLICATED_CODE);
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
					tdItem.addOneToIndicator(TDIndicator.MULTITHREAD_CORRECTNESS);
					specificBugs++;
				} else if (category.equals("PERFORMANCE")) {
					tdItem.addOneToIndicator(TDIndicator.SLOW_ALGORITHM);
					specificBugs++;
				}
			}

			if ((bugs.size() - specificBugs) > 0) {
				tdItem.addToIndicator(TDIndicator.AUTOMATIC_STATIC_ANALYSIS_ISSUES, bugs.size() - specificBugs);
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

}