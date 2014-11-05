package org.visminer.extractor;

import java.util.List;

import org.visminer.model.database.Commit;
import org.visminer.model.database.Committer;
import org.visminer.model.database.File;
import org.visminer.model.database.Tree;

public interface ILocalRepository {

	public void setLocalPath(String localPath);
	
	public List<Committer> getCommitters();
	
	public List<Commit> getCommits(Committer committer, String treeName);
	
	public List<Commit> getCommits(Committer committer);
	
	public List<Commit> getCommits(String treeName);
	
	public List<Tree> getTrees();
	
	public String getRepositoryAbsolutePath();
	
	public List<File> getCommitedFiles(String commitName);
	
	public List<String> getState(String name);
	
	public byte[] getFileState(String commitName, String filePath);
	
	public Commit getLastCommit(String tree);

	
}