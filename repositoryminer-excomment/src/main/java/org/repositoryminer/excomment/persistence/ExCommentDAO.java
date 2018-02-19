package org.repositoryminer.excomment.persistence;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.repositoryminer.persistence.GenericDAO;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;

public class ExCommentDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "excomment_comments_analysis";
	
	public ExCommentDAO() {
		super(COLLECTION_NAME);
	}

	public Document findByFile(long filehash, String commit, Bson projection) {
		Bson clause1 = new BasicDBObject("filehash", filehash);
		Bson clause2 = new BasicDBObject("commit", commit);
		return findOne(Filters.and(clause1, clause2), projection);
	}

	public void deleteByCommit(String hash) {
		deleteMany(Filters.eq("commit", hash));
	}
	
	public List<Document> findByCommit(String hash, Bson projection) {
		return findMany(Filters.eq("commit", hash), projection);
	}
	
}
