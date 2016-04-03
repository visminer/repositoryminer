package br.edu.ufba.softvis.visminer.model;

/**
 * User friendly commit bean class. This class will be used for user interface.
 */
public class AntiPattern {
	
	private File file;
	private Commit commit;
	private Committer committer;
	
	public AntiPattern(File file, Commit commit, Committer committer) {
		this.file = file;
		this.commit = commit;
		this.committer = committer;
	}
	

	public File getFile() {
		return file;
	}

	public Commit getCommit() {
		return commit;
	}
	public Committer getCommitter() {
		return committer;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	public void setCommitter(Committer committer) {
		this.committer = committer;
	}
}
