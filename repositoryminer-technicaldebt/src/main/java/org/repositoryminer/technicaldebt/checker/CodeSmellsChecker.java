package org.repositoryminer.technicaldebt.checker;

import static org.repositoryminer.technicaldebt.model.TDIndicator.BRAIN_METHOD;
import static org.repositoryminer.technicaldebt.model.TDIndicator.COMPLEX_METHOD;
import static org.repositoryminer.technicaldebt.model.TDIndicator.DATA_CLASS;
import static org.repositoryminer.technicaldebt.model.TDIndicator.FEATURE_ENVY;
import static org.repositoryminer.technicaldebt.model.TDIndicator.GOD_CLASS;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.metrics.persistence.CodeAnalysisDAO;
import org.repositoryminer.metrics.persistence.CodeAnalysisReportDAO;
import org.repositoryminer.technicaldebt.model.TDIndicator;
import org.repositoryminer.technicaldebt.model.TDItem;

import com.mongodb.client.model.Projections;

public class CodeSmellsChecker extends Checker {

	@SuppressWarnings("unchecked")
	@Override
	public void check(String commit) {
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

	@Override
	public List<TDIndicator> getIndicators() {
		return Arrays.asList(GOD_CLASS, COMPLEX_METHOD, FEATURE_ENVY, BRAIN_METHOD, DATA_CLASS);
	}

}
