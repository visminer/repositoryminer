package org.repositoryminer.technicaldebt;

import static com.mongodb.client.model.Projections.include;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.checkstyle.persistence.CheckstyleAuditDAO;
import org.repositoryminer.findbugs.persistence.FindBugsDAO;
import org.repositoryminer.persistence.dao.CodeAnalysisDAO;
import org.repositoryminer.pmd.cpd.persistence.CPDDAO;
import org.repositoryminer.util.HashingUtils;

import com.mongodb.client.model.Projections;

public class CodeIndicatorsAnalyzer {

	private CodeAnalysisDAO directAnalysisHandler = new CodeAnalysisDAO();
	private CPDDAO cpdHandler = new CPDDAO();
	private FindBugsDAO bugHandler = new FindBugsDAO();
	private CheckstyleAuditDAO checkstyleHandler = new CheckstyleAuditDAO();

	private Map<TechnicalDebtIndicator, Integer> indicators = new HashMap<TechnicalDebtIndicator, Integer>();

	public Map<TechnicalDebtIndicator, Integer> detect(String filename, String filestate, String snapshot) {
		indicators.clear();

		long filehash = HashingUtils.encodeToCRC32(filename);
		detecCodeSmells(filehash, filestate, snapshot);
		detectDuplicatedCode(filehash, snapshot);
		detectBugs(filehash, snapshot);
		detectStyleProblems(filehash, snapshot);

		return indicators;
	}

	private void addValueToIndicator(TechnicalDebtIndicator indicator, int value) {
		if (!indicators.containsKey(indicator))
			indicators.put(indicator, value);
		else {
			int newValue = indicators.get(indicator) + value;
			indicators.replace(indicator, newValue);
		}
	}


	@SuppressWarnings("unchecked")
	public void detecCodeSmells(long filehash, String filestate, String snapshot) {
		Document doc = directAnalysisHandler.findByFileAndCommit(filehash, filestate, include("types"));
		if (doc == null) 
			return;

		List<Document> classes = (List<Document>) doc.get("types");

		for (int i = 0; i < classes.size(); i++) {
			for (Document codesmell : (List<Document>) classes.get(i).get("codesmells")) {
				TechnicalDebtIndicator indicator = TechnicalDebtIndicator
						.getTechnicalDebtIndicator(codesmell.getString("codesmell"));

				if (indicator == null)
					continue;

				addValueToIndicator(indicator, 1);
			}

			List<Document> methods = (List<Document>) doc.get("methods");
			for (int j = 0; j < methods.size(); i++) {
				for (Document codesmell : (List<Document>) classes.get(i).get("codesmells")) {
					TechnicalDebtIndicator indicator = TechnicalDebtIndicator
							.getTechnicalDebtIndicator(codesmell.getString("codesmell"));

					if (indicator == null)
						continue;

					addValueToIndicator(indicator, 1);
				}
			}
		}
	}

	public void detectDuplicatedCode(long fileshash, String snapshot) {
		long occurrences = cpdHandler.countOccurrences(fileshash, snapshot);
		if (occurrences > 0) 
			addValueToIndicator(TechnicalDebtIndicator.DUPLICATED_CODE, new Long(occurrences).intValue());
	}

	@SuppressWarnings("unchecked")
	public void detectBugs(long fileshash, String snapshot) {
		Document doc = bugHandler.findByFile(fileshash, snapshot, include("bugs.category"));
		if (doc == null)
			return;

		List<Document> bugs = (List<Document>) doc.get("bugs");
		addValueToIndicator(TechnicalDebtIndicator.AUTOMATIC_STATIC_ANALYSIS_ISSUES, bugs.size());

		for (Document bug : bugs) {
			String category = bug.getString("category");
			TechnicalDebtIndicator indicator = null;

			if (category.equals("MT_CORRECTNESS"))
				indicator = TechnicalDebtIndicator.MULTITHREAD_CORRECTNESS;
			else if (category.equals("PERFORMANCE"))
				indicator = TechnicalDebtIndicator.SLOW_ALGORITHM;

			if (indicator == null)
				continue;

			addValueToIndicator(indicator, 1);
		}
	}

	@SuppressWarnings("unchecked")
	public void detectStyleProblems(long fileshash, String snapshot) {
		Document doc = checkstyleHandler.findByFile(fileshash, snapshot, Projections.include("style_problems.line"));
		if (doc == null)
			return;

		List<Document> problems = (List<Document>) doc.get("style_problems");
		addValueToIndicator(TechnicalDebtIndicator.CODE_WITHOUT_STANDARDS, problems.size());
	}

}