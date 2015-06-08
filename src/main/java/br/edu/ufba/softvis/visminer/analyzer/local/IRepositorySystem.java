package br.edu.ufba.softvis.visminer.analyzer.local;

import java.util.Date;
import java.util.List;

import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.Committer;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.model.bean.Tree;

public interface IRepositorySystem {

	public void open(String repositoryPath);

	public String getAbsolutePath();

	public List<Committer> getCommitters();

	public List<Tree> getTrees();

	public List<String> getCommitsNames(String treeName);

	public Date getLastCommitDate(String treeName);

	public List<Commit> getCommits();

	public List<Commit> getCommitsByCommitter(String committerEmail);
	
	public List<Commit> getCommitsByTree(String treeName);

	public byte[] getData(String commitName, String filePath);

	public List<File> getCommitedFiles(String commitName);

	public void close();
	
}