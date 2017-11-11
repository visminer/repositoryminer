package org.repositoryminer.persistence;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.repositoryminer.domain.ReferenceType;

import com.mongodb.client.model.Filters;

/**
 * This class handles rm_reference collection.
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
	public List<Document> findByRepository(String repositoryId, Bson projection) {
		return findMany(Filters.eq("repository", new ObjectId(repositoryId)), projection);
	}

	/**
	 * Finds a reference that contains certain commit.
	 * 
	 * @param repositoryId
	 *            the repository id.
	 * @param commitId
	 *            the commit id.
	 * @param projection
	 *            the query projection.
	 * @return a reference.
	 */
	public Document findByCommit(String repositoryId, String commitId, Bson projection) {
		return findOne(Filters.and(Filters.eq("repository", new ObjectId(repositoryId)),
				Filters.in("commits", Arrays.asList(commitId))), projection);
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
	 * Finds a reference by name and type.
	 * 
	 * @param repositoryId
	 *            the repository id
	 * @param name
	 *            the reference name
	 * @param type
	 *            the reference type
	 * @param projection
	 *            the query projection
	 * @return a reference.
	 */
	public Document findByNameAndType(String name, ReferenceType type, String repositoryId, Bson projection) {
		return findOne(Filters.and(Filters.eq("repository", new ObjectId(repositoryId)), Filters.eq("name", name),
				Filters.eq("type", type.toString())), projection);
	}

}