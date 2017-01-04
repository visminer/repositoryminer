package org.repositoryminer.technicaldebt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bson.Document;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.WorkingDirectory;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.TechnicalDebtDocumentHandler;
import org.repositoryminer.persistence.handler.WorkingDirectoryDocumentHandler;
import org.repositoryminer.scm.ReferenceType;

import com.mongodb.client.model.Projections;

public class TDAnalyzer {

	private String repositoryId;
	private List<ITechnicalDebt> technicalDebts;
	
	private WorkingDirectoryDocumentHandler wdHandler;
	private TechnicalDebtDocumentHandler tdHandler;
	private ReferenceDocumentHandler refHandler;
	private CommitDocumentHandler commitHandler;
	
	private TDAnalyzer() {
		wdHandler = new WorkingDirectoryDocumentHandler();
		tdHandler = new TechnicalDebtDocumentHandler();
		refHandler = new ReferenceDocumentHandler();
		commitHandler = new CommitDocumentHandler();
	}
	
	public TDAnalyzer(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	public List<ITechnicalDebt> getTechnicalDebts() {
		if (technicalDebts == null) {
			technicalDebts = new ArrayList<ITechnicalDebt>();
		}
		return technicalDebts;
	}

	public void setTechnicalDebts(List<ITechnicalDebt> technicalDebts) {
		this.technicalDebts = technicalDebts;
	}

	public boolean addTechnicalDebt(ITechnicalDebt td) {
		for (ITechnicalDebt debt : getTechnicalDebts()) {
			if (debt.getId() == td.getId()) {
				return false;
			}
		}
		
		technicalDebts.add(td);
		return true;
	}
	
	public void analyze(String commit) {
	}
	
}