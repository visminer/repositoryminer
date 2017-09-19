package org.repositoryminer.persistence.dao;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.repositoryminer.model.ReferenceType;

import com.mongodb.client.model.Filters;

/**
 * This class handles rm_reference collection operations.
 */
public class ReferenceDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_reference";

	public ReferenceDAO() {
		super(COLLECTION_NAME);
	}

	/**
	 * Retrieves a reference by path.
	 * 
	 * @param path
	 *            the reference path.
	 * @param repositoryId
	 *            the repository id.
	 * @param projection
	 *            the query projection.
	 * @return a reference.
	 */
	public Document findByPath(String path, String repositoryId, Bson projection) {
		return findOne(Filters.and(Filters.eq("path", path), Filters.eq("repository", new ObjectId(repositoryId))),
				projection);
	}

	/**
	 * Retrieves all reference from a repository.
	 * 
	 * @param repositoryId
	 *            the repository id.
	 * @param projection
	 *            the query projection.
	 * @return list of references.
	 */
	public List<Document> getByRepository(String repositoryId, Bson projection) {
		return findMany(Filters.eq("repository", new ObjectId(repositoryId)), projection);
	}

	/**
	 * Updates the commits in a reference.
	 * 
	 * @param id
	 *            reference id.
	 * @param commits
	 *            the new commits.
	 */
	public void updateOnlyCommits(String id, List<String> commits) {
		collection.updateOne(Filters.eq("_id", new ObjectId(id)),
				new Document("$set", new Document("commits", commits)));
	}

	/**
	 * Retrieves a reference by name and type.
	 * 
	 * @param name
	 * @param type
	 * @param repositoryId
	 * @param projection
	 * @return
	 */
	public Document findByNameAndType(String name, ReferenceType type, String repositoryId, Bson projection) {
		return findOne(Filters.and(Filters.eq("name", name), Filters.eq("type", type.toString()), Filters.eq("repository", new ObjectId(repositoryId))),
				projection);
	}

}