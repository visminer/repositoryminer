package org.repositoryminer.technicaldebt;

import static com.mongodb.client.model.Projections.include;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.checkstyle.persistence.CheckstyleDocumentHandler;
import org.repositoryminer.findbugs.persistence.FindBugsDocumentHandler;
import org.repositoryminer.persistence.handler.DirectCodeAnalysisDocumentHandler;
import org.repositoryminer.persistence.handler.IndirectCodeAnalysisDocumentHandler;
import org.repositoryminer.pmd.cpd.persistence.CPDDocumentHandler;
import org.repositoryminer.utility.StringUtils;

import com.mongodb.client.model.Projections;

public class CodeIndicatorsAnalyzer {

	private DirectCodeAnalysisDocumentHandler directAnalysisHandler = new DirectCodeAnalysisDocumentHandler();
	private IndirectCodeAnalysisDocumentHandler indirectAnalysisHandler = new IndirectCodeAnalysisDocumentHandler();
	
	private CPDDocumentHandler cpdHandler = new CPDDocumentHandler();
	private FindBugsDocumentHandler bugHandler = new FindBugsDocumentHandler();
	private CheckstyleDocumentHandler checkstyleHandler = new CheckstyleDocumentHandler();
	
	private Map<TechnicalDebtIndicator, Integer> indicators = new HashMap<TechnicalDebtIndicator, Integer>();

	public Map<TechnicalDebtIndicator, Integer> detect(String filename, String filestate, String snapshot) {
		indicators.clear();

		long filehash = StringUtils.encodeToCRC32(filename);
		detecCodeSmells(filehash, filestate, snapshot);
		detectDuplicatedCode(filehash, snapshot);
		detectBugs(filehash, snapshot);
		detectStyleProblems(filehash, snapshot);

		return indicators;
	}

	private void addValueToIndicator(TechnicalDebtIndicator indicator, int value) {
		if (!indicators.containsKey(indicator)) {
			indicators.put(indicator, value);
		} else {
			int newValue = indicators.get(indicator) + value;
			indicators.replace(indicator, newValue);
		}
	}

	
	public void detecCodeSmells(long filehash, String filestate, String snapshot) {
		Document doc = directAnalysisHandler.findByFileAndCommit(filehash, filestate, include("classes"));
		processClasses(doc);
		
		doc = indirectAnalysisHandler.findByFileAndSnapshot(filehash, snapshot, include("classes"));
		processClasses(doc);
	}

	@SuppressWarnings("unchecked")
	private void processClasses(Document doc) {
		if (doc == null) {
			return;
		}

		List<Document> classes = (List<Document>) doc.get("classes");

		for (int i = 0; i < classes.size(); i++) {
			for (Document codesmell : (List<Document>) classes.get(i).get("codesmells")) {
				TechnicalDebtIndicator indicator = TechnicalDebtIndicator
						.getTechnicalDebtIndicator(codesmell.getString("codesmell"));

				if (indicator == null) {
					continue;
				}
				
				if (indicator.equals(TechnicalDebtIndicator.COMPLEX_METHOD)
						|| indicator.equals(TechnicalDebtIndicator.BRAIN_METHOD)) {
					List<Document> methods = (List<Document>) codesmell.get("methods");
					addValueToIndicator(indicator, methods.size());
				} else {
					addValueToIndicator(indicator, 1);
				}

			}
		}
	}

	public void detectDuplicatedCode(long fileshash, String snapshot) {
		long occurrences = cpdHandler.countOccurrences(fileshash, snapshot);
		
		if (occurrences > 0) {
			addValueToIndicator(TechnicalDebtIndicator.DUPLICATED_CODE, new Long(occurrences).intValue());
		}
	}

	@SuppressWarnings("unchecked")
	public void detectBugs(long fileshash, String snapshot) {
		Document doc = bugHandler.findByFile(fileshash, snapshot, include("bugs.category"));

		if (doc == null) {
			return;
		}

		List<Document> bugs = (List<Document>) doc.get("bugs");
		addValueToIndicator(TechnicalDebtIndicator.AUTOMATIC_STATIC_ANALYSIS_ISSUES, bugs.size());
		
		for (Document bug : bugs) {
			String category = bug.getString("category");
			TechnicalDebtIndicator indicator = null;

			if (category.equals("MT_CORRECTNESS")) {
				indicator = TechnicalDebtIndicator.MULTITHREAD_CORRECTNESS;
			} else if (category.equals("PERFORMANCE")) {
				indicator = TechnicalDebtIndicator.SLOW_ALGORITHM;
			}

			if (indicator == null) {
				continue;
			}
			
			addValueToIndicator(indicator, 1);
		}
	}

	@SuppressWarnings("unchecked")
	public void detectStyleProblems(long fileshash, String snapshot) {
		Document doc = checkstyleHandler.findByFile(fileshash, snapshot, Projections.include("style_problems.line"));
		
		if (doc == null) {
			return;
		}
		
		List<Document> problems = (List<Document>) doc.get("style_problems");
		addValueToIndicator(TechnicalDebtIndicator.CODE_WITHOUT_STANDARDS, problems.size());
	}
	
}