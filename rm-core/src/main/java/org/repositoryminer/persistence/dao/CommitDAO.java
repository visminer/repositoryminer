package org.repositoryminer.persistence.dao;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

/**
 * This class handles rm_commit collection operations.
 */
public class CommitDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_commit";

	public CommitDAO() {
		super(COLLECTION_NAME);
	}

	@Override
	public Document findById(String id, Bson projection) {
		return findOne(Filters.eq("_id", id), projection);
	}

	/**
	 * Retrieves commits from a repository.
	 * 
	 * @param repositoryId
	 *            the repository id.
	 * @param projection
	 *            the query projection.
	 * @return a list of commits.
	 */
	public List<Document> findByRepository(String repositoryId, Bson projection) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("repository", new ObjectId(repositoryId));
		return findMany(whereClause, projection);
	}

	/**
	 * Retrieves commits from a id list. The commits are ordered by commit date.
	 * 
	 * @param idList
	 *            the id list.
	 * @param projection
	 *            the query projection.
	 * @return a list of commits.
	 */
	public List<Document> findByIdList(List<String> idList, Bson projection) {
		MongoCursor<Document> cursor = collection.find(Filters.in("_id", idList))
				.sort(new BasicDBObject("commit_date", 1)).projection(projection).iterator();
		return fromCursorToList(cursor);
	}

}