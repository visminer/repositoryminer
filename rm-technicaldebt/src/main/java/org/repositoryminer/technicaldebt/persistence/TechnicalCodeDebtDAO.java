package org.repositoryminer.technicaldebt.persistence;

import org.repositoryminer.persistence.dao.GenericDAO;

public class TechnicalCodeDebtDAO extends GenericDAO {

	private final static String COLLECTION_NAME = "technical_debt_analysis";
	
	public TechnicalCodeDebtDAO() {
		super(COLLECTION_NAME);
	}
	
}
