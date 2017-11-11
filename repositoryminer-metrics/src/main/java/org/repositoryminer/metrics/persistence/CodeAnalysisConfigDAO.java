package org.repositoryminer.metrics.persistence;

import org.repositoryminer.persistence.GenericDAO;

public class CodeAnalysisConfigDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_code_analysis_config";

	public CodeAnalysisConfigDAO() {
		super(COLLECTION_NAME);
	}

}