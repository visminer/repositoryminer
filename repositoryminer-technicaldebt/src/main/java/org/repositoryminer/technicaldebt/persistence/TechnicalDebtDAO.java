package org.repositoryminer.technicaldebt.persistence;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.repositoryminer.persistence.GenericDAO;

import com.mongodb.client.model.Filters;

public class TechnicalDebtDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_technical_debt";

	public TechnicalDebtDAO() {
		super(COLLECTION_NAME);
	}

	public void deleteByCommit(String hash) {
		deleteMany(Filters.eq("commit", hash));
	}
	
	/**
	 * Retrieves an analyzed file.
	 * 
	 * @param fileHash
	 *            the file hash.
	 * @param commit
	 *            the commit id.
	 * @param projection
	 *            the query projection.
	 * @return the technical debt analysis.
	 */
	public Document findByFileAndCommit(long fileHash, String commit, Bson projection) {
		return findOne(Filters.and(Filters.eq("filehash", fileHash), Filters.eq("commit", commit)), projection);
	}

	public void deleteByReport(ObjectId report) {
		deleteMany(Filters.eq("analysis_report", report));
	}

	public List<Document> findByCommit(String commit,  Bson projection) {
		return findMany(Filters.eq("commit", commit), projection);
	}
	
	public List<Document> findByReport(ObjectId report, Bson projection) {
		return findMany(Filters.eq("analysis_report", report), projection);
	}
	
}
