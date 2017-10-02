package org.repositoryminer.persistence.dao;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.model.Filters;

/**
 * This class handles rm_repository collection.
 */
public class RepositoryDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_repository";

	public RepositoryDAO() {
		super(COLLECTION_NAME);
	}

	/**
	 * Finds a repository by its key.
	 * 
	 * @param key
	 *            the repository key.
	 * @param projection
	 *            the query projection.
	 * @return the found repository.
	 */
	public Document findByKey(String key, Bson projection) {
		return findOne(Filters.eq("key", key), projection);
	}

	/**
	 * Checks if a repository was already mined.
	 * 
	 * @param id
	 *            the repository id.
	 * @return true if the repository was mined or false otherwise.
	 */
	public boolean wasMined(String key) {
		return super.count(Filters.eq("key", key)) > 0;
	}

	/**
	 * Update contributors in the repository.
	 * 
	 * @param id
	 *            the repository id.
	 * @param contributors
	 *            the contributors.
	 */
	public void updateOnlyContributors(String id, List<Document> contributors) {
		collection.updateOne(Filters.eq("_id", new ObjectId(id)),
				new Document("$set", new Document("contributors", contributors)));
	}

}