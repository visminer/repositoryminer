package org.repositoryminer.persistence.dao;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.model.Filters;

/**
 * This class handles rm_repository collection operations.
 */
public class RepositoryDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_repository";

	public RepositoryDAO() {
		super(COLLECTION_NAME);
	}

	/**
	 * Checks if a repository was already mined.
	 * 
	 * @param id
	 *            the repository id.
	 * @return true if the repository was mined or false otherwise.
	 */
	public boolean wasMined(String id) {
		return super.count(Filters.eq("_id", id)) > 0;
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
		collection.updateOne(Filters.eq("_id", new ObjectId(id)), new Document("$set", new Document("contributors", contributors)));
	}

}