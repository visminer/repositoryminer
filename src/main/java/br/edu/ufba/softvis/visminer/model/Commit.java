package br.edu.ufba.softvis.visminer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

/**
 * User friendly commit bean class. This class will be used for user interface.
 */
public class Commit {
	private Repository repository;

	private String uid;
	private Date date;
	private String message;
	private String name;

	private Committer committer;

	private List<File> commitedFiles;

	public Commit() {
	}

	/**
	 * @param uid
	 * @param date
	 * @param message
	 * @param name
	 */
	public Commit(String uid, Date date, String message, String name) {
		super();
		this.uid = uid;
		this.date = date;
		this.message = message;
		this.name = name;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param sha
	 *            the uid of the commit
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * @return the commitedFiles
	 */
	public List<File> getCommitedFiles() {
		return commitedFiles;
	}

	/**
	 * @param commitedFiles
	 *            the commitedFiles to set
	 */
	public void setCommitedFiles(List<File> commitedFiles) {
		this.commitedFiles = commitedFiles;
	}

	/**
	 * @return the committer
	 */
	public Committer getCommitter() {
		return committer;
	}

	/**
	 * @param committer
	 *            the committer to set
	 */
	public void setCommitter(Committer committer) {
		this.committer = committer;
	}

	/**
	 * @return the repository
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * @param repository
	 *            the repository to set
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Commit)) {
			return false;
		}
		Commit other = (Commit) obj;
		return uid.equals(other.getUid());
	}

	/**
	 * @return mongo's document format of commit
	 */
	public Document toDocument() {
		Document doc;
		if (repository != null) {
			doc = new Document("repository", getRepository().getUid()).append("uid", getUid());
		} else {
			doc = new Document("uid", getUid());
		}
		doc.append("date", getDate()).append("name", getName()).append("message", getMessage());

		if (committer != null) {
			doc.append("committer", committer.toDocument());
		}
		if (commitedFiles != null) {
			List<Document> filesDoc = new ArrayList<Document>();
			for (File file : commitedFiles) {
				filesDoc.add(file.toDocument());
			}

			doc.append("files", filesDoc);
		}

		return doc;
	}

}