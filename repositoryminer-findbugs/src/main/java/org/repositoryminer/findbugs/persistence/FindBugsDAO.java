package org.repositoryminer.findbugs.persistence;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.repositoryminer.persistence.GenericDAO;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;

public class FindBugsDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "findbugs_bugs_analysis";

	public FindBugsDAO() {
		super(COLLECTION_NAME);
	}

	public Document findByClasses(String[] classes, String commit, Bson projection) {
		Bson clause1 = Filters.in("bugs.class", classes);
		Bson clause2 = new BasicDBObject("commit", commit);
		return findOne(Filters.and(clause1, clause2), projection);
	}

	public Document findByFile(long filehash, String commit, Bson projection) {
		Bson clause1 = new BasicDBObject("filehash", filehash);
		Bson clause2 = new BasicDBObject("commit", commit);
		return findOne(Filters.and(clause1, clause2), projection);
	}

	public void deleteByCommit(String hash) {
		deleteMany(Filters.eq("commit", hash));
	}
	
}