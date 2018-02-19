package org.repositoryminer.technicaldebt.checker;

import static org.repositoryminer.technicaldebt.model.TDIndicator.DUPLICATED_CODE;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.pmd.cpd.persistence.CPDDAO;
import org.repositoryminer.technicaldebt.model.TDIndicator;
import org.repositoryminer.technicaldebt.model.TDItem;

import com.mongodb.client.model.Projections;

public class CPDChecker extends Checker {

	@SuppressWarnings("unchecked")
	@Override
	public void check(String commit) {
		CPDDAO dao = new CPDDAO();
		List<Document> analysisDoc = dao.findByCommit(commit, Projections.include("filename", "occurrences.filename"));

		for (Document doc : analysisDoc) {
			for (Document occurrence : (List<Document>) doc.get("occurrences", List.class)) {
				TDItem tdItem = searchFile(occurrence.getString("filename"));
				addTDIndicator(tdItem, DUPLICATED_CODE, 1);
			}
		}
	}

	@Override
	public List<TDIndicator> getIndicators() {
		return Arrays.asList(DUPLICATED_CODE);
	}

}
