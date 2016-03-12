package br.edu.ufba.softvis.visminer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.constant.TreeType;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9 User friendly tree bean class. This class will be used for user
 *          interface.
 */
public class Tree {
	private Repository repository;

	private String uid;
	private Date lastUpdate;
	private String name;
	private String fullName;
	private TreeType type;

	private List<Commit> commits;

	/**
	 * @param id
	 * @param lastUpdate
	 * @param name
	 * @param fullName
	 * @param type
	 */
	public Tree(String uid, Date lastUpdate, String name, String fullName, TreeType type) {
		super();
		this.uid = uid;
		this.lastUpdate = lastUpdate;
		this.name = name;
		this.fullName = fullName;
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate
	 *            the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the type
	 */
	public TreeType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(TreeType type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public List<Commit> getCommits() {
		return commits;
	}

	/**
	 * @param commits
	 */
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	/**
	 * @return
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * @param repository
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	@Override
	public int hashCode() {
		return uid.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tree other = (Tree) obj;
		return uid.equals(other.getUid());
	}

	@Override
	public String toString() {
		return name;
	}

	public Document toDocument() {
		Document doc = new Document("uid", getUid())            
                .append("lastUpdate", getLastUpdate())
                .append("name", getName())
                .append("fullName", getFullName())
                .append("type", getType().name())
                .append("repository", getRepository().getUid());

		if (commits != null) {
			List<Document> commitsDoc = new ArrayList<Document>();

			for (Commit commit : commits) {
				commitsDoc.add(commit.toDocument());
			}

			doc.append("commits", commitsDoc);
		}

		return doc;
	}

}