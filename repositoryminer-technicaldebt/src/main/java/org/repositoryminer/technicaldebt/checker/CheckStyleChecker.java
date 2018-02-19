package org.repositoryminer.technicaldebt.checker;

import static org.repositoryminer.technicaldebt.model.TDIndicator.CODE_WITHOUT_STANDARDS;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.checkstyle.persistence.CheckstyleAuditDAO;
import org.repositoryminer.technicaldebt.model.TDIndicator;
import org.repositoryminer.technicaldebt.model.TDItem;

import com.mongodb.client.model.Projections;

public class CheckStyleChecker extends Checker {

	@SuppressWarnings("unchecked")
	@Override
	public void check(String commit) {
		CheckstyleAuditDAO dao = new CheckstyleAuditDAO();
		List<Document> analysisDoc = dao.findByCommit(commit, Projections.include("filename", "style_problems.line"));

		for (Document fileDoc : analysisDoc) {
			TDItem tdItem = searchFile(fileDoc.getString("filename"));
			addTDIndicator(tdItem, CODE_WITHOUT_STANDARDS, ((List<Document>) fileDoc.get("style_problems")).size());
		}
	}

	@Override
	public List<TDIndicator> getIndicators() {
		return Arrays.asList(CODE_WITHOUT_STANDARDS);
	}

}
