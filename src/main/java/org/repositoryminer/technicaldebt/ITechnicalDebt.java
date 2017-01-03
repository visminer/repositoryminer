package org.repositoryminer.technicaldebt;

import java.util.List;

import org.bson.Document;

public interface ITechnicalDebt {

	public List<Document> detect(String filename, String fileState, String repositoryState);
	
	public TechnicalDebtId getId();
	
}