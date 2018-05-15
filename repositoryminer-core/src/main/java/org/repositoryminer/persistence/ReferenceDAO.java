package org.repositoryminer.persistence;

import java.util.Arrays;
import java.util.Date;
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
	public List<Document> findByRepository(ObjectId repositoryId, Bson projection) {
		return findMany(Filters.eq("repository", repositoryId), projection);
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
	 * Updates a reference.
	 * 
	 * @param id
	 *            reference id.
	 * @param doc
	 *            the reference document.
	 */
	public void update(ObjectId id, Document doc) {
		collection.updateOne(Filters.eq("_id", id), doc);
	}

	/**
	 * Updates the commits and last commit date in a reference.
	 * 
	 * @param id
	 *            reference id.
	 * @param commits
	 *            the new commits.
	 * @param lastCommitDate
	 *            the new last commit date.
	 */
	public void updateCommitsAndLastCommitDate(ObjectId id, List<String> commits, Date lastCommitDate) {
		collection.updateOne(Filters.eq("_id", id),
				new Document("$set", new Document("commits", commits).append("last_commit_date", lastCommitDate)));
	}

	/**
	 * Deletes a reference by its id.
	 * 
	 * @param id
	 *            reference id.
	 */
	public void delete(ObjectId id) {
		collection.deleteOne(Filters.eq("_id", id));
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