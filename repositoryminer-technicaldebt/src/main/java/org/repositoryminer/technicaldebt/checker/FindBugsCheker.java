package org.repositoryminer.technicaldebt.checker;

import static org.repositoryminer.technicaldebt.model.TDIndicator.AUTOMATIC_STATIC_ANALYSIS_ISSUES;
import static org.repositoryminer.technicaldebt.model.TDIndicator.MULTITHREAD_CORRECTNESS;
import static org.repositoryminer.technicaldebt.model.TDIndicator.SLOW_ALGORITHM;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.findbugs.persistence.FindBugsDAO;
import org.repositoryminer.technicaldebt.model.TDIndicator;
import org.repositoryminer.technicaldebt.model.TDItem;

import com.mongodb.client.model.Projections;

public class FindBugsCheker extends Checker {

	@SuppressWarnings("unchecked")
	@Override
	public void check(String commit) {
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

	@Override
	public List<TDIndicator> getIndicators() {
		return Arrays.asList(AUTOMATIC_STATIC_ANALYSIS_ISSUES, SLOW_ALGORITHM, MULTITHREAD_CORRECTNESS);
	}

}
