package org.repositoryminer.technicaldebt.code;

import static com.mongodb.client.model.Projections.include;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.findbugs.persistence.FindBugsDocumentHandler;
import org.repositoryminer.persistence.handler.DirectCodeAnalysisDocumentHandler;
import org.repositoryminer.pmd.cpd.persistence.CPDDocumentHandler;
import org.repositoryminer.technicaldebt.TechnicalDebtId;
import org.repositoryminer.technicaldebt.TechnicalDebtIndicator;
import org.repositoryminer.utility.StringUtils;

public class CodeDebt implements ITechnicalCodeDebt {

	private DirectCodeAnalysisDocumentHandler directAnalysisHandler;
	private CPDDocumentHandler cpdHandler;
	private FindBugsDocumentHandler bugHandler;
	private Map<String, Long> indicators;

	public CodeDebt() {
		directAnalysisHandler = new DirectCodeAnalysisDocumentHandler();
		cpdHandler = new CPDDocumentHandler();
		bugHandler = new FindBugsDocumentHandler();
	}

	@Override
	public TechnicalDebtId getId() {
		return TechnicalDebtId.CODE_DEBT;
	}

	@Override
	public Document detect(String filename, String filestate, String snapshot) {
		indicators = new HashMap<String, Long>();
		
		long filehash = StringUtils.encodeToCRC32(filename);
		detecCodeSmells(filehash, filestate);
		detectDuplicatedCode(filehash, snapshot);
		detectBugs(filehash, snapshot);
		
		if (indicators.size() == 0) {
			return null;
		}
		
		Document indicatorsDoc = new Document();
		indicatorsDoc.putAll(indicators);
		return new Document("debt", TechnicalDebtId.CODE_DEBT.toString()).append("indicators", indicatorsDoc);
	}

	@SuppressWarnings("unchecked")
	public void detecCodeSmells(long filehash, String filestate) {
		Document doc = directAnalysisHandler.getByFileAndCommit(filehash, filestate, include("classes"));

		if (doc == null) {
			return;
		}

		List<Document> classes = (List<Document>) doc.get("classes");

		for (int i = 0; i < classes.size(); i++) {
			for (Document codesmell : (List<Document>) classes.get(i).get("codesmells")) {
				TechnicalDebtIndicator indicator = TechnicalDebtIndicator.valueOf(codesmell.getString("codesmell"));

				if (indicator == null) {
					continue;
				}

				if (!indicators.containsKey(indicator.toString())) {
					indicators.put(indicator.toString(), 0l);
				}

				if (indicator == TechnicalDebtIndicator.COMPLEX_METHOD
						|| indicator == TechnicalDebtIndicator.BRAIN_METHOD) {
					List<Document> methods = (List<Document>) codesmell.get("methods");
					indicators.put(indicator.toString(), indicators.get(indicator.toString()) + methods.size());
				} else {
					indicators.put(indicator.toString(), indicators.get(indicator.toString()) + 1);
				}

			}
		}
	}

	public void detectDuplicatedCode(long fileshash, String snapshot) {
		long occurrences = cpdHandler.countOccurrences(fileshash, snapshot);

		if (occurrences == 0) {
			return;
		}

		if (occurrences > 0) {
			indicators.put(TechnicalDebtIndicator.DUPLICATED_CODE.toString(), occurrences);
		}
	}

	@SuppressWarnings("unchecked")
	public void detectBugs(long fileshash, String snapshot) {
		Document doc = bugHandler.findByFile(fileshash, snapshot, include("bugs.category"));

		if (doc == null) {
			return;
		}
		
		List<Document> bugs = (List<Document>) doc.get("bugs");
		
		for (Document bug : bugs) {
			String category = bug.getString("category");
			TechnicalDebtIndicator indicator = null;

			if (category.equals("BAD_PRACTICE") || category.equals("STYLE") || category.equals("CORRECTNESS")) {
				indicator = TechnicalDebtIndicator.AUTOMATIC_STATIC_ANALYSIS_ISSUES;
			} else if (category.equals("MT_CORRECTNESS")) {
				indicator = TechnicalDebtIndicator.MULTITHREAD_CORRECTNESS;
			} else if (category.equals("PERFORMANCE")) {
				indicator = TechnicalDebtIndicator.SLOW_ALGORITHM;
			}

			if (indicator == null) {
				continue;
			}

			if (!indicators.containsKey(indicator.toString())) {
				indicators.put(indicator.toString(), 0l);
			}

			indicators.put(indicator.toString(), indicators.get(indicator.toString()) + 1);
		}
	}

}