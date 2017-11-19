package org.repositoryminer.pmd.cpd.persistence;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.repositoryminer.persistence.GenericDAO;

import com.mongodb.client.model.Filters;

public class CPDDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "pmd_cpd_analysis";
	
	public CPDDAO() {
		super(COLLECTION_NAME);
	}

	public void deleteByCommit(String hash) {
		deleteMany(Filters.eq("commit", hash));
	}
	
	public List<Document> findByCommit(String hash, Bson projection) {
		return findMany(Filters.eq("commit", hash), projection);
	}
	
}