package org.repositoryminer.technicaldebt.checker;

import static org.repositoryminer.technicaldebt.model.TDIndicator.COMMENT_ANALYSIS_ARCHITECTURE_DEBT;
import static org.repositoryminer.technicaldebt.model.TDIndicator.COMMENT_ANALYSIS_BUILD_DEBT;
import static org.repositoryminer.technicaldebt.model.TDIndicator.COMMENT_ANALYSIS_CODE_DEBT;
import static org.repositoryminer.technicaldebt.model.TDIndicator.COMMENT_ANALYSIS_DEFECT_DEBT;
import static org.repositoryminer.technicaldebt.model.TDIndicator.COMMENT_ANALYSIS_DESIGN_DEBT;
import static org.repositoryminer.technicaldebt.model.TDIndicator.COMMENT_ANALYSIS_DOCUMENTATION_DEBT;
import static org.repositoryminer.technicaldebt.model.TDIndicator.COMMENT_ANALYSIS_PEOPLE_DEBT;
import static org.repositoryminer.technicaldebt.model.TDIndicator.COMMENT_ANALYSIS_REQUIREMENT_DEBT;
import static org.repositoryminer.technicaldebt.model.TDIndicator.COMMENT_ANALYSIS_TEST_DEBT;
import static org.repositoryminer.technicaldebt.model.TDIndicator.COMMENT_ANALYSIS_UNKNOWN_DEBT;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.excomment.persistence.ExCommentDAO;
import org.repositoryminer.technicaldebt.model.TDIndicator;
import org.repositoryminer.technicaldebt.model.TDItem;

import com.mongodb.client.model.Projections;

public class ExCommentChecker extends Checker {

	@SuppressWarnings("unchecked")
	@Override
	public void check(String commit) {
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

	@Override
	public List<TDIndicator> getIndicators() {
		return Arrays.asList(COMMENT_ANALYSIS_ARCHITECTURE_DEBT, COMMENT_ANALYSIS_BUILD_DEBT,
				COMMENT_ANALYSIS_CODE_DEBT, COMMENT_ANALYSIS_DEFECT_DEBT, COMMENT_ANALYSIS_DESIGN_DEBT, 
				COMMENT_ANALYSIS_DOCUMENTATION_DEBT, COMMENT_ANALYSIS_PEOPLE_DEBT,
				COMMENT_ANALYSIS_REQUIREMENT_DEBT, COMMENT_ANALYSIS_TEST_DEBT, COMMENT_ANALYSIS_UNKNOWN_DEBT);
	}

}
