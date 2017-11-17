package org.repositoryminer.technicaldebt.persistence;

import org.repositoryminer.persistence.GenericDAO;

public class TechnicalDebtDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_technical_debt";

	public TechnicalDebtDAO() {
		super(COLLECTION_NAME);
	}

}